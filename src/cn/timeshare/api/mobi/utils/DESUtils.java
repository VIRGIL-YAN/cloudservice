package cn.timeshare.api.mobi.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DESUtils {

    // 指定DES加密解密所用的密钥
    private static Key key;

    /**
     * 加密key为空
     */
    public DESUtils() {
	setkey("www.gduf.edu.cn");
    }

    /**
     * 设置加密key
     * 
     * @param keyStr
     *            加密key值
     */
    public DESUtils(String keyStr) {
	setkey(keyStr);
    }

    /**
     * 设置加密的校验码
     * 
     * @Date:2012-10-16 下午04:25:13
     * @ClassDescription:
     */
    private void setkey(String keyStr) {
	try {
	    DESKeySpec objDesKeySpec = new DESKeySpec(keyStr.getBytes("UTF-8"));
	    SecretKeyFactory objKeyFactory = SecretKeyFactory.getInstance("DES");
	    key = objKeyFactory.generateSecret(objDesKeySpec);

	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    // 对字符串进行DES加密，返回BASE64编码的加密字符串
    public final String encryptString(String str) {

	byte[] bytes = str.getBytes();
	try {
	    Cipher cipher = Cipher.getInstance("DES");
	    cipher.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encryptStrBytes = cipher.doFinal(bytes);
	    return Base64.encodeBase64String(encryptStrBytes);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    // 对BASE64编码的加密字符串进行解密，返回解密后的字符串
    public final String decryptString(String str) {
	try {
	    byte[] bytes = Base64.decodeBase64(str);
	    Cipher cipher = Cipher.getInstance("DES");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    bytes = cipher.doFinal(bytes);
	    return new String(bytes);
	} catch (Exception e) {
	    throw new RuntimeException();
	}
    }
}
