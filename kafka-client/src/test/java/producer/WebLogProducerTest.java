package producer;

import core.DirectoryWatcher;
import core.event.EventCallback;
import core.event.impl.KafkaEventCallbackImpl;
import core.impl.SimpleDirectoryAppendWatcher;
import org.junit.Test;
import producer.impl.WebLogProducerImpl;

import java.io.IOException;

/**
 * Created by coderJiang on 2016/12/20.
 */
public class WebLogProducerTest {


    @Test
    public void producerTest() throws IOException, InterruptedException {

        WebLogProduce produce = new WebLogProducerImpl("weblog", "192.168.3.161:9092");

        EventCallback eventCallback = new KafkaEventCallbackImpl(produce);

        DirectoryWatcher directoryWatcher = new SimpleDirectoryAppendWatcher(eventCallback);

        String path = "C:\\Users\\Administrator\\Desktop\\nginx-1.11.7\\logs";

        directoryWatcher.watcher(path);

        System.in.read();

    }

}
