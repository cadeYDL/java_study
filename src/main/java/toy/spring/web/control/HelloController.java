package toy.spring.web.control;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import toy.spring.Component;
import toy.spring.web.*;

@Component
@Controller
@RequestMapping(path = "/web")
public class HelloController {
    @RequestMapping(path = "/string")
    public String hello(@Param(value = "name") String name,@Param(value = "path",required = false) int path){
        return "hello"+" name:"+name+" path:"+path;
    }

    @RequestMapping(path = "/json")
    @ResponseBody
    public User json(@Param(value = "name") String name,@Param(value = "age") int age){
        return new User(name,age);
    }

    @RequestMapping(path = "/local")
    public ModelAndView local(@Param("name") String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("index.html");
        modelAndView.getContext().put("name", name);
        return modelAndView;
    }

    @RequestMapping(path = "/test")
    public ModelAndView test(@Param("name") String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("index.html");
        modelAndView.getContext().put("name", name);
        return modelAndView;
    }


    public class User{
        @Getter
        @Setter
        private String name;
        @Getter
        @Setter
        private int age;

        public User(String name, int age){
            this.name = name;
            this.age = age;
        }

        public User(){
        }


    }

}
