package toy.dynamic_proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicLong;

public class MyInterfaceFactory<T> {
    private static final AtomicLong counter = new AtomicLong();
    private static <T> File createJavaFile(String className,HandlerOp handler) throws IOException {
        String context = getInterfaceContext(className,handler);
        File javaFile = new File(className + ".java");
        Files.writeString(javaFile.toPath(),context);
        return javaFile;
    }


    private static  String getInterfaceContext(String className,HandlerOp handler) {
        Class<?> aClass = handler.getInterface();
        if (!aClass.isInterface()){
            throw new RuntimeException("类型必须为interface");
        }
        String block = "package tech.dynamic_proxy;\n";
        block += "public class "+className+" implements %s {\n";
        block += "    %s;\n";
        String is ="";
        String innerString ="";
        is += aClass.getSimpleName()+",";
        innerString += aClass.getSimpleName()+" inner"+";\n";
        for(Method m: aClass.getMethods()){
            block += "    @Override\n" +
                    "    public "+getMethodSign(m)+"{\n"+
                    "        "+handler.getBody(m.getName())+"\n"+
                    "    }\n\n";
        }


        block += "}";
        is = is.substring(0,is.length()-1);
        return String.format(block,is,innerString);
    }

    private static String getMethodSign(Method m) {
        String  sign = m.getReturnType().getSimpleName()+" " + m.getName()+"(%s)";
        String params = "";
        for (int i=0;i<m.getParameterCount();i++){
            params += m.getParameterTypes()[i].getSimpleName()+" "+ m.getParameters()[i].getName();
            if (i!=m.getParameterCount()-1){
                params += ",";
            }
        }
        sign = String.format(sign,params);
        return sign;
    }

    private static String getClassName(){
        return "MyInterfaceFactory$proxy" + counter.incrementAndGet();
    }

    private  static <T> T newInstance(String className,HandlerOp handler) throws Exception {
        Class<?> aClass = MyInterfaceFactory.class.getClassLoader().loadClass(className);
        Constructor<?> constructor = aClass.getConstructor();
        T proxy = (T) constructor.newInstance();
        Object proxys = handler.getProxy();
        if (proxys!=null){
            Field filed = aClass.getDeclaredField("inner");
            filed.setAccessible(true);
            filed.set(proxy,proxys);
        }
        return proxy;
    }

    public static <T> T createProxyObject(HandlerOp handler)throws Exception{
        String className = getClassName();
        File javaFile = createJavaFile(className,handler);
        Compiler.compile(javaFile);
        return newInstance("tech.dynamic_proxy."+className,handler);
    }


}