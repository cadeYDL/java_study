package design_pattern.builder.sql.helper;

import java.util.HashMap;
import java.util.Map;

public class SetValues{
    private final Map<String, String> values;
    private SetValues(Map<String, String> values){
        this.values = values;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private final Map<String, String> values = new HashMap<>();
        public Builder set(String key,String val){
            values.put(key,val);
            return this;
        }

        public SetValues build() {
            return new SetValues(this.values);
        }
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(" SET ");
        for(Map.Entry<String, String> entry : values.entrySet()){
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" ");
        return sb.toString();
    }
}
