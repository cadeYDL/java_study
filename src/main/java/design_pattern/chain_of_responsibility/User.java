package design_pattern.chain_of_responsibility;

import design_pattern.chain_of_responsibility.annotation.Length;
import design_pattern.chain_of_responsibility.annotation.Max;
import design_pattern.chain_of_responsibility.annotation.Min;

public class User {
    @Length(3)
    private String name;

    @Max(17)
    @Min(20)
    private Integer age;

    @Max(value = 10,stop = true)
    @Min(value = 15)
    private Integer id;


    public User(String name, Integer age,Integer id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }
}
