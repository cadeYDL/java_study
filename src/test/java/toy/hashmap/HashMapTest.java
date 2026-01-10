package toy.hashmap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class MyHashMapTest {

    int newCount = 0;
    @Test
    void testApi() {
        HashMap<String, String> myHashMap = new HashMap<>(10);
        int count = 100000;
        for (int i = 0; i < count; i++) {
            myHashMap.put(String.valueOf(i), String.valueOf(i));
        }

        assertEquals(count, myHashMap.getSize());

        for (int i = 0; i < count; i++) {
            assertEquals(String.valueOf(i), myHashMap.get(String.valueOf(i)));
        }

        myHashMap.remove("8");
        assertNull(myHashMap.get("8"));

        assertEquals(count - 1, myHashMap.getSize());


        myHashMap.forEach((key,val)->{
//            System.out.println("key:"+key+" val: "+val);
            newCount++;
            return true;
        });
        assertEquals(count - 1, newCount);
    }

}
