package toy.spring;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanDefinition {
    @Getter
    private  final String name;
    @Getter
    private final Constructor<?> constructor;
    @Getter
    private final List<PostConstructEntry> portConstructorMethods;
    @Getter
    private final List<Field> autowiredFields;
    @Getter
    private final Class<?> beanClass;

    public BeanDefinition(Class<?> type) {
        beanClass = type;
        Component component = type.getDeclaredAnnotation(Component.class);
        name = component.name().isEmpty()?type.getSimpleName():component.name();
        try {
            constructor = type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        portConstructorMethods = Arrays.stream(type.getDeclaredMethods()).
                filter( m->m.getDeclaredAnnotation(PostConstruct.class)!=null)
                .map(m->new PostConstructEntry(m,m.getDeclaredAnnotation(PostConstruct.class).args())).toList();

        autowiredFields = Arrays.stream(type.getDeclaredFields()).
                filter(f->f.getDeclaredAnnotation(Autowired.class)!=null).toList();


    }



    class PostConstructEntry {
        @Getter
        Method method;
        @Getter
        String[] args;

        public PostConstructEntry(Method method, String[] args) {
            this.method = method;
            this.args = args;
        }
    }
}
