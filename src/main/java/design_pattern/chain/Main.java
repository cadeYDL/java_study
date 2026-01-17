package design_pattern.chain;


import design_pattern.chain.excepation.ValidatorException;
import design_pattern.chain.validation.Validator;

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User("yd",18,11);
        Validator validator = new Validator();
        validator.validate(user);
    }

}
