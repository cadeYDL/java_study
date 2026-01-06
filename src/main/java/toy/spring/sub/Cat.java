package toy.spring.sub;

import lombok.ToString;
import toy.spring.Autowired;
import toy.spring.Component;
import toy.spring.PostConstruct;

@Component(name="yuan_da")
public class Cat {
    private int age;
    @Autowired
    private Dog dog;
    @Autowired
    private Cat cat;
    @Autowired
    private Fish fish;

    @PostConstruct
    public void init() {
        System.out.println(this+" Cat init"+cat+" "+dog+" "+fish);
    }
}
