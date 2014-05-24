package cn.timeshare.api.mobi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

/**
 * 对字符串的简单处理
 * 
 * @author L.cm
 * @date 2013-6-6 下午5:08:06
 */
public class StringsUtil {

    private static final char[] CHAR_NUM = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private static final char[] CHAR_STR = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private static final String SECRET = ConfigUtil.get("secret");

    /**
     * 截取文字safe 中文
     * 
     * @param @param string
     * @param @param length
     * @param @param more like `...`,`>>>`
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String subCn(String string, int length, String more) {
	if (StringUtils.isNotEmpty(string)) {
	    char[] chars = string.toCharArray();
	    if (chars.length > length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
		    sb.append(chars[i]);
		}
		sb.append(more);
		return sb.toString();
	    }
	}
	return string;
    }

    /**
     * 获取url里的path
     * 
     * @param @param queryString
     * @param @return 设定文件
     * @return List<String> 返回类型
     * @throws
     */
    public static List<String> comboList(String queryString) {
	if (queryString.indexOf("&") > 0) {
	    queryString = queryString.substring(0, queryString.indexOf("&"));
	}
	List<String> fileList = new ArrayList<String>();
	if (queryString.indexOf(",") > 0) {
	    String[] paths = queryString.split(",");
	    for (String path : paths) {
		if (StrKit.notBlank(path)) {
		    fileList.add(PathKit.getWebRootPath() + path);
		}
	    }
	} else {
	    fileList.add(PathKit.getWebRootPath() + queryString);
	}
	return fileList;
    }

    /**
     * 生成一个10位的tonken用于http cache
     * 
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getTonken() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < 10; i++) {
	    sb.append(CHAR_NUM[new Random().nextInt(9)]);
	}
	return sb.toString();
    }

    /**
     * 生成一个10位的tonken用于http cache
     * 
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String randomPwd(int count) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < count; i++) {
	    sb.append(CHAR_STR[new Random().nextInt(36)]);
	}
	return sb.toString();
    }

    /**
     * 加密密码
     * 
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String pwdEncrypt(String pwd) {
	return DigestUtils.md5Hex(DigestUtils.md5Hex(pwd + SECRET));
    }

    /**
     * 加密cookie
     * 
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String cookieEncrypt(String email, String pwd) {
	return new DESUtils(SECRET).encryptString(email + ":" + pwdEncrypt(pwd));
    }

    /**
     * 解密cookie
     * 
     * @param @param cookieString
     * @param @return 设定文件
     * @return String[] 返回类型
     * @throws
     */
    public static String[] cookieDecryption(String cookieString) {
	cookieString = new DESUtils(SECRET).decryptString(cookieString);
	if (cookieString.indexOf(":") != -1) {
	    return cookieString.split(":");
	} else {
	    return null;
	}
    }

    /**
     * 功能描述: 生成sql占位符 ?,?,?
     * 
     * @param @param size
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String sqlPlaceHolder(Integer size) {
	if (null == size || size < 1) {
	    return "";
	} else {
	    String[] paras = new String[size];
	    for (int i = 0; i < size; i++) {
		paras[i] = "?";
	    }
	    return StringUtils.join(paras, ",");
	}
    }

    /**
     * 计算文字长度-.-无中文问题
     * 
     * @param @param string
     * @param @return 设定文件
     * @return int 返回类型
     * @throws
     */
    public static int getLength(String string) {
	if (StrKit.isBlank(string)) {
	    return 0;
	} else {
	    char[] strChars = string.toCharArray();
	    return strChars.length;
	}
    }

    /**
     * 获取Token
     * 
     * @param valueToken
     * @param startString
     * @param endString
     * @param unStart
     * @return
     */
    public static String getValue(String valueToken, String startString, String endString, int unStart) {
	int start = valueToken.indexOf(startString);
	int end = valueToken.length();
	String tempStr = valueToken.substring(start + unStart, end);
	end = tempStr.indexOf(endString, unStart);
	if (end == -1)
	    end = tempStr.length();
	return tempStr.substring(0, end);
    }

    /**
     * 判断Token是否有效
     * 
     * @param valueToken
     * @param value
     * @return
     */
    public static boolean isValue(String valueToken, String value) {
	if (valueToken.indexOf(value) != -1) {
	    return true;
	} else
	    return false;
    }
}
