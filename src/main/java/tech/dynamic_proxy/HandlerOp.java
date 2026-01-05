package tech.dynamic_proxy;

import java.lang.reflect.Field;

public interface  HandlerOp<T> {
    String getBody(String name);
    T getInterface();

    default Object[] getProxys(){
        return new Object[0];
    }
}

class HandlerTest1 implements HandlerOp<MyInterface> {
    @Override
    public String getBody(String name) {

        return String.format("System.out.println(\"%s\");\n",name);
    }

    @Override
    public MyInterface getInterface() {
        return new test1();
    }
}

class HandlerTest2 implements HandlerOp<MyInterface2> {
    @Override
    public String getBody(String name) {
        return "return arg0+arg1;";
    }

    @Override
    public MyInterface2 getInterface() {
        return new add();
    }
}

class HandlerTest3 implements HandlerOp<MyInterface> {
    MyInterface inner;
    HandlerTest3(MyInterface inner) {
        this.inner = inner;
    }
    String name="inner0";

    @Override
    public Object[] getProxys() {
        return new Object[]{this.inner};
    }

    @Override
    public String getBody(String name) {
        return "System.out.println(\"before\");\n"+
                this.name+"."+name+"();\n"+
                "System.out.println(\"after\");\n";
    }

    @Override
    public MyInterface getInterface() {
        return new test1();
    }
}
