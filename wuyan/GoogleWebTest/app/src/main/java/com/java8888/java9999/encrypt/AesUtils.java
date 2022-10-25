package com.java8888.java9999.encrypt;

import android.content.Context;

import com.java8888.java9999.R;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName: EncodeUtils
 * @Author: CaoLong
 * @CreateDate: 2021/10/20 12:58
 * @Description:
 */
public class AesUtils {
    /**
     * 算法/模式/填充
     **/
    private static final String CipherMode = "AES/CBC/PKCS5Padding";
    //  key
    private static String key =  "";
    //  偏移量
    private static String iv = "";

    private static String ALGO = "AES";

    /**
     * 加密
     *
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, Context context) {
        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            byte[] dataBytes = plainText.getBytes("UTF-8");
            key = context.getString(R.string.key);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), ALGO);
            iv = context.getString(R.string.iv);
            IvParameterSpec ivspec = new IvParameterSpec(initIV(iv));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(dataBytes);
            String EncStr = Base64Utils.encode(encrypted);
            return stringToHexString(EncStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     *
     * @param cipherText
     * @return
     */
    public static String decrypt(String cipherText,Context context) {
        try {
            byte[] encrypted1 = Base64Utils.decode(hexStringToString(cipherText));
            Cipher cipher = Cipher.getInstance(CipherMode);
            key = context.getString(R.string.key);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), ALGO);
            iv = context.getString(R.string.iv);
            IvParameterSpec ivspec = new IvParameterSpec(initIV(iv));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 初始向量的方法，全部为0
     * 这里的写法适合于其它算法，AES算法IV值一定是128位的(16字节)
     */
    private static byte[] initIV(String IVString) {
        try {
            return IVString.getBytes("UTF-8");
        } catch (Exception e) {
            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }

    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str.toUpperCase();
    }

    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        s = s.toLowerCase();
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
