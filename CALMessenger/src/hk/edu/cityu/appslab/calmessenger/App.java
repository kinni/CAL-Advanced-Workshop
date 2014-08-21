package hk.edu.cityu.appslab.calmessenger;

import hk.edu.cityu.appslab.calmessenger.gcm.GcmManager;
import hk.edu.cityu.appslab.calmessenger.gcm.MyGcmIntentService;
import android.app.Application;
import android.content.SharedPreferences;

public class App extends Application {

	private String token;

	private static App sInstance;

	public static App getInstance() {
		return sInstance;
	}

	private final static String PREF_NAME = "cal_messenger";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sInstance = this;
		init();
	}

	private void init() {

		if (!token.isEmpty()) {
			GcmManager gcmManager = GcmManager.getInstance(this);
			gcmManager.setHandleIntentService(MyGcmIntentService.class);
			gcmManager.enablePushNotification();
		}
	}

	public String getToken() {
		return this.token;
	}

	public void storeToken(String token) {

	}

	public void clearToken() {

	}

}
