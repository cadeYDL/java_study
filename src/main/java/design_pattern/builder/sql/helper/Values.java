package design_pattern.builder.sql.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Values<T> {
    private String[] fieldNames;
    private List<T> objs;

    private  Values(List<T> objs, String[] fieldNames) {
        this.fieldNames = fieldNames;
        this.objs = objs;
    }

    public static Builder builder(String... fieldName){
        return new Builder(fieldName);
    }

    private StringBuilder buildFieldNames(StringBuilder sb){
        if (fieldNames.length == 0){
            return sb;
        }
        sb.append("(");
        sb.append(String.join(",", fieldNames));
        sb.append(")");
        return sb;
    }
    public String build() throws Exception {
        if(objs.isEmpty()){
            return "";
        }

        Class<?> clazz = objs.get(0).getClass();
        StringBuilder sb =buildFieldNames( new StringBuilder());
        sb.append(" VALUES ");
        sb.append("((");
        List<String> val = new ArrayList<>();
        for(T obj : objs){
            List<String> values = new ArrayList<>();
            if(fieldNames.length==0){
                for(Field field:clazz.getDeclaredFields()){
                    field.setAccessible(true);
                    values.add(field.get(obj).toString());
                }
            }else{
                for(String name:fieldNames){
                    Field field = clazz.getDeclaredField(name);
                    field.setAccessible(true);
                    values.add(field.get(obj).toString());
                }
            }
            val.add(String.join(",", values));
        }
        sb.append(String.join("),(",val));
        sb.append("))");
        return sb.toString();
    }


    public static class Builder<T> {
        private List<T> objs = new ArrayList<>();
        private final String[] fieldNames;

        public Builder(String[] fieldName) {
            this.fieldNames = fieldName;
        }

        public Builder add(T obj) {
            objs.add(obj);
            return this;
        }

        public Values<T> build(){
            return new Values<>(objs,fieldNames);
        }
    }


}
