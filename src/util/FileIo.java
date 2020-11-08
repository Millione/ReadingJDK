package util;

import java.io.*;

/**
 * @author LiuJie
 */
public class FileIo {

    private static String inPath = "C:/Users/LiuJie/Desktop/1.amr";
    private static String outPath = "C:/Users/LiuJie/Desktop/2.txt";

    public static void main(String[] args) {
        Codec codec = new Codec();
        File inFile = new File(inPath);
        File outFile = new File(outPath);
        if (inFile.isFile()) {
            FileInputStream fis = null;
            Writer writer = null;
            try {
                fis = new FileInputStream(inFile);
                writer = new FileWriter(outFile);
                byte[] bytes = new byte[1048];
                StringBuilder sb = new StringBuilder();
                int len = 0;
                while ((len = fis.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, len, "utf-8"));
                }
                for (int i = 0; i < sb.length(); i++) {
                    String s = codec.hexToBin(codec.encodeUtf8(sb.substring(i, i + 1)));
                    for (int j = s.length() - 1; j >= 0; j--) {
                        writer.write(s.substring(j, j + 1));
                        writer.write("\r\n");
                    }
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist!");
        }
    }
}


