package cn.timeshare.api.mobi.controller;

import java.util.HashMap;
import java.util.Map;

import cn.timeshare.api.mobi.common.Consts;
import cn.timeshare.api.mobi.model.User;

import com.jfinal.core.Controller;
import com.sina.sae.cloudservice.Utils;

public class UserController extends Controller {

    public void login() {
	String username = getPara("username");
	String password = getPara("password");
	User user = User.dao.getUser(username, password);

	Map<String, Object> params = new HashMap<String, Object>();
	String msg = "登录成功";
	int error = 0;
	if (user == null) {
	    error = 1;
	    msg = "用户名不存在";
	} else {
	    setSessionAttr(user.getStr(User.USERNAME), Utils.calcSignature(user.toJson(), "timeshare"));
	    params.put(Consts.USER_SESSION, getSessionAttr(user.getStr(User.USERNAME)));
	}
	params.put("error", error);
	params.put("msg", msg);

	setAttrs(params);
	renderJson();
    }
}
