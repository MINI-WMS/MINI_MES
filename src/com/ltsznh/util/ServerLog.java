package com.ltsznh.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ServerLog {
	private static boolean isLog;
	private static boolean isfileLogLog;
	private static boolean isPrintLog;
	private static String LOG_FILE = "";
	private static String ERR_FILE = "";
	private static PrintWriter logWriter = null;
	private static PrintWriter errWriter = null;
	private static String path;

	static {
		isLog = Boolean.parseBoolean(parameters.getParam("log", "isLog"));
		isfileLogLog = Boolean.parseBoolean(parameters.getParam("log", "isFileLog"));
		isPrintLog = Boolean.parseBoolean(parameters.getParam("log", "isPrintLog"));
		path = parameters.getParam("log", "path");
	}

	public static void err(String id, Exception err) {
		// TODO Auto-generated method stub
		if (isLog) {
			// if (isPrintLog)
			pirntLog("Server", "err: ", id, err.getLocalizedMessage());
			if (isfileLogLog)
				fileLogErr("Server", "err: ", id, err.getLocalizedMessage());
		}
	}

	public static void err(String id, SQLException err) {
		// TODO Auto-generated method stub
		if (isLog) {
			if (isPrintLog)
				pirntLog("Server", "err: ", id, err.getLocalizedMessage());
			if (isfileLogLog)
				fileLogErr("Server", "err: ", id, err.getLocalizedMessage());
		}
	}

	public static void err(String source, String id, Throwable err) {
		if (isLog) {
			if (isPrintLog)
				pirntLog(source, "err:", id, err.getLocalizedMessage());
			if (isfileLogLog)
				fileLogErr(source, "err:", id, err.getLocalizedMessage());
		}
	}

	public static void err(String source, String id, String message) {
		// TODO Auto-generated method stub
		if (isLog) {
			// if (isPrintLog)
			pirntLog(source, "err:", id, message);
			if (isfileLogLog)
				fileLogErr(source, "err:", id, message);
		}
	}

	public static void log(String source, String id, String message) {
		if (isLog) {
			if (isPrintLog)
				pirntLog(source, "log:", id, message);
			if (isfileLogLog)
				fileLogLog(source, "log:", id, message);
		}
	}

	public static void sql(String source, String id, String message) {
		if (isLog) {
			if (isPrintLog)
				pirntLog(source, "sql:", id, message);
			if (isfileLogLog)
				fileLogLog(source, "sql:", id, message);
		}
	}

	public static void pirntLog(String source, String type, String id, String message) {
		System.out.println(parameters.DATE_FORMAT.format(System.currentTimeMillis()) + " " + source + " " + type + " "
				+ id + " " + message + ";");
	}

	private static synchronized void fileLogLog(String source, String type, String id, String message) {
		// TODO Auto-generated method stub
		if (LOG_FILE == null
				|| !LOG_FILE.endsWith((parameters.SIMPLEDA_DATE_FORMAT.format(System.currentTimeMillis())) + ".txt")) {// 日期变化
			if (path == null || path.equals("")) {
				LOG_FILE = System.getProperty("user.dir") + File.separator + "Log" + File.separator
						+ (parameters.SIMPLEDA_DATE_FORMAT.format(System.currentTimeMillis())) + ".txt";// 新文件名
			} else {
				LOG_FILE = path + File.separator + (parameters.SIMPLEDA_DATE_FORMAT.format(System.currentTimeMillis()))
						+ ".txt";// 新文件名
			}

			if (null != logWriter) {// 关闭写文件
				logWriter.flush();
				logWriter.close();
				logWriter = null;
			}
		}
		if (null == logWriter) {
			File file = new File(LOG_FILE);
			System.out.println("Log File:" + file.getPath());
			File path = file.getParentFile();
			if (!path.exists()) {
				path.mkdirs();
			}
			FileWriter fw = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fw = new FileWriter(file, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logWriter = new PrintWriter(fw);
		}
		logWriter.println(parameters.DATE_FORMAT.format(System.currentTimeMillis()) + "\t" + source + "\t" + type + "\t"
				+ id + "\t" + message + ";");
		logWriter.flush();
	}

	private static synchronized void fileLogErr(String source, String type, String id, String message) {
		// TODO Auto-generated method stub
		if (ERR_FILE == null || !ERR_FILE
				.endsWith((parameters.SIMPLEDA_DATE_FORMAT.format(System.currentTimeMillis())) + "err.txt")) {// 日期变化
			if (path == null || path.equals("")) {
				ERR_FILE = System.getProperty("user.dir") + "\\Log\\"
						+ (parameters.SIMPLEDA_DATE_FORMAT.format(System.currentTimeMillis())) + "err.txt";// 新文件名
			} else {
				ERR_FILE = path + "\\" + (parameters.SIMPLEDA_DATE_FORMAT.format(System.currentTimeMillis()))
						+ "err.txt";// 新文件名
			}
			if (null != errWriter) {// 关闭写文件
				errWriter.flush();
				errWriter.close();
				errWriter = null;
			}
		}
		if (null == errWriter) {
			File file = new File(ERR_FILE);
			System.out.println("Log File:" + file.getPath());
			File path = file.getParentFile();
			if (!path.exists()) {
				path.mkdirs();
			}
			FileWriter fw = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fw = new FileWriter(file, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			errWriter = new PrintWriter(fw);
		}
		errWriter.println(parameters.DATE_FORMAT.format(System.currentTimeMillis()) + "\t" + source + "\t" + type + "\t"
				+ id + "\t" + message + ";");
		errWriter.flush();
		fileLogLog(source, type, id, message);
	}
}
