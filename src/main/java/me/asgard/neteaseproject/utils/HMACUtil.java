package me.asgard.neteaseproject.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACUtil {
    public static String HMACSHA256(String secretKey, String data) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = byteArrayToHexString(sha256_HMAC.doFinal(data.getBytes()));
            //System.out.println(hash);
        }
        catch (Exception e){
            System.out.println("Error");
        }
        return hash;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte item : b) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString().toLowerCase();
    }

}
