package hk.edu.cityu.appslab.calmessenger.activity;

import hk.edu.cityu.appslab.calmessenger.R;
import hk.edu.cityu.appslab.calmessenger.adapter.MessageAdapter;
import hk.edu.cityu.appslab.calmessenger.database.DatabaseHelper;
import hk.edu.cityu.appslab.calmessenger.model.Message;
import hk.edu.cityu.appslab.calmessenger.utils.APICall;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ConversationActivity extends Activity {

	private final static String TAG = "ConversationActivity";

	public final static String DISPLAY_NAME = "display_name";
	public final static String UID = "uid";

	private ArrayList<Message> messageList;

	private String displayName;
	private int uid;

	private ListView conversation;
	private MessageAdapter adapter;

	private EditText messageET;

	private DatabaseHelper dbHelper;

	private boolean isSending = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayName = getIntent().getStringExtra(DISPLAY_NAME);
		uid = getIntent().getIntExtra(UID, -1);

		dbHelper = DatabaseHelper.getInstance(this);

		initUI();
	}

	private void initUI() {
		// initActionBar
		setTitle(displayName);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// initLayout
		setContentView(R.layout.activity_conversation);
		messageET = (EditText) findViewById(R.id.message);
		conversation = (ListView) findViewById(R.id.conversation_detail_list);
		
		// FIXME: setup the adapter

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
		}

		return super.onOptionsItemSelected(item);
	}

	public void sendMessage(View view) {
		//FIXME: send message 
	}

}
