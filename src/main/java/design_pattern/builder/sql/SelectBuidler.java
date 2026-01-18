package design_pattern.builder.sql;

import design_pattern.builder.sql.helper.Where;

public class SelectBuidler implements SelectStage,WhereStage,TableStage,EndStage {
    private String[] fields;
    private String tableName;
    private Where where;
    @Override
    public String build() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if(fields != null&&fields.length > 0) {
            sb.append(String.join(",",fields));

        }else{
            sb.append("*");
        }
        sb.append(" FROM ").append(tableName);
        if(where != null) {
            sb.append(where.build());
        }
        sb.append(";");

        return sb.toString();
    }

    @Override
    public TableStage select(String... fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public WhereStage From(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public EndStage where(Where where) {
        this.where = where;
        return this;
    }
}
