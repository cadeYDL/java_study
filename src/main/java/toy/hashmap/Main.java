package toy.hashmap;

public class Main {
    public static void main(String[] args) {
        HashMap<Integer,Integer> map = new HashMap(5);
        for(int i=0;i<10;i++){
            map.put(i,i);
        }
        int size = map.getSize();
        System.out.println("size:"+size);

        for(int i=0;i<10;i++){
            map.put(i,i);
        }
        int newSize = map.getSize();
        System.out.println(size+"=="+newSize);

        for(int i=100;i<110;i++){
            map.put(i,i);
        }
        for(int i=0;i<map.getSize();i++){
            System.out.println("get:"+map.get(i));
        }
        for(int i=0;i<10;i++){
            System.out.println("remove:"+map.remove(i));
        }

        map.forEach((k,v)->{
            System.out.println("key:"+k+" value:"+v);
            return true;
        });




    }
}
