package design_pattern.chain.validation;

public interface ValidatorHandler {
    void validate(Object obj, ValidatorContext context, int ticket);
}
