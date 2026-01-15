package design_pattern.strategy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class LevelController {
    private Map<LevelType,LevelService> levelServiceMap;

    @Autowired
    private DefaultLevelService defaultLevelService;

    @GetMapping(path = "/level")
    public String level(@RequestParam("year") int year,@RequestParam("base") int base){
        LevelType level = LevelType.getLevel(year);
        LevelService service = levelServiceMap.getOrDefault(level, defaultLevelService);
        Stage stage = service.getStage(base);
        String levelStr = "未知";
        if (level!=null){
            levelStr = level.toString();
        }
        return "工作年限:"+year+"年,当前base:"+base+"推测应该是"+levelStr+"靠"+stage+"的部分";
    }

    @Autowired
    public void setLevelServiceMap(List<LevelService> levelServiceList){
        this.levelServiceMap = levelServiceList.stream().
                filter(service->service.getClass()
                        .isAnnotationPresent(SupportLevelType.class)).collect(Collectors.toMap(this::findLevelFromService, Function.identity()));


        if (this.levelServiceMap.size()!=LevelType.values().length){
            throw new IllegalArgumentException("缺乏一些职级策略的实现");
        }
    }

    private LevelType findLevelFromService(LevelService levelService) {
        return levelService.getClass().getAnnotation(SupportLevelType.class).level();
    }
}
