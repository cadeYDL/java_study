package design_pattern.strategy;

import org.springframework.stereotype.Component;


@Component
public class DefaultLevelService implements LevelService {
    @Override
    public Stage getStage(int base) {
        return Stage.Unknown;
    }
}
