package spring.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kv")
public class KVController {
    private final RedisTemplate<String,String> redisTemplate;

    public KVController(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @GetMapping("/get/{key}")
    public String get(@PathVariable("key") String key){
        return redisTemplate.opsForValue().get(key);
    }

    @PutMapping("/set/{key}/{value}")
    public String  set(@PathVariable("key")String key,@PathVariable("value")String value){
        redisTemplate.opsForValue().set(key,value);
        return "OK";
    }
}
