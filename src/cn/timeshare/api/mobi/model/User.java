package cn.timeshare.api.mobi.model;

import java.io.Serializable;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User> implements Serializable {

    // 数据库表名
    public static final String TABLE_NAME = "user";

    private static final long serialVersionUID = -1786098573949757618L;

    public static final User dao = new User();

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public User getUser(String username, String password) {
	return dao.findFirst("select * from user where username = ? and password = ? limit 1", username, password);
    }

    public Boolean canVisit(String actionKey) {
	return true;
    }

}
