package event;

import org.junit.Test;

/**
 * Created by Administrator on 2016/12/19.
 */
public class WebLogEvenetCallbackTest {

    @Test
    public void testGetSplit(){
        WebLogEventCallbackImpl webLogEventCallbackImpl = new WebLogEventCallbackImpl((event, appendContent) -> {

        });

        String line = "127.0.0.1 - - [19/Dec/2016:16:29:33 +0800] \"GET /dist/www/js/blog.min.js?t=1481510681000 HTTP/1.1\" 404 1520 \"http://localhost/\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36\" \"-\" 0.098 0.098 -";
        String res[] = webLogEventCallbackImpl.getSplit(line, " ");
        for(String s : res){
            System.out.printf(s + ",");
        }
    }

}
