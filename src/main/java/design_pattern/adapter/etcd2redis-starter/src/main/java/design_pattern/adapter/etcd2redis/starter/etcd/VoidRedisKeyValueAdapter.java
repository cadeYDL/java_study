package design_pattern.adapter.etcd2redis.starter.etcd;

import org.springframework.data.redis.core.RedisKeyValueAdapter;

public class VoidRedisKeyValueAdapter extends RedisKeyValueAdapter {
    @Override
    public void afterPropertiesSet() {

    }
}
