package design_pattern.composite;

import design_pattern.composite.exception.ExpressionException;
import design_pattern.composite.exception.ExpressionIsEmpty;
import design_pattern.composite.exception.GrammarIsIllegal;
import design_pattern.composite.expression.*;

import java.util.*;
import java.util.function.Function;

public class CalculationParser {

    private Map<String,CustomExpression> context = new HashMap<>();
    static {
        try {
            RegisterManager.register();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public  List<Expression> getExpression(String midParser)throws ExpressionException {
        String[] exps = midParser.split(";");
        if (exps.length==0){
            throw new ExpressionIsEmpty();
        }
        List<Expression> result = new ArrayList<>();
        for (String exp:exps){
            String[] e = exp.split("=");
            String symbol = "";
            String curExp = e[0];
            if (e.length==2){
                curExp = e[1];
                symbol = e[0];
            }
            Expression cur =  toPression(curExp);
            if (symbol.isEmpty()){
                result.add(cur);
                continue;
            }
            context.put(symbol,new CustomExpression(symbol,cur));
        }
        return result;
    }

    private Expression toPression(String curExp) {
        List<String> suffix = toSuffer(curExp);
        LinkedList<Expression> stack = new LinkedList<>();
        for (String item : suffix) {
            if (Expression.builder.containsKey(item)){
                if (stack.size()<2){
                    throw new GrammarIsIllegal("op");
                }
                Expression right = stack.pop();
                stack.push(Expression.builder.get(item).apply(stack.pop(),right));
                continue;
            }
            Expression e;
            if(context.containsKey(item)){
                e = context.get(item);
            }else{
                e = new NumberExpression(item);
            }
            stack.push(e);
        }
        if (stack.isEmpty()){
            throw new GrammarIsIllegal("stack");
        }
        return stack.pop();
    }
    private LinkedList<String> toSuffer(String curExp){
        curExp = curExp.trim();
        LinkedList<String> helper = new LinkedList<>();
        LinkedList<String> result = new LinkedList<>();
        for(int inx=0;inx<curExp.length();inx++){
            char c = curExp.charAt(inx);
            if (c=='('){
                helper.push("(");
                continue;
            }
            if (c==')'){
                if (helper.size()<2){
                    throw new GrammarIsIllegal(")");
                }
                if (helper.peek().equals("(")){
                    throw new GrammarIsIllegal("empty ()");
                }
                for(String item=helper.peek();!helper.isEmpty()&&!helper.peek().equals("(");item=helper.pop()){
                    result.add(item);
                }
                if (!helper.peek().equals("(")){
                    throw new GrammarIsIllegal(")缺少与之对应的(");
                }
                helper.pop();
                continue;
            }
            String top = String.valueOf(c);
            if (Expression.builder.containsKey(top)){
                while (!helper.isEmpty()&& Expression.priorityMap.containsKey(helper.peek()) && Expression.priorityMap.get(helper.peek())>= Expression.priorityMap.get(top)){
                    result.add(helper.pop());
                }
                helper.push(top);
                continue;
            }

            Function<Character,Boolean> isNumber = input->input>='0'&&input<='9';
            Function<Character,Boolean> isCustomCharacters = input->input=='_'||(input>='a' && input<='z')||input>='A' && input<='Z';

            top = Optional.ofNullable(getFullString(curExp,inx,isNumber)).orElse(getFullString(curExp,inx,isCustomCharacters));
            if (top==null||top.isEmpty()){
                throw new GrammarIsIllegal("other");
            }
            inx += top.length()-1;
            result.add(top);
        }
        while (!helper.isEmpty()){
            result.add(helper.pop());
        }
        return result;
    }

    private String getFullString(String curExp, int inx, Function<Character, Boolean> is) {
        char c = curExp.charAt(inx);
        if (!is.apply(c)){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (inx<curExp.length()&&is.apply(c)){
            sb.append(c);
            if (inx+1>=curExp.length()){
                break;
            }
            c = curExp.charAt(++inx);
        }
        return sb.toString();
    }
}
