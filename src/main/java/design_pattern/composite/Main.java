package design_pattern.composite;

import design_pattern.composite.expression.Expression;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CalculationParser p = new CalculationParser();
        List<Expression> ps = p.getExpression("1+2*(3+4);x_x=7*(6+3)-1;y=1;x_x*(y+27)");
        for(Expression p1:ps){
            System.out.println("mid:"+p1.getMidExpression());
            System.out.println("suffer:"+p1.getSufferExpression());
            System.out.println("ans:"+p1.getValue());
        }

    }
}
