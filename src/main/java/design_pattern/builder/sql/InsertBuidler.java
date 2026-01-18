package design_pattern.builder.sql;

import design_pattern.builder.sql.helper.Values;

public class InsertBuidler implements InsertStage,ValuesSatge,EndStage {

    private String tableName;
    private Values vals;
    @Override
    public String build() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(tableName);
        sb.append(vals.build());
        sb.append(";");
        return sb.toString();
    }

    @Override
    public ValuesSatge Into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public EndStage value(Values vals) {
        this.vals = vals;
        return this;
    }
}
