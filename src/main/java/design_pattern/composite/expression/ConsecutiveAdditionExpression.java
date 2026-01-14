package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;

public class ConsecutiveAdditionExpression extends Expression {
    private static final String symbol = "@";
    static {
        Expression.builder.put(symbol,ConsecutiveAdditionExpression::new);
        Expression.priorityMap.put(symbol,0);
    }

    public ConsecutiveAdditionExpression(Expression left, Expression right) {
        if (left==null||right==null){
            throw new ExpressionIsIllegal("consecutiveAddition");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public int getValue() {
        int ans = 0;
        for (int i = left.getValue(); i >0 && i+ right.getValue()> left.getValue(); i--){
            ans += i;
        }
        return ans;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
