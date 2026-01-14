package design_pattern.composite.exception;

public class ExpressionIsIllegal extends ExpressionException{
    public ExpressionIsIllegal(String message) {
        super("表达式不合法:"+message);
    }

    public ExpressionIsIllegal(){
        super("表达式不合法");
    }
}
