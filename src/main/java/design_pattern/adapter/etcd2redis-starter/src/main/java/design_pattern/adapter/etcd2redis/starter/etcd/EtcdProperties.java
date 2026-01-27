package design_pattern.adapter.etcd2redis.starter.etcd;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "etcd-demo")
@Data
public class EtcdProperties {
    private String url;
}
