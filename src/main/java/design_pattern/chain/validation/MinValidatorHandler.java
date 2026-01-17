package design_pattern.chain.validation;

public class MinValidatorHandler implements ValidatorHandler {
    private int value;
    private String name;
    public MinValidatorHandler(int value,String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public void validate(Object obj, ValidatorContext context, int ticket) {
        if(obj instanceof Integer && (Integer)obj < value){
            context.addError(name+"less than " + value);
        }
    }
}
