package design_pattern.adapter.etcd;

import org.springframework.data.redis.core.RedisKeyValueAdapter;

public class VoidRedisKeyValueAdapter extends RedisKeyValueAdapter {
    @Override
    public void afterPropertiesSet() {

    }
}
