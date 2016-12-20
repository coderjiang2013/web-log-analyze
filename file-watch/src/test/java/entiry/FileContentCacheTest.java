package entiry;

import entity.FileContentCache;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/15.
 */
public class FileContentCacheTest {

    @Test
    public void testGetContentByPos() throws IOException, InterruptedException {

        FileContentCache fileContentCache = new FileContentCache();

        String res = fileContentCache.getContentByPos(new File("C:\\Users\\Administrator\\Desktop\\www\\demo.txt"), 0, 149);
        System.out.println(res.length());
        System.out.println(res);
    }

}
