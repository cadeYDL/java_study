package design_pattern.iterator;

import lombok.ToString;

@ToString
public class User {
    String name;
    int age;

    public User(String item) {
        String[] items = item.split(",");
        this.name = items[0];
        this.age = Integer.valueOf(items[1]);
    }
}
