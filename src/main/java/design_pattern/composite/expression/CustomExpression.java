package design_pattern.composite.expression;

import design_pattern.composite.exception.ExpressionIsIllegal;

public class CustomExpression extends Expression {
    private String symbol;
    private Expression value;

    public CustomExpression(String symbol, Expression value){
        if (symbol==null||symbol.isEmpty()){
            throw new ExpressionIsIllegal("Custom:symbol is null or empty");
        }
        if (value==null){
            throw new ExpressionIsIllegal("Custom:value is null");
        }
        this.symbol = symbol;
        this.value = value;
    }
    @Override
    public int getValue() {
        if (valueCache==null){
            valueCache = value.getValue();
        }
        return valueCache;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
