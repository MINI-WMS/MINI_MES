package com.ltsznh.Servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.ltsznh.util.ServerLog;
import com.ltsznh.util.Tools;
import com.ltsznh.util.check;
import com.ltsznh.util.get;
import com.ltsznh.util.parameters;
import com.ltsznh.gwt.shared.model.ResultData;
import com.ltsznh.gwt.shared.param.PARAM;

public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = parameters.dataFilePath
			+ File.separator + parameters.appName + File.separator + "WEB-INF"
			+ File.separator + "upload" + File.separator;// tomcat环境根目录下

	/**
	 * Constructor
	 */
	public UploadFileServlet() {
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
		// String initParameterEncoding = config.getInitParameter("encoding");
		// if (initParameterEncoding != null) {
		// setEncoding(initParameterEncoding);
		// }
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		upload(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		upload(request, response);
	}

	@SuppressWarnings("unchecked")
	private void upload(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding(PARAM.ENCODING);
		HashMap<String, Object> param = null;

		String ipString = get.getPIP(request);
		String idString = Tools.getRandomIdString();

		File uploadedFile = null;
		if (ServletFileUpload.isMultipartContent(request)) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// System.out.println("==============="
			// + System.getProperty("file.encoding")); // GBK
			// Parse the request
			try {
				List<FileItem> items = upload.parseRequest(request);

				for (FileItem item : items) {
					if (item.isFormField())
						if (item.getFieldName().equals("param")) {
							param = new Gson().fromJson(URLDecoder.decode(
									item.getString(), PARAM.ENCODING),
									HashMap.class);
							System.out.println(new Gson().fromJson(URLDecoder
									.decode(item.getString(), PARAM.ENCODING),
									HashMap.class));
							// items.remove(item);
							continue;
						}
				}
				if (param == null) {
					response(response, "参数错误！");
					ServerLog.log("UploadFileServlet", "", "参数错误");
				}

				param.put("ip", ipString);
				param.put("id", idString);
				param.put("request", request);
				if (param.get("client_type") == null)
					param.put("client_type", "P");
				if (!check.checkOverdue(param).getStatus()) {
					return;
				}

				for (FileItem item : items) {
					// process only file upload - discard other form item types
					if (item.isFormField())
						continue;

					if (item.getSize() > 100 * 1024 * 1024L) {
						response(response, "");
						return;
					}

					String fileName = new String(item.getName().getBytes(
							System.getProperty("file.encoding")),
							PARAM.ENCODING);
					param.put("oldFileName", fileName);
					ServerLog.log("Servlet",
							ipString + " " + param.get("userCode").toString(),
							fileName + " " + item.getSize());
					// get only the file name not whole path
					if (fileName != null) {
						fileName = FilenameUtils.getName(fileName);
					}
					fileName = idString
							+ param.getOrDefault("userCode", "").toString()
							+ fileName.substring(fileName.lastIndexOf("."),
									fileName.length());

					uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
					param.put("filePath", uploadedFile.getAbsolutePath());
					param.put("fileName", fileName);

					ServerLog.log("Servlet", "get upload file:",
							uploadedFile.getAbsolutePath());
					if (!uploadedFile.getParentFile().exists())
						if (!uploadedFile.getParentFile().mkdirs())
							return;
					uploadedFile.delete();
					if (uploadedFile.createNewFile()) {
						item.write(uploadedFile);
						response.setStatus(HttpServletResponse.SC_CREATED);
						// response(response, fileName);
					} else {
						throw new IOException(
								"The file already exists in repository.");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"An error occurred while creating the file : "
								+ e.getMessage());
				if (uploadedFile.exists())
					uploadedFile.delete();
				return;
			}

		}
		// else {
		// response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
		// "Request contents type is not supported by the servlet.");
		// }
		if (uploadedFile != null && uploadedFile.exists()) {
			String fromClass = param.getOrDefault("fromClass", "").toString();
			String className = param.getOrDefault("className", "").toString();
			String methodName = param.getOrDefault("methodName", "").toString();

			ResultData result = null;
			if (!fromClass.equals("") && !className.equals("")
					&& !methodName.equals("")) {
				String classString = "com.ltsznh." + className;

				ServerLog.log("Servlet upload", idString, ipString + " "
						+ param.getOrDefault("userCode", "") + " " + fromClass
						+ "\n" + classString + " - " + methodName);

				// 根据类名获取到到该类的class类
				Method method = null;
				try {
					Class<?> cls = Class.forName(classString);
					Object obj = cls.newInstance();

					if (param != null) {
						method = cls.getMethod(methodName,
								new Class[] { param.getClass() });
						result = (ResultData) method.invoke(obj,
								new Object[] { param });
					}

				} catch (Exception e) {
					// TODO: handle exception
					ServerLog.err("Server", idString, e);
					if (result == null) {
						result = new ResultData();
						result.setStatus(false);
						result.setMessage(idString + "系统异常，请联系管理员");
					}
				} finally {
					if (uploadedFile.exists())
						uploadedFile.delete();
				}
			}
			// if(result.getStatus()){
			// response(response,result.getMessage());
			// response.sendRedirect(result.getMessage());
			// }
			if (uploadedFile.exists())
				uploadedFile.delete();
		}
	}

	private void response(HttpServletResponse response, String result) {
		// TODO Auto-generated method stub
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

}