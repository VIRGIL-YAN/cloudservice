package cn.timeshare.api.mobi.common;

import com.sina.sae.util.SaeUserInfo;

import cn.timeshare.api.mobi.utils.ConfigUtil;

/**
 * Created with IntelliJ IDEA. Author: iver Date: 13-3-28
 */
public class Consts {
    // 缓存时间 30分钟
    public static final long CACHE_TIME_MINI = 1800000L;
    // 缓存时间 1小时
    public static final long CACHE_TIME_MAX = 3600000L;
    // Http缓存时间 一个月
    public static final long HTTP_CACHE_TIME = 2592000L;
    // 用户session key
    public static final String USER_SESSION = "token";

    // 本地开发环境上 data source 的配置
    public static final String DEV_JDBC_URL = "jdbc:mysql://localhost:3306/cloudservice";
    public static final String DEV_USERNAME = "root";
    public static final String DEV_PASSWORD = "root";
    // 云环境上 data source 的配置
    public static final String SAE_JDBC_URL = "jdbc:mysql://r.rdc.sae.sina.com.cn:3307/"+SaeUserInfo.getAppName();
    public static final String SAE_USERNAME = SaeUserInfo.getAccessKey();
    public static final String SAE_PASSWORD = SaeUserInfo.getSecretKey();

    public static String DOMAIN_COOKIE = ConfigUtil.get("domain");
    public static String DOMAIN_NAME = ConfigUtil.get("domain.name");

    // ajax 状态
    public static final String AJAX_STATUS = "status";
    public static final int AJAX_Y = 0; // OK
    public static final int AJAX_N = 1; // no
    public static final int AJAX_S = 2; // 权限
    public static final int AJAX_O = 3; // other 其他错误

    // valid
    public static final int token = 1;
}
