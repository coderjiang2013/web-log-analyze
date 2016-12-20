package entity;

import core.impl.SimpleDirectoryAppendWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by coderjiang on 16-12-13.
 */
public class FileContentCache {

    private Map<String, FileCache> fileContentCache = new HashMap();
    private static Logger logger = LoggerFactory.getLogger(SimpleDirectoryAppendWatcher.class);
    private Charset charset = Charset.forName("UTF-8");

    public FileContentCache(){
    }

    public FileContentCache(Charset charset){
        this.charset = charset;
    }

    public void add(File file) {
        if (file.exists()) {
            this.fileContentCache.put(file.getAbsolutePath(), new FileCache(file));
        } else {
            logger.warn("文件不存在 " + file.getAbsolutePath());
            throw new RuntimeException("文件不存在 " + file.getAbsolutePath());
        }
    }

    public void add(Path path) {
        for (File file : path.toFile().listFiles()) {
            if (file.exists() && file.isFile()) {
                this.add(file);
            }
        }
    }

    public int size() {
        return this.fileContentCache.size();
    }

    public void delete(File file) {
        this.fileContentCache.remove(file.getAbsolutePath());
    }

    public String modify(File file) {
        if(file.exists()){
            return getContentByFile(file);
        }else{
            logger.warn("文件不存在 " + file.getAbsolutePath());
            throw new RuntimeException("文件不存在 " + file.getAbsolutePath());
        }
    }

    private synchronized String getContentByFile(File file) {

        long startPos = this.fileContentCache.get(file.getAbsolutePath()).getLength();
        long endPos = file.length();
        this.fileContentCache.get(file.getAbsolutePath()).setLength(endPos);

        if(endPos < startPos){
            throw new RuntimeException("文件并非增加(append)，该工具不能对修改的内容进行监听:" + file.getAbsolutePath() + " startPos:" + startPos + " endPos:" + endPos);
        }

        return getContentByPos(file, startPos, endPos);
    }

    public String getContentByPos(File file, long startPos, long endPos) {
        String res = new String();
        FileChannel fileChannel = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r");
            fileChannel = randomAccessFile.getChannel();
            ByteBuffer buff = ByteBuffer.allocate(new Long(endPos - startPos).intValue());
            int bytesRead = fileChannel.read(buff, startPos);
            if(bytesRead > 0) {
                res = getStringFromBuff(buff, this.charset);
            }

        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                if(null != fileChannel) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn(e.getMessage());
            }
        }
        return res;
    }

    private String getStringFromBuff(ByteBuffer buff, Charset charset) throws CharacterCodingException {
        buff.flip();
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(buff.asReadOnlyBuffer());
        buff.clear();
        return charBuffer.toString();
    }


    class FileCache {

        private File file;
        private long length;

        public FileCache(File file) {
            this.file = file;
            this.length = file.length();
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }
    }


}
