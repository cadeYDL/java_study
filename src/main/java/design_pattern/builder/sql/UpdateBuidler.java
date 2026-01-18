package design_pattern.builder.sql;

import design_pattern.builder.sql.helper.SetValues;
import design_pattern.builder.sql.helper.Where;

import java.util.HashMap;
import java.util.Map;

public class UpdateBuidler  implements UpdateStage,SetStage,WhereStage,EndStage {
    private String tableName;
    private final Map<String,String> valueMap= new HashMap<>();
    private Where where;
    private SetValues setValues;
    UpdateBuidler(){}

    @Override
    public SetStage from(String table) {
        this.tableName = table;
        return this;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName);
        sb.append(setValues.build());
        sb.append(where.build());
        sb.append(";");
        return sb.toString();
    }

    @Override
    public WhereStage set(SetValues setValues) {
        this.setValues = setValues;
        return this;
    }

    @Override
    public EndStage where(Where where) {
        this.where = where;
        return this;
    }

}
