package com.sina.sae.cloudservice.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sina.sae.cloudservice.Utils;
import com.sina.sae.kvdb.SaeKV;
import com.sina.sae.kvdb.SaeKVUtil;

/**
 * Rest�ӿڣ�Object�� ��Ӧsae��˵�kvdb���� �洢����Ϊ�����json��ʽ
 */
public class ObjectRest extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(ObjectRest.class.getName());

    /**
     * ����get��list�����ӿ�(ͨ���������ж�ʹ���ĸ��ӿ�) get�ӿڲ���=>key
     * count�ӿڲ���=>prefix��count��compare
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String key = request.getParameter("key");// ���ҵ�key
	String prefix = request.getParameter("prefix");// ǰ׺
	String count = request.getParameter("count");// ȡֵ����
	String compare = request.getParameter("compare");// �Ƚϵ�key
	PrintWriter writer = response.getWriter();
	SaeKV kv = new SaeKV();
	kv.init();
	if (key != null) {// get�ӿ�
	    String value = null;
	    byte[] bs = kv.get(key);
	    if (bs != null) {
		value = SaeKVUtil.byteToString(bs);
	    }
	    writer.write(Utils.toData(value, kv.getErrCode(), kv.getErrMsg()));
	    logger.debug("getObject key:" + key + " |value:" + value);
	} else if (count != null && Utils.checkInteger(count)) {// list�ӿ�
	    if (null == prefix)
		prefix = "";
	    Map<String, Object> map = kv.pkrget(prefix, Integer.parseInt(count), compare);
	    // ��Ϊ�洢��kvdb�еĶ���byte[]��Ҫ����ת��ΪString
	    String data = JSONObject.fromObject(Utils.changeToStringMap(map)).toString();
	    writer.write(Utils.toData(data, kv.getErrCode(), kv.getErrMsg()));
	    logger.debug("listObject count:" + count + " |prefix:" + prefix + "|compare:" + compare + "|data:" + data);
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// ���������Ϲ淶����ֵ
	}
	writer.close();
    }

    /**
     * ����save�ӿ� �����������ƶ�
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	PrintWriter writer = response.getWriter();
	String key = request.getParameter("key");// �����key
	String data = request.getParameter("data");// �����value
	if (key != null && data != null) {
	    SaeKV kv = new SaeKV();
	    kv.init();
	    kv.set(key, SaeKVUtil.StringToByte(data));
	    writer.write(Utils.toData(null, kv.getErrCode(), kv.getErrMsg()));
	    logger.debug("saveObject key:" + key + " |data:" + data);
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// �������󷵻�
	}
	writer.close();
    }

    /**
     * ����delete�ӿ� ɾ���ƶ�����
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	PrintWriter writer = response.getWriter();
	String key = request.getParameter("key");// ��Ҫɾ����key
	if (key != null) {
	    SaeKV kv = new SaeKV();
	    kv.init();
	    kv.delete(key);
	    writer.write(Utils.toData(null, kv.getErrCode(), kv.getErrMsg()));
	    logger.debug("deleteObject key:" + key + " |code:" + kv.getErrCode());
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// �������󷵻�
	}
	writer.close();
    }

}
