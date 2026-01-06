package toy.spring.sub;

import toy.spring.Autowired;
import toy.spring.Component;
import toy.spring.PostConstruct;

@Component
public class Fish {
    @Autowired
    private Cat cat;

    @PostConstruct
    public void init() {
        System.out.println(this+" Fish init"+cat);
    }
}
