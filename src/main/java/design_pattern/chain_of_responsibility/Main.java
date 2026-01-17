package design_pattern.chain_of_responsibility;


import design_pattern.chain_of_responsibility.validation.Validator;

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User("yd",18,11);
        Validator validator = new Validator();
        validator.validate(user);
    }

}
