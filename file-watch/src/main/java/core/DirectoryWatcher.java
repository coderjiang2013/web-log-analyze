package core;

import java.io.IOException;

/**
 * Created by CoderJiang on 2016/12/13.
 * 监听目录下文件的修改，可以用来监听日志文件
 */
public interface DirectoryWatcher {

    /**
     * 监听目录下文件的修改，不能监听子目录
     * @param first 目录名
     * @param more  目录名
     * @throws InterruptedException
     * @throws IOException
     */
    void watcher(String first, String... more) throws InterruptedException, IOException;

}
