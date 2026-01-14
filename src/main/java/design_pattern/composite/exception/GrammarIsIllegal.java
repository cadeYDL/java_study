package design_pattern.composite.exception;

public class GrammarIsIllegal extends ExpressionException {
    public GrammarIsIllegal(String message) {
        super("语法不合法:"+message);
    }

    public GrammarIsIllegal(){
        super("语法不合法");
    }


}
