package toy.spring;

import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class ApplicationContext {
    private Map<String,Object> ioc = new HashMap<>();
    private Map<String,Object>loadingIOC = new HashMap<>();
    private Map<String,BeanDefinition> beanDefinitions = new HashMap<>();
    private  List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    public ApplicationContext(String packageName) throws Exception {
        initContext(packageName);
    }

    public Object getBean(String beanName) {
        if (ioc.containsKey(beanName)) {
            return ioc.get(beanName);
        }
        if (beanDefinitions.containsKey(beanName)){
            return createBean(beanDefinitions.get(beanName));
        }
        return null;
    }

    public <T> T getBean(Class<T> clazz) {
        return beanDefinitions.values().stream().
                filter((o)->clazz.isAssignableFrom(o.getBeanClass()))
                .findAny()
                .map(entry->entry.getName())
                .map(this::getBean)
                .map(b->(T)b)
                .orElse(null);
    }

    public <T> List<T> getBeans(Class<T> clazz) {
        return beanDefinitions.values().stream().
                filter((o)->clazz.isAssignableFrom(o.getBeanClass()))
                .map(entry->entry.getName())
                .map(this::getBean)
                .map(b->(T)b)
                .toList();
    }

    private List<Class<?>> scanPackages(String packageName) throws Exception {
        var resource = this.getClass().getClassLoader().getResource(packageName.replace(".", File.separator));
        Path path = Path.of(resource.getFile());
        List<Class<?>>  classList = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @SneakyThrows
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                Path absolutePath = file.toAbsolutePath();
                if (absolutePath.toString().endsWith(".class")) {
                    String replaceStr = absolutePath.toString().replace(".class", "").replace(File.separator, ".");
                    int index = replaceStr.indexOf(packageName);
                    String className = replaceStr.substring(index);
                    classList.add(Class.forName(className));
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classList;
    }

    public void initContext(String packageName) throws Exception {
        scanPackages(packageName).stream().filter(this::canScan).map(this::toBeanDefinition).forEach(b->this.beanDefinitions.put(b.getName(), b));
        initBeanPostProcessor();
        this.beanDefinitions.values().stream().forEach(this::createBean);
    }

    private void initBeanPostProcessor() {
        beanDefinitions.values().stream()
                .filter(bd->BeanPostProcessor.class.isAssignableFrom(bd.getBeanClass()))
                .map(this::createBean)
                .map(b->(BeanPostProcessor)b)
                .forEach(beanPostProcessors::add);
    }

    protected Object createBean(BeanDefinition beanDefinition) {
        if (ioc.containsKey(beanDefinition.getName())) {
            return ioc.get(beanDefinition.getName());
        }
        if (loadingIOC.containsKey(beanDefinition.getName())) {
            return loadingIOC.get(beanDefinition.getName());
        }
        return doCreateBean(beanDefinition);
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        try {
            Object bean = constructor.newInstance();
            loadingIOC.put(beanDefinition.getName(), bean);
            autowiredBean(bean,beanDefinition);
            bean = initializeBean(bean,beanDefinition);
            loadingIOC.remove(beanDefinition.getName());
            ioc.put(beanDefinition.getName(), bean);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object initializeBean(Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor postProcessor : beanPostProcessors) {
            bean = postProcessor.beforeInitializeBean(bean, beanDefinition.getName());
        }
        final Object newBean = bean;
        beanDefinition.getPortConstructorMethods().stream().filter(Objects::nonNull).forEach(entry-> {
            try {
                entry.getMethod().invoke(newBean,entry.args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        for (BeanPostProcessor postProcessor : beanPostProcessors) {
            bean = postProcessor.afterInitializeBean(bean, beanDefinition.getName());
        }
        return bean;
    }

    private void autowiredBean(Object bean, BeanDefinition beanDefinition) throws Exception {
        for(Field field:beanDefinition.getAutowiredFields()){
            field.setAccessible(true);
            Object fieldObj = getBean(field.getType());
            if (fieldObj==null && field.getDeclaredAnnotation(Autowired.class).required()) {
                throw new RuntimeException(field.getType().getName()+"未交付框架托管且该字段为必选");
            }
            field.set(bean,fieldObj);
        }
    }

    protected BeanDefinition toBeanDefinition(Class<?> aClass) {
        BeanDefinition beanDefinition = new BeanDefinition(aClass);
        if (beanDefinitions.containsKey(beanDefinition.getName())) {
            throw new RuntimeException("bean already exists");
        }
        beanDefinitions.put(beanDefinition.getName(), beanDefinition);
        return beanDefinition;
    }

    protected boolean canScan(Class<?> aClass) {
        return aClass.getDeclaredAnnotation(Component.class) != null;
    }
}
