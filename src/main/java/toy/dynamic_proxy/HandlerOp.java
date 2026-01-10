package toy.dynamic_proxy;

public interface  HandlerOp {
    String getBody(String name);
    Class getInterface();

    default Object getProxy(){
        return null;
    }
}

class HandlerTest1 implements HandlerOp {
    @Override
    public String getBody(String name) {

        return String.format("System.out.println(\"%s\");\n",name);
    }

    @Override
    public Class getInterface() {
        return MyInterface.class;
    }
}

class HandlerTest2 implements HandlerOp {
    @Override
    public String getBody(String name) {
        return "return arg0+arg1;";
    }

    @Override
    public Class getInterface() {
        return MyInterface2.class;
    }
}

class HandlerTest3 implements HandlerOp {
    MyInterface inner;
    HandlerTest3(MyInterface inner) {
        this.inner = inner;
    }
    String name="inner";

    @Override
    public Object getProxy() {
        return this.inner;
    }

    @Override
    public String getBody(String name) {
        return "System.out.println(\"before\");\n"+
                this.name+"."+name+"();\n"+
                "System.out.println(\"after\");\n";
    }

    @Override
    public Class getInterface() {
        return MyInterface.class;
    }
}
