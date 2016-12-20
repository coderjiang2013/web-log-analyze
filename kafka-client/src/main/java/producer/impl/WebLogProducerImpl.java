package producer.impl;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import producer.WebLogProduce;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by coderJiang on 2016/12/20.
 */
public class WebLogProducerImpl implements WebLogProduce {

    private final String TOPIC;
    private final String BROKER_LIST;
    private Properties props = new Properties();

    private Producer kafkaProducer;


    public WebLogProducerImpl(String topic, String brokerList){
        this.TOPIC = topic;
        this.BROKER_LIST = brokerList;
        props.put("bootstrap.servers", brokerList);
        this.start();
    }

    private void start(){

        this.props.put("acks", "all");
        this.props.put("retries", 0);
        this.props.put("batch.size", 16384);
        this.props.put("linger.ms", 1);
        this.props.put("buffer.memory", 33554432);
        this.props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.kafkaProducer = new KafkaProducer(this.props);
    }


    @Override
    public Future<RecordMetadata> send(Object key, Object value) {
        return this.kafkaProducer.send(new ProducerRecord(this.TOPIC, key, value));
    }

    @Override
    public void close() {
        this.kafkaProducer.close();
    }
}
