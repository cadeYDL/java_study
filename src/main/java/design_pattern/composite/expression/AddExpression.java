package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;


public class AddExpression extends Expression {
    private static final String symbol = "+";
    static {
        Expression.builder.put(symbol,AddExpression::new);
        Expression.priorityMap.put(symbol,5);
    }

    public AddExpression(Expression left, Expression right){
        if (left==null||right==null){
            throw new ExpressionIsIllegal("add");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public int getValue() {
        return left.getValue() + right.getValue();
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
