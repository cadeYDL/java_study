package design_pattern.builder.sql;

public interface UpdateStage {
    SetStage from(String table);
}
