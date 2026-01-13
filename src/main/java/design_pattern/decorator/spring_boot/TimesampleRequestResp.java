package design_pattern.decorator.spring_boot;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimesampleRequestResp {
}
