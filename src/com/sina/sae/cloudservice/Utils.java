package com.sina.sae.cloudservice;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sina.sae.util.SaeUserInfo;

/**
 * ������
 */
public class Utils {

    public static String debugToken;

    static {
	changeDebugToken();
    }

    /**
     * ��������debugToken
     * 
     * @return
     */
    public static String changeDebugToken() {
	String token = "debug_token_" + System.currentTimeMillis();
	debugToken = md5(token);
	return debugToken;
    }

    /**
     * ��������debugToken
     * 
     * @return
     */
    public static void disabledDebugToken() {
	debugToken = "";
    }

    /**
     * ����Ҫ���ص��������json��ʽ
     * ����{"code":4000,"message":"success","data":{"className":"user"
     * ,"name":"lizy","age":"23","sex":"man"}}
     * 
     * @param data
     *            ����
     * @param code
     *            ������
     * @param message
     *            ������Ϣ
     * @return
     */
    public static String toData(String data, int code, String message) {
	StringBuilder sb = new StringBuilder();
	sb.append("{\"code\":").append(code).append(",\"message\":\"").append(message).append("\"");
	if (data != null) {
	    sb.append(",\"data\":").append(data);
	}
	sb.append("}");
	return sb.toString();
    }

    /**
     * ��֤�Ƿ�Ϊ����
     * 
     * @param str
     * @return
     */
    public static boolean checkInteger(String str) {
	try {
	    if (null == str)
		return false;
	    Integer.parseInt(str);
	} catch (Exception ex) {
	    return false;
	}
	return true;
    }

    /**
     * ��Map<String,Object>ת��ΪMap<String,String> ����ObjectΪbyte[]
     * ��Ҫ����kvdb��list�ӿڳ�����Map ��Ϊkvdb�д洢����byte[]����String
     * 
     * @param map
     *            ��Ҫת����map
     * @return
     */
    public static Map<String, String> changeToStringMap(Map<String, Object> map) {
	Map<String, String> retMap = new HashMap<String, String>();
	if (null != map) {
	    Set<String> keys = map.keySet();
	    for (String key : keys) {
		retMap.put(key, new String((byte[]) map.get(key)));
	    }
	}
	return retMap;
    }

    /**
     * ��List<String>ת��ΪList<Map> ��Ϊstorage��list�ӿڷ��ص����ļ��б���������ƿװһ���ٷ���
     * 
     * @param list
     *            ת����list
     * @param domain
     *            ������
     * @return
     */
    public static List<Map<String, String>> changeToMapList(List<String> list, String domain) {
	List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
	Map<String, String> map;
	for (String path : list) {
	    map = new HashMap<String, String>();
	    map.put("filepath", path);
	    map.put("url", "http://" + SaeUserInfo.getAppName() + "-" + domain + ".stor.sinaapp.com/" + path);
	    retList.add(map);
	}
	return retList;
    }

    /**
     * ���ַ�������md5����
     * 
     * @param password
     *            �����ַ���
     * @return ���ؼ��ܽ��
     */
    public static String md5(String password) {
	try {
	    MessageDigest md = MessageDigest.getInstance("md5");
	    md.update(password.getBytes());
	    return new BigInteger(1, md.digest()).toString(16);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * ��ָ�������ݽ��м��ܣ�����HmacSHA256��ʽ���ܣ����ܺ��ٶ����ݽ���Base64����
     * 
     * @param cryptoType
     *            ���ܵ�����
     * @param content
     *            ��Ҫ���ܵ�����
     * @param secretKey
     *            ���������Key
     * @return ���ܺ������
     */
    public static String calcSignature(String content, String secretKey) {
	try {
	    Mac mac = Mac.getInstance("HmacSHA256");
	    SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
	    mac.init(secret);
	    byte[] digest = mac.doFinal(content.getBytes());
	    sun.misc.BASE64Encoder encode = new sun.misc.BASE64Encoder();
	    return encode.encode(digest);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
