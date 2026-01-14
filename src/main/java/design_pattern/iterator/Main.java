package design_pattern.iterator;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("src/main/java/design_pattern/iterator/objData");
        FileReader<User>  reader = new FileReader<>(User::new,3,file);
        for(User user : reader){
            System.out.println(user);
        }
    }
}
