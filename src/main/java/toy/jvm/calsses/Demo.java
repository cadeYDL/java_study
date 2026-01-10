package toy.jvm.calsses;

public class Demo {
    public static void main(String[] args) {
        System.out.println("Hello World");
        System.out.println(min(1,4));
        System.out.println(max(1,2));
        System.out.println(add(7,1));
        System.out.println(add(1,2,3));
        System.out.println(add("1","2"));
        System.out.println(add("1",1));
        System.out.println(add("1",1,1));
    }

    private static int add(String number, int i2, int i3) {
        return 1;
    }

    private static int add(String number, int i) {
        return 4;
    }

    private static int add(String number1, String number2) {
        return 7;
    }

    private static int min(int a,int b){
        return a<b?a:b;
    }

    private static int max(int a,int b){
        return a>b?a:b;
    }

    private static int add(int i,int j){
        if (i>0){
            return add(i-j,i)+1;
        }
        return -i+j;
    }

    private static int add(int i,int j,int k){
        if (i>0){
            return add(i-j,i,k)+1;
        }
        return -i+j+k;
    }
}
