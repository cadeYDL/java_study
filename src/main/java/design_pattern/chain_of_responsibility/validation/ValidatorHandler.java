package design_pattern.chain_of_responsibility.validation;

public interface ValidatorHandler {
    void validate(Object obj, ValidatorContext context, int ticket);
}
