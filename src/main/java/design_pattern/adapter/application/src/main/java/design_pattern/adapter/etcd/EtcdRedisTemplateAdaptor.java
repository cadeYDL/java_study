package design_pattern.adapter.etcd;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class EtcdRedisTemplateAdaptor extends RedisTemplate<String,String> {

    private final EtcdValueOperations operations;
    public EtcdRedisTemplateAdaptor(EtcdValueOperations etcdValueOperations) {
        this.operations = etcdValueOperations;
    }

    @Override
    public ValueOperations<String, String> opsForValue() {
        return operations;
    }

    @Override
    public void afterPropertiesSet(){

    }
}
