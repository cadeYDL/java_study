package design_pattern.builder.sql;

import design_pattern.builder.sql.helper.Where;

public class DeleteBuidler implements DeleteStage,EndStage,WhereStage {
    private String tableName;
    private Where where;
    @Override
    public WhereStage From(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE from ");
        sb.append(tableName);
        if(where != null) {
            sb.append(where.build());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public EndStage where(Where where) {
        this.where = where;
        return this;
    }
}
