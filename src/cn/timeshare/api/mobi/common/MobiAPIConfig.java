package cn.timeshare.api.mobi.common;

import cn.timeshare.api.mobi.controller.UserController;
import cn.timeshare.api.mobi.handler.SessionIdHandler;
import cn.timeshare.api.mobi.model.User;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.render.ViewType;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MobiAPIConfig extends JFinalConfig {
    private boolean isLocal = isDevMode();

    /**
     * 判断运行环境，设定环境参数
     * 
     * @param me
     * @return null
     */
    @Override
    public void configConstant(Constants me) {
	if (isLocal)
	    me.setDevMode(true);
	else
	    me.setDevMode(false);
	me.setViewType(ViewType.OTHER);
	me.setEncoding("UTF-8");
    }

    /**
     * 设定路径
     * 
     * @param me
     * @return null
     */
    @Override
    public void configRoute(Routes me) {
	me.add("/api/mobi/user", UserController.class);
    }

    /**
     * 根据不同环境连接数据库
     * 
     * @param me
     * @return null
     */
    @Override
    public void configPlugin(Plugins me) {
	String jdbcUrl, userID, pass;
	if (isLocal) {
	    jdbcUrl = Consts.DEV_JDBC_URL;
	    userID = Consts.DEV_USERNAME;
	    pass = Consts.DEV_PASSWORD;
	} else {
	    jdbcUrl = Consts.SAE_JDBC_URL;
	    userID = Consts.SAE_USERNAME;
	    pass = Consts.SAE_PASSWORD;
	}

	MysqlDataSource ds = new MysqlDataSource();
	ds.setUrl(jdbcUrl);
	ds.setUser(userID);
	ds.setPassword(pass);

	ActiveRecordPlugin arp = new ActiveRecordPlugin(ds);
	// 如果是在本地，对数据库进行访问时数据SQL语句
	if (isLocal) {
	    arp.setShowSql(true);
	}
	arp.addMapping("user", User.class);

	me.add(arp);
    }

    /**
     * 设定拦截规则
     * 
     * @param me
     * @return null
     */
    @Override
    public void configInterceptor(Interceptors me) {
    }

    /**
     * 设定访问管理规则
     * 
     * @param me
     * @return null
     */
    @Override
    public void configHandler(Handlers me) {
	me.add(new SessionIdHandler());
    }

    /**
     * 初始化常量
     */
    public void afterJFinalStart() {
    }

    private boolean isDevMode() {
	String osName = System.getProperty("os.name");
	return osName.indexOf("Windows") != -1;
    }

}
