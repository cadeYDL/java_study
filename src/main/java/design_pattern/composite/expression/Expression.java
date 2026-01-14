package design_pattern.composite.expression;


import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class Expression {
    Expression left;
    Expression right;
    Integer valueCache;

    public static final Map<String,Integer> priorityMap = new HashMap<>();

    public static final Map<String, BiFunction<Expression, Expression, Expression>> builder = new HashMap<>();

    abstract public int getValue();

    abstract public String getSymbol();

    protected Expression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    public String posrReviewNode(Expression node){
        if(node==null){
            return "";
        }
        String leftStr = posrReviewNode(node.left);
        String rightStr = posrReviewNode(node.right);
        return leftStr+" "+rightStr+" "+node.getSymbol();

    }
    public String toString() {
        return posrReviewNode(this);
    }


    protected Expression() {}
}
