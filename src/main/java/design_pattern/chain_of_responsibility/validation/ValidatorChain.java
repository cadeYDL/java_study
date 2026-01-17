package design_pattern.chain_of_responsibility.validation;

import design_pattern.chain_of_responsibility.excepation.ValidatorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ValidatorChain {
    private final List<ValidatorHandler> validators = new ArrayList<>();
    private final String feildName;
    public ValidatorChain(String feildName) {
        this.feildName = feildName;
    }

    public void append(ValidatorHandler validator) {
        validators.add(validator);
    }

    public void validate(Object value, ValidatorContext context)throws ValidatorException{
        int ticket = context.getTicket();
        for(;;){
            int inx = context.getInx(ticket);
            validators.get(inx).validate(Optional.ofNullable(context.getVelue(ticket)).orElse(value), context,ticket);
            if(inx == validators.size()-1||inx==context.getInx(ticket)){
                break;
            }
        }
    }


    public String GetFeildName() {
        return feildName;
    }
}
