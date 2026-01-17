package design_pattern.chain_of_responsibility.validation;

import design_pattern.chain_of_responsibility.annotation.Length;
import design_pattern.chain_of_responsibility.annotation.Max;
import design_pattern.chain_of_responsibility.annotation.Min;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {


    static Map<Class<?>, List<ValidatorChain>> cache = new HashMap<>();

    private  ValidatorChain buildChain(Field field) {
        ValidatorChain chain = new ValidatorChain(field.getName());
        Max max = field.getAnnotation(Max.class);
        if (max!=null){
            chain.append(new MaxValidatorHandler(max.value(),max.stop(),field.getName()));
        }

        Min min = field.getAnnotation(Min.class);
        if (min!=null){
            chain.append(new MinValidatorHandler(min.value(),field.getName()));
        }

        Length length = field.getAnnotation(Length.class);
        if (length!=null){
            chain.append(new LengthValidatorHandler(length.value(),field.getName()));
        }
        return chain;
    }



    public void validate(Object obj) throws Exception {
        List<ValidatorChain> chains = getChaines(obj);
        ValidatorContext context = new ValidatorContext(chains.size());
        for(ValidatorChain chain:chains){
            Field field = obj.getClass().getDeclaredField(chain.GetFeildName());
            field.setAccessible(true);
            chain.validate(field.get(obj),context);
            if (context.sholdStop()){
                break;
            }
        }
        context.throwExceptionIfNecessary();
    }

    private List<ValidatorChain> getChaines(Object obj) {
        Class<?> clazz = obj.getClass();
        if (!cache.containsKey(clazz)){
            List<ValidatorChain> chains = new ArrayList<>();
            for(Field field:clazz.getDeclaredFields()){
                ValidatorChain chain = buildChain(field);
                chains.add(chain);
            }
            cache.put(clazz,chains);
        }
        return cache.get(clazz);
    }

}
