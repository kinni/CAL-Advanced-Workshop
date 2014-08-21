/*
 * CityU Apps Lab 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hk.edu.cityu.appslab.calmessenger.gcm;

import static hk.edu.cityu.appslab.calmessenger.Const.GCM_SENDER_ID;
import hk.edu.cityu.appslab.calmessenger.utils.APICall;

import java.io.IOException;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GcmManager {

	private final static String TAG = "GcmManager";

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private String regid;
	private GoogleCloudMessaging gcm;

	private static GcmManager sInstance;

	public static GcmManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new GcmManager(context);
		}

		return sInstance;

	}

	private String mIntentServiceCompName;

	private Context mContext;

	private GcmManager(Context context) {
		mContext = context;
		gcm = GoogleCloudMessaging.getInstance(mContext);
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mContext);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				if (mContext instanceof Activity) {
					GooglePlayServicesUtil.getErrorDialog(resultCode,
							(Activity) mContext,
							PLAY_SERVICES_RESOLUTION_REQUEST).show();
				}

			} else {
				Log.i(TAG, "This device is not supported.");
				if (mContext instanceof Activity) {
					((Activity) mContext).finish();
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.commit();
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}

		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return mContext.getSharedPreferences(context.getPackageName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(mContext);
					}
					regid = gcm.register(GCM_SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(mContext, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i(TAG, "onPostExecute: regId = " + regid);
				// You should send the registration ID to your server over
				// HTTP, so it
				// can use GCM/HTTP or CCS to send messages to your app.
				sendRegistrationIdToBackend();
			}
		}.execute(null, null, null);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		RequestParams params = new RequestParams();
		params.put("gcmRegId", regid);
		APICall.post("f=updateGcmRegId", params, new JsonHttpResponseHandler());
	}

	public void enablePushNotification() {
		if (checkPlayServices()) {
			regid = getRegistrationId(mContext);

			if (regid.isEmpty()) {
				registerInBackground();
			} else {
				Log.i(TAG, "regId = " + regid);
			}
		}
	}

	public void setHandleIntentService(Class<?> c) {
		this.mIntentServiceCompName = c.getName();
	}

	public String getHandleIntentServiceCompName() {
		return this.mIntentServiceCompName;
	}

}
