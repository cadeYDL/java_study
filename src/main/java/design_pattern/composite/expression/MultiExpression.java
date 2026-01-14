package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;

public class MultiExpression extends Expression {
    private static final String symbol = "*";
    static {
        Expression.builder.put(symbol,MultiExpression::new);
        Expression.priorityMap.put(symbol,10);
    }

    @Override
    public int getValue() {
        return left.getValue() * right.getValue();
    }

    public MultiExpression(Expression left, Expression right) {
        if (left==null||right==null){
            throw new ExpressionIsIllegal("multi");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
