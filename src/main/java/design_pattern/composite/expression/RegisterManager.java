package design_pattern.composite.expression;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterManager {
    private final static AtomicBoolean registered = new AtomicBoolean(false);
    public static void register() throws ClassNotFoundException {
        if(registered.compareAndSet(false, true)){
            Class.forName("design_pattern.composite.expression.Expression");
            Reflections reflections = new Reflections("design_pattern.composite.expression", Scanners.SubTypes);
            var sub = reflections.getSubTypesOf(Expression.class);
            for(var s:sub){
                Class.forName(s.getName());
            }
        }

    }
}
