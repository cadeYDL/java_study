package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;

public class DivTowExpression extends Expression {
    private static final String symbol = "/";
    static {
        Expression.builder.put(symbol,DivTowExpression::new);
        Expression.priorityMap.put(symbol,10);
    }


    public DivTowExpression(Expression left, Expression right) {
        if (left==null||right==null){
            throw new ExpressionIsIllegal("div");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public int getValue() {
        return this.left.getValue() / this.right.getValue();
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

}
