package design_pattern.strategy;

import java.util.function.IntPredicate;

public enum LevelType {
    LV1(year->year<3,"lv1"),
    LV2(year->year>=1 && year<=7,"lv2"),
    LV3(year->year>=3 && year<=12,"lv3");

    private final IntPredicate support;
    private final String name;

    LevelType(IntPredicate support,String name) {
        this.support = support;
        this.name = name;
    }

    public  static LevelType getLevel(int year){
        for(LevelType levelType:values()){
            if(levelType.support.test(year)){
                return levelType;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return name;
    }
}
