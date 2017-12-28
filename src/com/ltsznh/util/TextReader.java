package com.ltsznh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TextReader {
	private String encoding = "GB2312";

	public String getText(String filePath) {
		File file = new File(filePath);
		ServerLog.log("server", "读取文本", file.getAbsolutePath());
		if (file.exists()) {
			// System.out.println(getText(file));
			return getText(file);
		} else {
			return "";
		}
	}

	public String getText(File file) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fis != null)
			return getText(fis);
		else {
			return "";
		}
	}

	public String getText(FileInputStream fis) {
		// TODO Auto-generated method stub
		InputStreamReader read;
		StringBuffer result = new StringBuffer();
		try {
			read = new InputStreamReader(fis, encoding);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt;

			while ((lineTxt = bufferedReader.readLine()) != null) {
				// System.out.println(lineTxt);
				result.append(lineTxt);
			}
			read.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();
	}

	public static void main(String[] arg) {
		System.out.println(new TextReader()
				.getText("war\\WEB-INF\\template\\html\\btkrqrjbb.html"));
	}
}
