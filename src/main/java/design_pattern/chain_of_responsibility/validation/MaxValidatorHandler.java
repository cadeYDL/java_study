package design_pattern.chain_of_responsibility.validation;

public class MaxValidatorHandler implements ValidatorHandler {
    private int value;
    private boolean stop;
    private String name;
    public MaxValidatorHandler(int value,boolean stop,String name) {
        this.value = value;
        this.stop = stop;
        this.name = name;
    }

    @Override
    public void validate(Object obj, ValidatorContext context, int ticket) {
        if(obj instanceof Integer && (Integer)obj > value){
            context.addError(name+" is over " + value);
            if (stop) {
                context.stopNow();
                return;
            }
        }
        context.doNext(ticket,obj);

    }
}
