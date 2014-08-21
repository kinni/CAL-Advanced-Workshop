package hk.edu.cityu.appslab.calmessenger.utils;

import static hk.edu.cityu.appslab.calmessenger.Const.SERVER_BASE_URL;
import hk.edu.cityu.appslab.calmessenger.App;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APICall {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		if (params != null) {
			params.add("token", App.getInstance().getToken());
		}
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		if (params != null) {
			params.add("token", App.getInstance().getToken());
		}
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return SERVER_BASE_URL + relativeUrl;
	}

}
