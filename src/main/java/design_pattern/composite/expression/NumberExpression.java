package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;

public class NumberExpression extends Expression {
    private int number;
    public NumberExpression(String numberStr) {
        try{
            this.number = Integer.parseInt(numberStr);
        }catch (NumberFormatException e){
            throw new ExpressionIsIllegal("number:"+e.getMessage());
        }
    }
    public int getValue(){
        return number;
    }

    @Override
    public String getSymbol() {
        return String.valueOf(number);
    }
}
