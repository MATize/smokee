package at.mse.walchhofer.utilities.strings;

import java.util.List;

public class StringFormater {

    public static String listToString(List<String> stringList, String seperator) {
        String sep = "";
        String ret = "";
        for (String cur : stringList) {
            ret = ret + sep + cur;
            sep = seperator;
        }
        return ret;
    }
}
