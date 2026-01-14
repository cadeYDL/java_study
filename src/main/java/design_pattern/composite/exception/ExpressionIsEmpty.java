package design_pattern.composite.exception;

public class ExpressionIsEmpty extends ExpressionException {
    public ExpressionIsEmpty(String message) {
        super("表达式为空:"+message);
    }

    public ExpressionIsEmpty(){
        super("表达式为空");
    }
}
