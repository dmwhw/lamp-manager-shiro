package com.gzseeing.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 加密算法类
 * @Description 
 * @author haowen
 * @version 1.0.0  2017年3月8日 下午4:07:15
 * @see      
 * @since     [管理平台] 
 */
public class EncryptUtils {
	
	/**
	 * 把字符串用MD5 salt加密
	 * @Description 
	 * @param str
	 * @param salt 字符串。
	 * @return
	 */
	public static String getMD5String(String str,String salt){
		StringBuilder result=new StringBuilder();
		try {
			MessageDigest digest= MessageDigest.getInstance("MD5");
			StringBuffer code=new StringBuffer(str);
			
			if (salt!=null&&!"".equals(salt)){
				code.append(salt);
			}
			
			byte []  buff=digest.digest(code.toString().getBytes());
			for(int i=0;i<buff.length;i++){			   
			   int num=buff[i]&0xFF;
			   String hexString=Integer.toHexString(num);
			   
			   if (hexString.length()==1){
					result.append("0").append(hexString);
				}else{
					result.append(hexString);
				}			
			}
			return result.toString();
		}  catch (Exception e) {
			e.printStackTrace();
		}		
		return null;		
	}
	
	
	

    private static final int KEY_LENGTH = 24 * 8;
    private static final int SALT_LENGTH = 12;
    private static final int ITERATIONS = 901;

    /**
     * This method creates a new PBKDF2 password (in mosquitto-auth-plug format)
     * from a plain password.
     *
     * @author Manuel Domínguez Dorado - manuel.dominguez@enzinatec.com
     * @param plainPassword The plain password used to generate the
     * corresponding PBKDF2 password (in mosquitto-auth-plug) format.
     * @return The generated PBKDF2 password in mosquitto-auth-plug format
     * (usually, it will be stored in a MySQL database).
     * @since 1.0
     */
    public static String createPBKDF2WithHmacSHA256Password(String plainPassword) {
        byte someBytes[] = new byte[SALT_LENGTH];
        Random randomGenerator = new Random();
        randomGenerator.nextBytes(someBytes);
        String encodedSalt = Base64.encode(someBytes);

        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(MosquittoPBKDF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        KeySpec ks = new PBEKeySpec(plainPassword.toCharArray(), encodedSalt.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKey s;
        try {
            s = f.generateSecret(ks);
            String encodedKey = Base64.encode (s.getEncoded());
            String hashedKey = "PBKDF2$sha256$" + ITERATIONS + "$" + encodedSalt + "$" + encodedKey;
            return hashedKey;
        } catch (InvalidKeySpecException ex) {
            //Logger.getLogger(MosquittoPBKDF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

	

	/**
	 * 获得UUID 10位盐值
	 * @Description 
	 * @return
	 */
	public static String getSalt(){
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0,10);
	}

	
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");

	}

	
	/**
	 * MD5增强双重加密
	 * @author haowen
	 * @Description
	 * @param password
	 * @param salt1
	 * @param salt2
	 * @return
	 */
	public static String getEnhanceMD5(String password,Integer id,String salt){
		String md5String1 = getMD5String(password, id+"");
		//用原来的md5 一半作为盐值
		return getMD5String(md5String1, md5String1.substring(0,md5String1.length()/2)+salt);
	}
	


}
