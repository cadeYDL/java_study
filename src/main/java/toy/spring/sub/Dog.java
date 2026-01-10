package toy.spring.sub;


import toy.spring.Autowired;
import toy.spring.Component;
import toy.spring.PostConstruct;

@Component
public class Dog {

    @Autowired
    private Cat cat;

    @Autowired
    private Dog dog;

    @Autowired(required = true)
    private Bird brid;

    @PostConstruct
    public void init() {
        System.out.println(this+" Dog init"+cat+" "+dog+" "+brid );
    }
    @PostConstruct(args = {"z","y","y"})
    public void init2(String info1, String info2, String info3) {
        System.out.println("Dog init2"+info1+info2+info3);
    }

}
