package cn.timeshare.api.mobi.controller;

import java.util.HashMap;
import java.util.Map;

import cn.timeshare.api.mobi.common.Consts;
import cn.timeshare.api.mobi.model.User;
import cn.timeshare.api.mobi.utils.DESUtils;

import com.jfinal.core.Controller;

public class UserController extends Controller {

    private DESUtils desUtils = new DESUtils();
    
    public void login() {
	String username = getPara("username");
	String password = getPara("password");
	User user = User.dao.login(username, password);

	Map<String, Object> params = new HashMap<String, Object>();
	String msg = "";
	int error = 0;
	if (user == null) {
	    error = 1;
	    msg = "用户名不存在";
	}
	params.put("error", error);
	params.put("msg", msg);
	setSessionAttr(user.getStr(User.USERNAME), desUtils.encryptString(user.toJson()));
	params.put(Consts.USER_SESSION, getSessionAttr(user.getStr(User.USERNAME)));
	setAttrs(params);
	renderJson();
    }
}
