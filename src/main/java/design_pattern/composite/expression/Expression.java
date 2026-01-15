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
    private String postReviewNode(Expression node){
        if(node==null){
            return "";
        }
        String leftStr = postReviewNode(node.left);
        String rightStr = postReviewNode(node.right);
        return leftStr+" "+rightStr+" "+node.getSymbol();

    }


    private String midReviewNode(Expression node){
        if(node==null){
            return "";
        }
        int p = -1;
        if (priorityMap.containsKey(node.getSymbol())){
            p = priorityMap.get(node.getSymbol());
        }
        String leftStr = "";

        if (node.left!=null){
            leftStr = midReviewNode(node.left);
            int pLeft = 20;
            if (priorityMap.containsKey(node.left.getSymbol())){
                pLeft = priorityMap.get(node.left.getSymbol());
            }
            if (pLeft<p){
                leftStr = "("+leftStr+")";
            }
        }
        String rightStr = "";
        if (node.right!=null){
            rightStr = midReviewNode(node.right);
            int pRight = 20;
            if (priorityMap.containsKey(node.right.getSymbol())){
                pRight = priorityMap.get(node.right.getSymbol());
            }
            if(pRight<p){
                rightStr = "("+rightStr+")";
            }
        }
        return leftStr+" "+node.getSymbol()+" "+rightStr;

    }

    public String getMidExpression(){
        return midReviewNode(this);
    }

    public String getSufferExpression() {
        return postReviewNode(this);
    }


    protected Expression() {}
}
