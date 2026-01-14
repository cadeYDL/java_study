package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;

public class SubExpression extends Expression {
    private static final String symbol = "-";
    static {
        Expression.builder.put(symbol,SubExpression::new);
        Expression.priorityMap.put(symbol,5);
    }

    @Override
    public int getValue() {
        return left.getValue() / right.getValue();
    }

    public SubExpression(Expression left, Expression right) {
        if (left==null||right==null){
            throw new ExpressionIsIllegal("sub");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
