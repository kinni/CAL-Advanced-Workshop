package hk.edu.cityu.appslab.calmessenger.activity;

import hk.edu.cityu.appslab.calmessenger.App;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent;

		App app = App.getInstance();
		if (app.getToken().isEmpty()) {
			intent = new Intent(this, RegisterActivity.class);
		} else {
			intent = new Intent(this, MainActivity.class);
		}
		startActivity(intent);

		finish();
	}

}
