package design_pattern.builder.sql;

public interface TableStage {
    WhereStage From(String tableName);
}
