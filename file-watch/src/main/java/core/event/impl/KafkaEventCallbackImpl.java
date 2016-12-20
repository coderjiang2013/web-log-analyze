package core.event.impl;

import core.event.EventCallback;
import entity.Event;
import producer.WebLogProduce;

/**
 * Created by Administrator on 2016/12/20.
 */
public class KafkaEventCallbackImpl implements EventCallback {

    private String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    private WebLogProduce producer;

    public KafkaEventCallbackImpl(WebLogProduce producer) {
        this.producer = producer;
    }

    @Override
    public void call(Event event) {
        synchronized (this) {

            String[] appendStr = event.getAppendContent().split(this.lineSeparator);

            for(String line : appendStr) {
                this.producer.send(null, line);
            }

        }
    }
}
