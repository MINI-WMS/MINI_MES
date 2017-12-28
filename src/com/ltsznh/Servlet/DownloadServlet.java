package com.ltsznh.Servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ltsznh.util.ServerLog;
import com.ltsznh.util.parameters;
import com.ltsznh.gwt.shared.param.PARAM;

//import sun.nio.ch.FileChannelImpl;

/**
 * 下载文件，不影响任何文件
 * 
 * @author 涛
 *
 */
public class DownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public DownloadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
	}

	/**
	 * see HttpServlet#init(ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		download(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		download(request, response);
	}

	private void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 解决中文乱码，
		response.setCharacterEncoding(PARAM.ENCODING);
		String fileName = URLDecoder.decode(request.getParameter("fileName"), PARAM.ENCODING);

		String filePath = parameters.dataFilePath + File.separator + parameters.appName + File.separator + "file"
				+ File.separator;

		filePath = (filePath + fileName).replace("..", "");
		File file = new File(filePath);
		if (!file.exists()) {
			ServerLog.log("Servlet", "下载文件", "查无此文件" + filePath);
			response.getWriter().write("err,no file:" + file.getName());
			return;
		}
		ServerLog.log("Servlet", "下载文件:" + file.getName(), file.getAbsolutePath());

		FileChannel fc = null;
		RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
		fc = randomAccessFile.getChannel();
		MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0L, fc.size()).load();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ServerLog.err("Download Servlet", e);
		}
		byte[] bos = new byte[(int) fc.size()];
		if (byteBuffer.remaining() > 0) {
			byteBuffer.get(bos, 0, byteBuffer.remaining());
		}
		// 解决下载文件名中文乱码问题
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(file.getName(), PARAM.ENCODING) + "\"");
		// response.setHeader("Content-Disposition",
		// "attachment; filename=""+URLEncoder.encode(fname,"utf-8") +""");

		response.setContentLength(bos.length);
		response.getOutputStream().write(bos);

//		try {// 释放文件
//			Method method = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
//			method.setAccessible(true);
//			method.invoke(FileChannelImpl.class, byteBuffer);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			bos = null;
//			byteBuffer.clear();
//			byteBuffer = null;
//			fc.close();
//			randomAccessFile.close();
//		}
	}
}
