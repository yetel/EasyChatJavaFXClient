package com.easychat.fx.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private static final String IV_STRING = "ABCD1234EFGH5678";
    private static final String charset = "UTF-8";

    public static String encrypt(String content, String key) {
        try {
            byte[] contentBytes = content.getBytes(charset);
            byte[] keyBytes = key.getBytes(charset);
            byte[] encryptedBytes = encrypt(contentBytes, keyBytes);
            return new BASE64Encoder().encode(encryptedBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String content, String key) {
        try {
            byte[] encryptedBytes = new BASE64Decoder().decodeBuffer(content);
            byte[] keyBytes = key.getBytes(charset);
            byte[] decryptedBytes = decrypt(encryptedBytes, keyBytes);
            return new String(decryptedBytes, charset);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] encrypt(byte[] contentBytes, byte[] keyBytes) throws Exception {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    private static byte[] decrypt(byte[] contentBytes, byte[] keyBytes) throws Exception {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }

    public static void main(String[] args) {
        String test = "EVPQoaWlzv5hym3B/XblqDCseSO+Quwy+AfT73q5DIFW9IGZNF/Xm40pAKeHdTgh";
        String key = "20191010164810ab";

        System.out.println(decrypt(test, key));
    }

}