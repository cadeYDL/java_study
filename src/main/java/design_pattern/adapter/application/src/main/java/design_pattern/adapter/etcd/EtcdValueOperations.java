package design_pattern.adapter.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.kv.GetResponse;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class EtcdValueOperations implements ValueOperations<String, String> {
    private final Client client;

    public EtcdValueOperations(Client client) {
        this.client = client;
    }
    @Nullable
    @Override
    public String get(Object key) {
        ByteSequence byteSequenceKey = ByteSequence.from(key.toString(), StandardCharsets.UTF_8);
        GetResponse resp = null;
        try {
            resp = client.getKVClient().get(byteSequenceKey).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (resp.getCount()>0){
            return resp.getKvs().get(0).getValue().toString(StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public void set(String key, String value) {
        ByteSequence byteSequenceKey = ByteSequence.from(key, StandardCharsets.UTF_8);
        ByteSequence byteSequenceValue = ByteSequence.from(value, StandardCharsets.UTF_8);
        try {
            client.getKVClient().put(byteSequenceKey,byteSequenceValue).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit unit) {

    }

    @Nullable
    @Override
    public Boolean setIfAbsent(String key, String value) {
        return null;
    }

    @Nullable
    @Override
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        return null;
    }

    @Nullable
    @Override
    public Boolean setIfPresent(String key, String value) {
        return null;
    }

    @Nullable
    @Override
    public Boolean setIfPresent(String key, String value, long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public void multiSet(Map<? extends String, ? extends String> map) {

    }

    @Nullable
    @Override
    public Boolean multiSetIfAbsent(Map<? extends String, ? extends String> map) {
        return null;
    }



    @Nullable
    @Override
    public String getAndDelete(String key) {
        return "";
    }

    @Nullable
    @Override
    public String getAndExpire(String key, long timeout, TimeUnit unit) {
        return "";
    }

    @Nullable
    @Override
    public String getAndExpire(String key, Duration timeout) {
        return "";
    }

    @Nullable
    @Override
    public String getAndPersist(String key) {
        return "";
    }

    @Nullable
    @Override
    public String getAndSet(String key, String value) {
        return "";
    }

    @Nullable
    @Override
    public List<String> multiGet(Collection<String> keys) {
        return List.of();
    }

    @Nullable
    @Override
    public Long increment(String key) {
        return 0L;
    }

    @Nullable
    @Override
    public Long increment(String key, long delta) {
        return 0L;
    }

    @Nullable
    @Override
    public Double increment(String key, double delta) {
        return 0.0;
    }

    @Nullable
    @Override
    public Long decrement(String key) {
        return 0L;
    }

    @Nullable
    @Override
    public Long decrement(String key, long delta) {
        return 0L;
    }

    @Nullable
    @Override
    public Integer append(String key, String value) {
        return 0;
    }

    @Nullable
    @Override
    public String get(String key, long start, long end) {
        return "";
    }

    @Override
    public void set(String key, String value, long offset) {

    }

    @Nullable
    @Override
    public Long size(String key) {
        return 0L;
    }

    @Nullable
    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return null;
    }

    @Nullable
    @Override
    public Boolean getBit(String key, long offset) {
        return null;
    }

    @Nullable
    @Override
    public List<Long> bitField(String key, BitFieldSubCommands subCommands) {
        return List.of();
    }

    @Override
    public RedisOperations<String, String> getOperations() {
        return null;
    }
}
