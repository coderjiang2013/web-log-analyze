package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderJiang on 2016/12/20.
 */
public class StringContentUtils {

    /**
     * 按分割符拆分字符串，并且不会拆分特殊字符中的文字，特殊字符包括 ' " []
     * @param line 待拆分内容
     * @param infoSeparator 分隔符
     * @return
     */
    public static String[]  getSplit(String line, String infoSeparator) {

        List<String> result = new ArrayList<>();

        boolean inSingleQuotes = false;     // '
        boolean inDoubleQuotes = false;     // "
        boolean inBracketQuotes = false;    // []

        int lastPos = 0;

        char[] match = new char[infoSeparator.toCharArray().length];


        for (int i = 0; i < line.toCharArray().length; i++) {

            if (line.charAt(i) == '\'' && inSingleQuotes) {
                inSingleQuotes = false;
            } else if (line.charAt(i) == '\'' && !inSingleQuotes) {
                inSingleQuotes = true;
            }

            if (line.charAt(i) == '\"' && inDoubleQuotes) {
                inDoubleQuotes = false;
            } else if (line.charAt(i) == '\"' && !inDoubleQuotes) {
                inDoubleQuotes = true;
            }

            if (line.charAt(i) == ']' && inBracketQuotes) {
                inBracketQuotes = false;
            } else if (line.charAt(i) == '[' && !inBracketQuotes) {
                inBracketQuotes = true;
            }


            line.getChars(i, i + match.length, match, 0);
            if (!inSingleQuotes && !inDoubleQuotes &&  !inBracketQuotes && infoSeparator.equals(new String(match))) {
                //result.add(line.substring(lastPos + infoSeparator.length(), i));
                result.add(line.substring(lastPos, i));
                lastPos = i;
            } else if (i == line.toCharArray().length - 1) {
                result.add(line.substring(lastPos + infoSeparator.length(), i + 1));
            }

        }

        return result.toArray(new String[result.size()]);
    }

}
