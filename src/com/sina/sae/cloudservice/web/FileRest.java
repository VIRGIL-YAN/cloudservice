package com.sina.sae.cloudservice.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.sina.sae.cloudservice.Utils;
import com.sina.sae.storage.SaeStorage;

/**
 * Rest�ӿڣ�File�� ��Ӧsae��˵�Storage���� ���ڴ洢�ļ�
 */
public class FileRest extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(FileRest.class.getName());

    // �ļ��洢��storageʹ�õ�domain
    public static final String DOMAIN = "cloud";

    /**
     * ����get��list�����ӿ�(ͨ���������ж�ʹ���ĸ��ӿ�) get�ӿڲ���=>path list�ӿڲ���=>count
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String path = request.getParameter("path");// �ļ�·��
	String prefix = request.getParameter("prefix");// �ļ�ǰ׺
	String count = request.getParameter("count");// list����
	String skip = request.getParameter("skip");// list��ʼ��
	PrintWriter writer = response.getWriter();
	SaeStorage storage = new SaeStorage();
	if (null != path) {// ��ȡ�����ļ���Ϣ
	    boolean exists = storage.fileExists(DOMAIN, path);
	    String data;
	    if (exists) {// �ļ�����
		String url = storage.getUrl(DOMAIN, path);
		data = Utils.toData("{\"filepath\":\"" + path + "\",\"url\":\"" + url + "\"}", 0, "Success");
	    } else {// �ļ�������
		data = Utils.toData(null, 0, "file not exists");
	    }
	    logger.debug("getFile:" + data);
	    writer.write(data);
	} else if (null != count && Utils.checkInteger(count) && (skip == null || (skip != null && Utils.checkInteger(skip)))) {// list�ļ���Ϣ
	    int start = 0;
	    if (Utils.checkInteger(skip))
		start = Integer.parseInt(skip);
	    if (null == prefix)
		prefix = "";
	    List<String> files = storage.getList(DOMAIN, prefix, Integer.parseInt(count), start);
	    String data = Utils.toData(JSONArray.fromObject(Utils.changeToMapList(files, DOMAIN)).toString(), storage.getErrno(), storage.getErrno() == 0 ? "success" : storage.getErrmsg());
	    ;
	    logger.debug("listFile count:" + count + "|prefix:" + prefix + "|skip:" + skip + "|data:" + data);
	    writer.write(data);
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// ���������Ϲ淶����ֵ
	}
	writer.close();
    }

    /**
     * ����save�ӿ� �����ļ����ƶ� Ϊ�˴���ɶ��� ����ʹ��commons-fileupload �ϴ��ļ�
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	PrintWriter writer = response.getWriter();
	String contentType = request.getContentType();
	if (ServletFileUpload.isMultipartContent(request)) {// ������������Ƿ�Ϊmultipart������
	    try {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);// �õ����е��ļ�
		if (items != null && items.size() > 0) {
		    FileItem item = items.get(0);// �������һ��
		    SaeStorage storage = new SaeStorage();
		    // ע������ʹ�õ���getFieldName �������ļ�����ʵ���ƿ��ǵ��ļ����п��ܰ���·��
		    boolean flag = storage.write(DOMAIN, item.getFieldName(), item.get());
		    logger.debug("saveFile filename:" + item.getFieldName() + "|flag:" + flag);
		    writer.write(Utils.toData(null, storage.getErrno(), storage.getErrno() == 0 ? "success" : storage.getErrmsg()));
		}
	    } catch (FileUploadException e) {
		logger.debug("saveFile field:" + e.getMessage());
		writer.write(Utils.toData(null, 1002, "parse Request file Field" + e.getMessage()));
	    }
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter") + contentType);// ���������Ϲ淶����ֵ
	}
	writer.close();
    }

    /**
     * ����delete�ӿ� ɾ���ƶ��ļ�
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String path = request.getParameter("path");// list��ʼ��
	PrintWriter writer = response.getWriter();
	if (null != path) {// ��ȡ�����ļ���Ϣ
	    SaeStorage storage = new SaeStorage();
	    boolean flag = storage.delete(DOMAIN, path);
	    logger.debug("deleteFile:" + flag);
	    if (flag)
		writer.write(Utils.toData(null, 0, "Success"));
	    else
		writer.write(Utils.toData(null, storage.getErrno(), storage.getErrno() == 0 ? "success" : storage.getErrmsg()));
	} else {
	    writer.write(Utils.toData(null, 1002, "bad parameter"));// ���������Ϲ淶����ֵ
	}
	writer.close();
    }

}
