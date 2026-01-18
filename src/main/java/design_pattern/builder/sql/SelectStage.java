package design_pattern.builder.sql;

public interface SelectStage {
    TableStage select(String... feilds);
}
