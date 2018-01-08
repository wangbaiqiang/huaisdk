package com.huai.gamesdk.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import com.huai.gamesdk.exception.GameSDKException;

public final class GameHttpTool {

/*	private OkGoUtils() {}

	private static volatile OkGoUtils httpOkUtils = null;

	public static OkGoUtils getInstance() {
		synchronized (OkGoUtils.class) {
			if (httpOkUtils == null) {
				httpOkUtils = new OkGoUtils();
			}
		}
		return httpOkUtils;
	}*/



	public static final int CONNECT_TIMEOUT = 8000;
	public static final int CONNECT_TIMEOUT2 = 6000;
	public static final int RESPONSE_TIMEOUT = 10000;
	public static final String UTF8 = "UTF-8";

	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";

	private static MyX509TrustManager x509TrusManager = new MyX509TrustManager();
	private static MyHostnameVerifier hostnameVerifier = new MyHostnameVerifier();
	private static SSLContext sslContext = null;
	static {
		try {
			sslContext = SSLContext.getInstance("TLS");
			X509TrustManager[] xtmArray = new X509TrustManager[] { x509TrusManager };
			sslContext.init(null, xtmArray, new java.security.SecureRandom());
			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			}
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		} catch (Exception e) {
		}
	}

	public static HttpResult get(String url) {
		return get(url, UTF8);
	}



	public static HttpResult get(String url, String encode) {
		HttpURLConnection con = null;
		InputStream in = null;
		HttpResult result = new HttpResult();
		try {
			con = (HttpURLConnection) (new URL(url).openConnection());
			con.setDoInput(true);
			con.setRequestMethod(METHOD_GET);
			con.setConnectTimeout(CONNECT_TIMEOUT);// 连接超时时间
			con.setReadTimeout(RESPONSE_TIMEOUT);// 响应超时时间
			result.code = con.getResponseCode();
			if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
				in = con.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
				StringBuilder buffer = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}

				result.message = buffer.toString();
			}
		} catch (Exception e) {
			GameSdkLog.getInstance().e("往[" + url + "]发送GET请求时异常：", e);
			result.message = e.getMessage();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (con != null) {
				con.disconnect();
			}
		}
		return result;
	}

	public static HttpResult post(String url,Integer timeout, String requestData) {
		return post(url, timeout,requestData, UTF8);
	}

	public static HttpResult post(String url,Integer timeout,String requestData, String encode) {
		HttpURLConnection con = null;
		OutputStream out = null;
		InputStream in = null;
		HttpResult result = new HttpResult();
		try {
			con = (HttpURLConnection) (new URL(url).openConnection());
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod(METHOD_POST);
			con.setConnectTimeout(timeout);// 连接超时时间
			con.setReadTimeout(RESPONSE_TIMEOUT);// 响应超时时间
			out = con.getOutputStream();
			out.write(requestData.getBytes(encode));
			out.flush();

			result.code = con.getResponseCode();
			if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
				in = con.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
				StringBuilder buffer = new StringBuilder("");
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result.message = buffer.toString();
			}
		} catch (Exception e) {
			GameSdkLog.getInstance().e("往[" + url + "]发送POST请求时异常：", e);
			result.message = e.getMessage();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (con != null) {
				con.disconnect();
			}
		}
		return result;
	}

	public static HttpResult httpsPost(String url, String requestData) {
		return httpsPost(url, requestData, UTF8);
	}

	/**
	 * 发送HTTPS POST请求
	 * 
	 * @param url
	 *            请求地址
	 * @param requestData
	 *            请求数据
	 * @param encode
	 *            字符集
	 * @return HttpResult
	 * @throws GameSDKException
	 * @throws HttpException
	 */
	public static HttpResult httpsPost(String url, String requestData, String encode) {
		HttpsURLConnection conn = null;
		InputStream in = null;
		OutputStream out = null;
		HttpResult result = new HttpResult();
		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setHostnameVerifier(hostnameVerifier);
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestMethod(METHOD_POST);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(RESPONSE_TIMEOUT);
			out = conn.getOutputStream();
			out.write(requestData.getBytes(encode));
			out.flush();

			result.code = conn.getResponseCode();
			if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				in = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
				StringBuilder buffer = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result.message = buffer.toString();
			}
			return result;
		} catch (IOException e) {
			GameSdkLog.getInstance().e("往[" + url + "]发送POST请求时异常：", e);
			result.message = e.getMessage();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;

	}

	public static HttpResult httpsPostWithErrInfo(String url, String requestData) {
		return httpsPostWithErrInfo(url, requestData, UTF8);
	}

	public static HttpResult httpsPostWithErrInfo(String url, String requestData, String encode) {
		HttpsURLConnection conn = null;
		InputStream in = null;
		OutputStream out = null;
		HttpResult result = new HttpResult();
		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setHostnameVerifier(hostnameVerifier);
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestMethod(METHOD_POST);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(RESPONSE_TIMEOUT);
			out = conn.getOutputStream();
			out.write(requestData.getBytes(encode));
			out.flush();

			result.code = conn.getResponseCode();
			if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
			StringBuilder buffer = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			result.message = buffer.toString();

		} catch (IOException e) {
			GameSdkLog.getInstance().e("往[" + url + "]发送POST请求时异常：", e);
			result.message = e.getMessage();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	public static HttpResult httpsGet(String url) {
		return httpsGet(url, UTF8);
	}

	/**
	 * 发送HTTPS Get请求
	 * 
	 * @param url
	 *            请求地址
	 * @param charset
	 *            字符集
	 * @return HttpResult
	 * @throws GameSDKException
	 */
	public static HttpResult httpsGet(String url, String encode) {
		HttpsURLConnection conn = null;
		InputStream in = null;
		HttpResult result = new HttpResult();
		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setHostnameVerifier(hostnameVerifier);
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestMethod(METHOD_GET);
			conn.setDoInput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(RESPONSE_TIMEOUT);

			result.code = conn.getResponseCode();
			if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				in = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
				StringBuilder buffer = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result.message = buffer.toString();
			}
		} catch (IOException e) {
			GameSdkLog.getInstance().e("往[" + url + "]发送https get请求时异常：", e);
			result.message = e.getMessage();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	private static class MyX509TrustManager implements X509TrustManager {
		public MyX509TrustManager() {
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	private static class MyHostnameVerifier implements HostnameVerifier {
		public MyHostnameVerifier() {
		}

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public static class HttpResult {
		public int code;
		public String message;
	}
}
