package design_pattern.chain.validation;

import design_pattern.chain.excepation.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ValidatorContext {
    private final Map<String, Object> data = new HashMap<>();
    private final List<String> errors = new ArrayList<>();
    private final int[] Inxs;
    private boolean stop = false;
    private final AtomicInteger t =new AtomicInteger(0);
    private final Object[] objs;

    public ValidatorContext(int size) {
        Inxs = new int[size];
        objs = new Object[size];
    }

    public void throwExceptionIfNecessary() {
        if (errors.isEmpty()){
            return;
        }
        throw new ValidatorException(errors.toString());
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void stopNow() {
        stop = true;
    }


    public boolean sholdStop() {
        return stop;
    }

    public int getTicket() {
        return t.getAndIncrement();
    }

    public int getInx(int ticket) {
        return Inxs[ticket];
    }

    public void doNext(int ticket, Object obj) {
        Inxs[ticket]++;
        objs[ticket] = obj;
    }

    public Object getVelue(int ticket) {
        return objs[ticket];
    }
}
