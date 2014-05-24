package com.sina.sae.cloudservice.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.sina.sae.cloudservice.Utils;
import com.sina.sae.util.SaeUserInfo;

/**
 * Rest�ӿ� ��Ӧ���MySQL���񣬿�ֱ��ͨ���˽ӿ�������SAEӦ�ö�Ӧ�����ݿ�
 */
public class DBRest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(DBRest.class.getName());

    /**
     * ���ݿ������Ϣ
     */
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    // private static final String URL =
    // "jdbc:mysql://localhost:3306/app_yaoqingma?characterEncoding=gbk";
    private static final String URL = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_" + SaeUserInfo.getAppName();
    private static final String USERNAME = SaeUserInfo.getAccessKey();
    private static final String PASSWORD = SaeUserInfo.getSecretKey();

    /**
     * ����Query�ӿ� ִ��select��䣬��ѯ���ݿ�������
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String sql = request.getParameter("sql");// ��ѯ��sql
	PrintWriter writer = response.getWriter();
	if (sql != null) {
	    Connection con = null;
	    ResultSet rs = null;
	    Statement stmt = null;
	    try {
		Class.forName(DRIVER).newInstance();
		con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		while (rs.next()) {
		    map = new HashMap<String, String>();
		    // ����resultSet�Ĳ�ѯ�ֶ�
		    for (int i = 1; i <= columnCount; i++) {
			Object obj = rs.getObject(i);
			map.put(metaData.getColumnName(i), obj == null ? "" : obj.toString());
		    }
		    list.add(map);
		}
		String data = Utils.toData(JSONArray.fromObject(list).toString(), 0, "Success");
		writer.write(data);
		logger.debug("executeQuery SQL:" + sql + " |data:" + data);
	    } catch (Exception e) {
		logger.error("executeQuery SQL error:" + sql + e.getMessage());
		writer.write(Utils.toData(null, 1003, e.getMessage()));
	    } finally {
		try {// �رո�������
		    if (stmt != null)
			stmt.close();
		    if (rs != null)
			rs.close();
		    if (con != null)
			con.close();
		} catch (Exception e) {
		}
	    }
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// ���������Ϲ淶����ֵ
	}
	writer.close();
    }

    /**
     * ����ִ�� INSERT��UPDATE �� DELETE ����Լ� CREATE TABLE �� DROP TABLE
     * ����ֵ��һ��������ָʾ��Ӱ��������������¼�������
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String sql = request.getParameter("sql");// ��ѯ��sql
	PrintWriter writer = response.getWriter();
	if (sql != null) {
	    logger.debug("executeUpdate SQL:" + sql);
	    Connection con = null;
	    ResultSet rs = null;
	    Statement stmt = null;
	    try {
		Class.forName(DRIVER).newInstance();
		con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		stmt = con.createStatement();
		int rows = stmt.executeUpdate(sql);
		String data = Utils.toData("{\"rows\":" + rows + "}", 0, "Success");
		writer.write(data);
		logger.debug("executeUpdate SQL:" + sql + " |rows:" + rows);
	    } catch (Exception e) {
		writer.write(Utils.toData(null, 1003, e.getMessage()));
		e.printStackTrace();
	    } finally {
		try {// �رո�������
		    if (stmt != null)
			stmt.close();
		    if (rs != null)
			rs.close();
		    if (con != null)
			con.close();
		} catch (Exception e) {
		}
	    }
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// ���������Ϲ淶����ֵ
	}
	writer.close();
    }

}
