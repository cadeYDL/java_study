package toy.jvm;

public class Main {
    public static void main(String[] args) throws Exception {
            HotSpot hotSpot = new HotSpot("toy.jvm.calsses.Demo","/Users/ydl/workspace/repo/java_study/build/classes/java/main");
            hotSpot.start();
        System.out.println(add(7,1));
    }

    private static int add(int i,int j){
        if (i>0){
            return add(i-j,i)+1;
        }
        return -i+j;
    }
}
