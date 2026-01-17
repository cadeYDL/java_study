package design_pattern.chain_of_responsibility.excepation;

public class ValidatorException extends RuntimeException {
    public ValidatorException(String message) {
        super(message);
    }
}
