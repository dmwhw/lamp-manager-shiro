package com.gzseeing.utils;

import javax.crypto.Cipher;  
import sun.misc.BASE64Decoder;  
import javax.crypto.KeyGenerator;  
import java.security.SecureRandom;
import javax.crypto.spec.SecretKeySpec;  
  
/** 
 * 编码工具类 
 * 实现aes加密、解密 
 */  
public class AESUtils {  

    /** 
     * 算法 
     */  
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";  

    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    private static String base64Encode(byte[] bytes){  
        return Base64.encode(bytes);  
    }  
  
    /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
    private static byte[] base64Decode(String base64Code) throws Exception{  
        return  base64Code==null||"".equals(base64Code.trim()) ? null : new BASE64Decoder().decodeBuffer(base64Code);  
    }  
  
      
    /** 
     * AES加密 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
        secureRandom.setSeed(encryptKey.getBytes()); 
        kgen.init(256, secureRandom);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));  
  
        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
  
  
    /** 
     * AES加密为base 64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    public static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
  
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
     public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
            secureRandom.setSeed(decryptKey.getBytes()); 
            kgen.init(256, secureRandom);  
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
            byte[] decryptBytes = cipher.doFinal(encryptBytes);   
            return new String(decryptBytes,"UTF-8");  
        }  
  
  
    /** 
     * 将base 64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return  encryptStr==null||"".equals(encryptStr.trim()) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }  
  
}  