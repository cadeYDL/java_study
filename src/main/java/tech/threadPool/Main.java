package tech.threadPool;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MyThreadPool pool = new MyThreadPool(null,10,3,null,1);
        for(int i=0;i<10;i++){
            int finalI = i;
            pool.execute(()->{
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                   throw new RuntimeException(Thread.currentThread().getName());
                }
                System.out.println("子线程ok"+ finalI +Thread.currentThread().getName());
            });
        }
        System.out.println("主线程ok");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            System.exit(0);
        }
    }
}