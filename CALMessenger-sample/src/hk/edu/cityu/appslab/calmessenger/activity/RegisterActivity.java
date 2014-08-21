package hk.edu.cityu.appslab.calmessenger.activity;

import hk.edu.cityu.appslab.calmessenger.App;
import hk.edu.cityu.appslab.calmessenger.R;
import hk.edu.cityu.appslab.calmessenger.gcm.GcmManager;
import hk.edu.cityu.appslab.calmessenger.gcm.MyGcmIntentService;
import hk.edu.cityu.appslab.calmessenger.utils.APICall;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends Activity {

	private final static String TAG = "RegisterActivity";

	private EditText usernameET, displayNameET, passwordET;
	private Button registerBtn;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initUI();
	}

	private void initUI() {
		setTitle("Register");

		setContentView(R.layout.activity_register);
		usernameET = (EditText) findViewById(R.id.username);
		displayNameET = (EditText) findViewById(R.id.display_name);
		passwordET = (EditText) findViewById(R.id.password);
		registerBtn = (Button) findViewById(R.id.register_btn);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);

	}

	private void updateUI(boolean isLoading) {
		registerBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
		progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
	}

	public void doRegister(View view) {
		updateUI(true);
		String username = usernameET.getText().toString().trim();
		String displayName = displayNameET.getText().toString().trim();
		String password = passwordET.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("displayName", displayName);
		params.put("password", password);
		APICall.post("f=register", params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				try {
					updateUI(false);
					Toast.makeText(getApplicationContext(),
							errorResponse.getString("message"),
							Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "Unknown error",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				updateUI(false);
				Log.d(TAG, response.toString());

				// Register OK!
				try {
					GcmManager gcmManager = GcmManager
							.getInstance(RegisterActivity.this);
					gcmManager.setHandleIntentService(MyGcmIntentService.class);
					gcmManager.enablePushNotification();

					App app = App.getInstance();
					app.storeToken(response.getString("token"));
					Intent intent = new Intent(RegisterActivity.this,
							MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

}
