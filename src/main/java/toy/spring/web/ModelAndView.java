package toy.spring.web;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    @Getter
    @Setter
    private String view;
    @Getter
    private Map<String, String> context = new HashMap<>();
}
