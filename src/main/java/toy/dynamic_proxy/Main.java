package toy.dynamic_proxy;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        try {
            MyInterface obj = MyInterfaceFactory.createProxyObject(new HandlerTest1());
            obj.func1();
            obj.func2();
            obj.func3();

            obj = MyInterfaceFactory.createProxyObject(new HandlerTest3(obj));
            obj.func1();
            obj.func2();
            obj.func3();

            MyInterface2 add = MyInterfaceFactory.createProxyObject(new HandlerTest2());
            System.out.println(add.add(1,2));
            System.out.println(add.add(2,3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


class test1 implements MyInterface {

    @Override
    public void func1() {

    }

    @Override
    public void func2() {

    }

    @Override
    public void func3() {

    }
}