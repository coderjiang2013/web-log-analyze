package core.event.impl;

import core.event.EventCallback;
import entity.Event;

/**
 * Created by coderJiang on 2016/12/14.
 * 用户输出测试的一个日志回调实现
 */
public class ConsoleEventCallbackImpl implements EventCallback {
    @Override
    public void call(Event event) {
        System.out.printf("File append info %s", event.getAppendContent());
    }
}
