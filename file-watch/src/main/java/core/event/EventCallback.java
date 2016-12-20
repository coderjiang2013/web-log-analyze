package core.event;

import entity.Event;

/**
 * Created by coderjiang on 2016/12/14.
 */

/**
 * 当监听目录文件修改后事件的回调接口，实现需要保证线程安全
 */
public interface EventCallback {

    void call(Event event);

}
