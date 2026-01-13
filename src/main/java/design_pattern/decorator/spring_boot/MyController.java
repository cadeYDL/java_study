package design_pattern.decorator.spring_boot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MyController {

    @PostMapping
    public Map<String,Object> origin(@TimesampleRequestResp Map<String,Object> json) {
        return json;
    }
}
