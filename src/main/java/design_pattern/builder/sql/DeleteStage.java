package design_pattern.builder.sql;

public interface DeleteStage {
    WhereStage From(String tableName);
}
