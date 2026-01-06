package toy.spring;

public class Main {
    public static void main(String[] args) throws Exception {
         ApplicationContext ioc = new ApplicationContext("toy.spring");
        System.out.println(ioc.getBean("yuan_da"));
        System.out.println(ioc.getBean("Dog"));
        System.out.println(ioc.getBean("Fish"));
    }
}
