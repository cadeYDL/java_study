package toy.list;

import toy.spring.web.Interceptor;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list.size());
        System.out.println("iterator--------");
        list.iterator().forEachRemaining(System.out::println);
        System.out.println("remove--------");
        for (int i = 0; i < 10; i++) {
            System.out.println(list.remove(0));
        }
        System.out.println(list.size());
        System.out.println("-------------");


        list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list.size());
        System.out.println("iterator--------");
        list.iterator().forEachRemaining(System.out::println);
        System.out.println("remove--------");
        for (int i = 0; i < 10; i++) {
            System.out.println(list.remove(0));
        }
        System.out.println(list.size());
        System.out.println("-------------");

    }
}
