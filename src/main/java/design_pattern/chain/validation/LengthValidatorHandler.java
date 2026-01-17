package design_pattern.chain.validation;

public class LengthValidatorHandler implements ValidatorHandler {
    private int value;
    private String name;
    public LengthValidatorHandler(int value,String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public void validate(Object obj, ValidatorContext context, int ticket) {
        if(obj instanceof String && ((String) obj).length() != value){
            context.addError(name+" length != " + value);
        }
        context.doNext(ticket,obj);
    }
}
