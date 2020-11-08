package util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LiuJie
 */
public class Codec {

    private Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    public String hexToBin(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String str = new BigInteger(s.substring(i, i + 1), 16).toString(2);
            int len = str.length();
            for (int j = 0; j + len < 4; j++) {
                str = "0" + str;
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public String revert8(String s) {
        if (s.length() % 8 != 0) {
            return s;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i += 8) {
                for (int j = 7; j >= 0; j--) {
                    sb.append(s.charAt(i + j));
                }
            }
            return sb.toString();
        }
    }

    /**
     * 汉字转为对应的 UTF-8 编码
     *
     * @param s	UTF-8 decode
     * @return UTF-8 code in hex
     */

    public String encodeUtf8(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                byte[] b = Character.toString(c).getBytes("utf-8");
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append(Integer.toHexString(k).toUpperCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * UTF-8 编码转换为对应的汉字
     *
     * @param s UTF-8 code in hex
     * @return UTF-8 decode
     */

    public String decodeUtf8(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        try {
            s = s.toUpperCase();
            int total = s.length() / 2;
            int pos = 0;
            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {
                int start = i * 2;
                buffer[i] = (byte) Integer.parseInt(
                        s.substring(start, start + 2), 16);
                pos++;
            }
            return new String(buffer, 0, pos, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 汉字转为对应的 Unicode 编码
     *
     * @param s	Unicode decode
     * @return Unicode code in hex
     */

    public String encodeUnicode(String s) {
        try {
            StringBuilder sb = new StringBuilder();
            byte[] bytes = s.getBytes("unicode");
            for (int i = 0; i < bytes.length - 1; i += 2) {
                sb.append("\\u");
                String str1 = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str1.length(); j < 2; j++) {
                    sb.append("0");
                }
                String str2 = Integer.toHexString(bytes[i] & 0xff);
                sb.append(str2);
                sb.append(str1);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Unicode 编码转换为对应的汉字
     *
     * @param s Unicode code in hex
     * @return Unicode decode
     */

    public  String decodeUnicode(String s) {
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String group = matcher.group(2);
            char ch = (char) Integer.parseInt(group, 16);
            String group1 = matcher.group(1);
            s = s.replace(group1, ch + "");
        }
        return s;
    }
}
