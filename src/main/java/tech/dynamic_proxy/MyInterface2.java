package tech.dynamic_proxy;

public interface MyInterface2 {
    int add(int a, int b);
}

class add implements MyInterface2 {
    @Override
    public int add(int a, int b) {
        return 0;
    }
}
