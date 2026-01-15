package design_pattern.strategy;


import org.springframework.stereotype.Component;

@Component
@SupportLevelType(level = LevelType.LV2)
public class L2LevelService implements LevelService {
    @Override
    public Stage getStage(int base) {
        if(base<=35000){
            return Stage.Low;
        }
        if(base<=45000){
            return Stage.Mid;
        }
        return Stage.High;
    }
}
