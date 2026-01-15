package design_pattern.strategy;


import org.springframework.stereotype.Component;

@Component
@SupportLevelType(level=LevelType.LV3)
public class L3LevelService implements LevelService {
    @Override
    public Stage getStage(int base) {
        if (base <= 45000) {
            return Stage.Low;
        }
        if (base<=55000){
            return Stage.Mid;
        }
        return Stage.High;
    }
}
