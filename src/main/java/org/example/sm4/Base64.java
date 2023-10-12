package org.example.sm4;

public class Base64 {
    public static byte[] decode(String str,int ignore){
        return java.util.Base64.getDecoder().decode(str);
    }
    public static String encodeToString(byte[] bytes, int ignore) {
        return java.util.Base64.getEncoder().encodeToString(bytes).replaceAll("\n","");
    }
}
