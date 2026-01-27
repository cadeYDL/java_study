package design_pattern.adapter.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.kv.GetResponse;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EtcdApiDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Client client = Client.builder().endpoints("http://192.168.139.120:2379").build();
        ByteSequence key = ByteSequence.from("k", StandardCharsets.UTF_8);
        CompletableFuture<GetResponse> getResponseCompletableFuture = client.getKVClient().get(key);
        GetResponse getResponse = getResponseCompletableFuture.get();
        if (getResponse.getCount()>0){
            System.out.println(getResponse.getKvs().get(0).getValue().toString(StandardCharsets.UTF_8));
        }else {
            System.out.println("not found");
        }
        client.getKVClient().put(key, ByteSequence.from("v", StandardCharsets.UTF_8)).get();
getResponseCompletableFuture = client.getKVClient().get(key);
         getResponse = getResponseCompletableFuture.get();
        if (getResponse.getCount()>0){
            System.out.println(getResponse.getKvs().get(0).getValue().toString(StandardCharsets.UTF_8));
        }else {
            System.out.println("not found");
        }
    }
}
