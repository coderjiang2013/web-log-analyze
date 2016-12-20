package core.impl;

import core.DirectoryWatcher;
import core.event.EventCallback;
import entity.Event;
import entity.FileContentCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by coderjiang on 2016/12/13.
 * 日志文件目录监控的一个实现，该实现只能用于监控增量文件的修改（append），例如日志文件，不能正常监控内容文件的修改，默认使用UTF8编码格式解析文件。该实现不能监听子目录内的文件
 */
public class SimpleDirectoryAppendWatcher implements DirectoryWatcher {

    private static Logger logger = LoggerFactory.getLogger(SimpleDirectoryAppendWatcher.class);

    private Map<Path, WatchService> watchServiceMap;

    private FileContentCache fileContentCache;

    private List<Path> pathList = new ArrayList();

    private EventCallback eventCallback;

    private Charset charset;

    private String dirSymbol = System.getProperty("file.separator");

    public SimpleDirectoryAppendWatcher(EventCallback eventCallback){
        this.eventCallback = eventCallback;
    }

    public SimpleDirectoryAppendWatcher(EventCallback eventCallback, Charset charset){
        this.charset = charset;
        this.eventCallback = eventCallback;
    }

    public void watcher(String first, String... more) throws IOException {

        init(first, more);

        ExecutorService executorService = Executors.newFixedThreadPool(this.pathList.size());

        // start watch
        for (Map.Entry<Path, WatchService> entry : this.watchServiceMap.entrySet()) {
            executorService.execute(new Watcher(entry.getValue(), entry.getKey()));
        }

    }

    private synchronized void process(Path path, WatchEvent<?>  watchEvent){
        logger.debug("{} 发生了 {} 事件 {}\n", watchEvent.context(), watchEvent.kind(), path.toFile().getAbsolutePath());
        switch (watchEvent.kind().name()){
            case "ENTRY_CREATE":
                this.fileContentCache.add(new File(path.toFile().getAbsolutePath() + dirSymbol + watchEvent.context() ));
                break;
            case "ENTRY_DELETE":
                this.fileContentCache.delete(new File(path.toFile().getAbsolutePath() + dirSymbol + watchEvent.context() ));
                break;
            case "ENTRY_MODIFY":
                String modifyContent = this.fileContentCache.modify(new File(path.toFile().getAbsolutePath() + dirSymbol + watchEvent.context() ));
                this.eventCallback.call(new Event(modifyContent, path, watchEvent));
                break;
        }
    }

    private void init(String first, String[] more) throws IOException {

        initPathList(first, more);

        initWatchServiceMap();

        initFileContent();

        initPathWatchService();

    }

    private void initPathWatchService() throws IOException {
        for (Path path : this.pathList) {
            path.register(this.watchServiceMap.get(path),
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

        }
    }

    private void initWatchServiceMap() throws IOException {
        this.watchServiceMap = new HashMap<>(this.pathList.size());
        for (Path path : this.pathList) {
            this.watchServiceMap.put(path, FileSystems.getDefault().newWatchService());
        }
    }

    private void initFileContent() {
        if(null == this.charset) {
            this.fileContentCache = new FileContentCache();
        }else{
            this.fileContentCache = new FileContentCache(this.charset);
        }

        for (Path path : this.pathList) {
            this.fileContentCache.add(path);
        }

    }

    private void initPathList(String first, String[] more) {
        this.pathList.add(Paths.get(first));
        for (String str : more) {
            this.pathList.add(Paths.get(str));
        }
    }

    class Watcher implements Runnable {

        private WatchService watchService;
        private Path path;

        public Watcher(WatchService watchService, Path path) {
            this.watchService = watchService;
            this.path = path;
        }

        @Override
        public void run() {
            logger.debug("{} was started watching ", this.getClass().getName());
            while (true) {
                WatchKey watchKey = null;
                try {
                    watchKey = watchService.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    process(path, event);
                }
                if (!watchKey.reset()) {
                    break;
                }
            }
        }
    }

}
