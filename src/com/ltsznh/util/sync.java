package com.ltsznh.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.ltsznh.util.getGsonString;
import com.ltsznh.gwt.shared.model.ResultData;
import com.ltsznh.gwt.shared.param.PARAM;

/**
 * Created by 涛 on 2015/6/30.
 */
public class sync {
	private static final int TIMEOUT = 10000;// 10秒

	/**
	 * 通过get方式提交参数给服务器
	 */
	public static ResultData sendGetRequest(String urlPath,
			HashMap<String, Object> params) {
		return sendGetRequest(urlPath, params, "utf-8");
	}

	/**
	 * 通过get方式提交参数给服务器
	 */
	public static ResultData sendGetRequest(String urlPath,
			HashMap<String, Object> params, String encoding) {
		ResultData result = new ResultData();
		InputStream in = null;
		Exception err = null;
		// 使用StringBuilder对象
		// StringBuilder sb = new StringBuilder(urlPath);
		// sb.append('?');
		try { // 迭代Map
				// for (Map.Entry<String, String> entry : params.entrySet()) {
				// sb.append(entry.getKey()).append('=').append(
				// URLEncoder.encode(entry.getValue(), encoding)).append('&');
				// }
				// sb.deleteCharAt(sb.length() - 1);
				// sb.append("param=");
				// sb.append(new Gson().toJson(params, HashMap.class));

			byte[] sendData = URLEncoder.encode(
					new getGsonString().toGsonString(params), PARAM.ENCODING)
					.getBytes(PARAM.ENCODING);

			// 打开链接
			URL url = new URL(urlPath);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(TIMEOUT);
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Charset", PARAM.ENCODING);

			// conn.setUseCaches(false);

			conn.setRequestProperty("Content-Length",
					String.valueOf(sendData.length));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(sendData);
			outStream.flush();
			outStream.close();

			// 如果请求响应码是200，则表示成功
			if (conn.getResponseCode() == 200) {
				// 获得服务器响应的数据
				in = new BufferedInputStream(conn.getInputStream());
				result = Convert.inputStreamToResultData(in, encoding);
				in.close();

				return result;
				// in无值时需要处理
			} else {
				result.setStatus(false);
				result.setMessage("ERROR CODE:" + conn.getResponseCode()
						+ "\nERROR:" + conn.getResponseMessage());
				return result;
			}
		} catch (UnsupportedEncodingException e) {
			err = e;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			err = e;
			e.printStackTrace();
		} catch (ProtocolException e) {
			err = e;
			e.printStackTrace();
		} catch (IOException e) {
			err = e;
			e.printStackTrace();
		}
		result.setStatus(false);
		result.setMessage(err.getLocalizedMessage());

		return result;
	}

	/**
	 * 通过Post方式提交参数给服务器,也可以用来传送json或xml文件
	 */
	public static ResultData sendPostRequest(String urlPath,
			HashMap<String, Object> params) {
		return sendPostRequest(urlPath, params, "utf-8");
	}

	/**
	 * 通过Post方式提交参数给服务器,也可以用来传送json或xml文件
	 */
	public static ResultData sendPostRequest(String urlPath,
			HashMap<String, Object> params, String encoding) {
		URL url = null;
		try {
			url = new URL(urlPath);
			if (url.getProtocol().equalsIgnoreCase("https")) {
				return sendPostRequestHttps(urlPath, params, encoding);
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ServerLog.log("sendPostRequest", params.getOrDefault("id", "")
				.toString(), urlPath);
		ResultData result = new ResultData();
		InputStream in = null;

		Exception err = null;

		byte[] sendData = null;
		try {
			try {
				sendData = URLEncoder.encode(
						new getGsonString().toGsonString(params),
						PARAM.ENCODING).getBytes(PARAM.ENCODING);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			// 打开链接
			OutputStream outStream;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(TIMEOUT);
			// 如果通过post提交数据，必须设置允许对外输出数据
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Charset", encoding);
			conn.setRequestProperty("Content-Length",
					String.valueOf(sendData.length));
			outStream = conn.getOutputStream();
			outStream.write(sendData);
			outStream.flush();
			outStream.close();

			// 如果请求响应码是200，则表示成功
			if (conn.getResponseCode() == 200) {
				// 获得服务器响应的数据
				in = new BufferedInputStream(conn.getInputStream());
				result = Convert.inputStreamToResultData(in, encoding);

				return result;
				// in无值时需要处理
			} else {
				result.setStatus(false);
				result.setMessage("ERROR CODE:" + conn.getResponseCode()
						+ "\nERROR:" + conn.getResponseMessage());
				return result;
			}
			// }

		} catch (UnsupportedEncodingException e) {
			err = e;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			err = e;
			e.printStackTrace();
		} catch (ProtocolException e) {
			err = e;
			e.printStackTrace();
		} catch (IOException e) {
			err = e;
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		result.setStatus(false);
		result.setMessage(err.getLocalizedMessage());
		return result;
	}

	private static ResultData sendPostRequestHttps(String urlPath,
			HashMap<String, Object> params, String encoding) {
		// TODO Auto-generated method stub
		ResultData result = new ResultData();
		InputStream in = null;
		Exception err = null;
		byte[] sendData = null;
		try {
			sendData = URLEncoder.encode(
					new getGsonString().toGsonString(params), PARAM.ENCODING)
					.getBytes(PARAM.ENCODING);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ServerLog.log("sendPostRequestHttps", params.getOrDefault("id", "")
				.toString(), urlPath);
		try {
			URL url = new URL(urlPath);
			OutputStream outStream;

			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0],
					new TrustManager[] { new DefaultTrustManager() },
					new SecureRandom());
			SSLContext.setDefault(ctx);

			// 打开链接
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}

			});

			conn.setRequestMethod("POST");
			conn.setConnectTimeout(TIMEOUT);
			// 如果通过post提交数据，必须设置允许对外输出数据
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Charset", encoding);
			conn.setRequestProperty("Content-Length",
					String.valueOf(sendData.length));
			outStream = conn.getOutputStream();
			outStream.write(sendData);
			outStream.flush();
			outStream.close();

			// 如果请求响应码是200，则表示成功
			if (conn.getResponseCode() == 200) {
				// 获得服务器响应的数据
				in = new BufferedInputStream(conn.getInputStream());
				result = Convert.inputStreamToResultData(in, encoding);

				return result;
				// in无值时需要处理
			} else {
				result.setStatus(false);
				result.setMessage("ERROR CODE:" + conn.getResponseCode()
						+ "\nERROR:" + conn.getResponseMessage());
				return result;
			}
		} catch (UnsupportedEncodingException e) {
			err = e;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			err = e;
			e.printStackTrace();
		} catch (ProtocolException e) {
			err = e;
			e.printStackTrace();
		} catch (IOException e) {
			err = e;
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		result.setStatus(false);
		result.setMessage(err.getLocalizedMessage());
		return result;
	}

	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}
}
