package producer;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

/**
 * Created by coderJiang on 2016/12/20.
 */
public interface WebLogProduce {

    Future<RecordMetadata> send(Object key, Object value);

    void close();

}
