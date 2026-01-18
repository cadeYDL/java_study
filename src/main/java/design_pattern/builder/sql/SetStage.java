package design_pattern.builder.sql;

import design_pattern.builder.sql.helper.SetValues;

public interface SetStage {
    WhereStage set(SetValues setValues);
}
