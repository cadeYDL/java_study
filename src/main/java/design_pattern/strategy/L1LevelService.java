package design_pattern.strategy;


import org.springframework.stereotype.Component;

@Component
@SupportLevelType(level = LevelType.LV1)
public class L1LevelService implements LevelService {
    @Override
    public Stage getStage(int base) {
        if(base<=30000){
            return Stage.Low;
        }
        if (base<=35000){
            return Stage.Mid;
        }
        return Stage.High;
    }
}
