package language.string;

import util.Codec;

/**
 * @author LiuJie
 */
public class StringTest {
    public static void main(String[] args) {
        Codec codec = new Codec();
        System.out.println(codec.revert8(codec.hexToBin(codec.encodeUtf8("郭"))));
    }
}
