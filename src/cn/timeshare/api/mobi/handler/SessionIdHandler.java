package cn.timeshare.api.mobi.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * 对于没有cookie的时候会传递url会带上sessionId导致action跳入404
 * 
 * @author L.cm
 * @date 2013-10-15 下午1:26:13 <url>/sign_in;jsessionid=7
 *       ba49c313a84295770fecbd01e86f116166sc5feg5yhzwis9zayzx492</url>
 */
public class SessionIdHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
	super.nextHandler.handle(target, request, response, isHandled);
    }

    /*@Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled){
	int place = target.indexOf(";");
	if (place != -1) {
	    target = target.substring(0, place);
	}
	nextHandler.handle(target, request, response, isHandled);
    }*/
    
}
