package hk.edu.cityu.appslab.calmessenger.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import hk.edu.cityu.appslab.calmessenger.R;
import hk.edu.cityu.appslab.calmessenger.database.DatabaseHelper;
import hk.edu.cityu.appslab.calmessenger.model.Contact;
import hk.edu.cityu.appslab.calmessenger.utils.APICall;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactActivity extends Activity {

	private final static String TAG = "AddContactActivity";

	private Button searchBtn;
	private ProgressBar progressBar;
	private LinearLayout resultPanel;
	private ImageView avatarIV;
	private TextView displayNameTV;
	private TextView userNotFoundTV;
	private Button addBtn;

	private EditText usernameET;

	private boolean isUserFound = false;
	private Contact resultContact;
	private boolean newContactAdded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initUI();

	}

	private void initUI() {
		// Set activity title
		setTitle(R.string.title_add_contact);

		// Set Action Bar
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activitiy_add_contact2);
		searchBtn = (Button) findViewById(R.id.search);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		usernameET = (EditText) findViewById(R.id.username);
		usernameET.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (v.getId() == R.id.username && !hasFocus) {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				}
			}

		});

		// Result Panel
		resultPanel = (LinearLayout) findViewById(R.id.result_panel);
		avatarIV = (ImageView) findViewById(R.id.avatar);
		displayNameTV = (TextView) findViewById(R.id.search_result_displayname);
		userNotFoundTV = (TextView) findViewById(R.id.search_result_user_not_found);
		addBtn = (Button) findViewById(R.id.search_result_add);

	}

	private void updateUI(boolean isLoading) {
		resultPanel.setVisibility(isLoading ? View.GONE : View.VISIBLE);
		progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
	}

	private void updateResultPanelUI(boolean isUserFound) {
		avatarIV.setVisibility(isUserFound ? View.VISIBLE : View.GONE);
		displayNameTV.setVisibility(isUserFound ? View.VISIBLE : View.GONE);
		addBtn.setVisibility(isUserFound ? View.VISIBLE : View.GONE);
		userNotFoundTV.setVisibility(isUserFound ? View.GONE : View.VISIBLE);

		if (isUserFound) {
			displayNameTV.setText(resultContact.getDisplayName());
		}
	}

	public void onSearch(View view) {
		// FIXME: Handling search contact
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void onAdd(View view) {
		// FIXME: Handling add contact
	}

}
