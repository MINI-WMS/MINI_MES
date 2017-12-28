package com.ltsznh.Servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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

//import sun.nio.ch.FileChannelImpl;

import com.ltsznh.util.ServerLog;
import com.ltsznh.util.parameters;
import com.ltsznh.gwt.shared.param.PARAM;

/**
 * 下载Excel，下载后删除生成的excel
 * 
 * @author 涛
 *
 */
public class DownloadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public DownloadFileServlet() {
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
		// String fileName = request.getParameter("fileName");

		String fileName = URLDecoder.decode(request.getParameter("fileName"), PARAM.ENCODING);
		ServerLog.log("download servlet", "", fileName + request.getParameter("fileName"));

		String filePath = parameters.dataFilePath + File.separator + parameters.appName + File.separator + "file"
				+ File.separator + "excel" + File.separator;
		filePath = (filePath + fileName).replace("..", "");
		File file = new File(filePath);// 删除路径下同名的Excel 文件
		if (!file.exists()) {
			response.getWriter().write("err,no file:" + fileName);
			return;
		}

		FileChannel fc = null;
		RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
		fc = randomAccessFile.getChannel();
		MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0L, fc.size()).load();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(byteBuffer.isLoaded());
		byte[] bos = new byte[(int) fc.size()];
		if (byteBuffer.remaining() > 0) {
			byteBuffer.get(bos, 0, byteBuffer.remaining());
		}

		String downloadFileName = new String(request.getParameter("downloadFileName"));
		if (downloadFileName != null && !downloadFileName.equals("")) {
			fileName = downloadFileName;
		}

		// response.setContentType("application/msexcel");//magic is here
		// 解决下载文件名中文乱码问题
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(file.getName(), PARAM.ENCODING) + "\"");

		response.setContentLength(bos.length);
		// response.setCharacterEncoding(encoding);
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
//		// 删除已下载文件
//		File path = new File(filePath);//
//		path.delete();
	}
}
