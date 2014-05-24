package cn.timeshare.api.mobi.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 
 * @author VIJAY
 * 
 */
public class ConfigUtil {

    private static final HashMap<String, String> config = new HashMap<String, String>();

    /**
     * 读取配置文件，将配置文件分配到一个静态HashMap
     * 
     * @param properties
     * @return
     */
    public static void loadConfig(Properties properties) {
	for (Entry<Object, Object> entry : properties.entrySet()) {
	    config.put(entry.getKey().toString(), entry.getValue().toString());
	}
    }

    public static final String get(String key) {
	return config.get(key);
    }

    public static Boolean getToBoolean(String key) {
	return Boolean.parseBoolean(config.get(key));
    }

    public static Long getToLong(String key) {
	return Long.parseLong(config.get(key));
    }
}
