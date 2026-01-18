package design_pattern.builder.sql;

import design_pattern.builder.sql.helper.Where;

public interface WhereStage {
    EndStage where(Where where);
}
