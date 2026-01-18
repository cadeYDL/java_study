package design_pattern.builder.sql.helper;

import java.util.ArrayList;
import java.util.List;

public class Where {

    private final List<String> whereList ;
    private Where(List<String> where) {
        whereList =where;
    }
    public static WhereBuilder builder() {
        return new WhereBuilder();
    }
    public static class WhereBuilder{
        private List<String> whereList = new ArrayList<>();
        public WhereBuilder where(String where) {
            whereList.add(where);
            return this;
        }

        public Where build() {
            return new Where(whereList);
        }
    }

    public String build() {
        if (whereList.size()==0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ");
        for (String where:whereList){
            sb.append("(");
            sb.append(where);
            sb.append(")");
            sb.append(" AND ");
        }
        sb.delete(sb.length()-4,sb.length());
        return sb.toString();
    }
}
