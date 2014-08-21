package hk.edu.cityu.appslab.calmessenger.activity;

import hk.edu.cityu.appslab.calmessenger.App;
import hk.edu.cityu.appslab.calmessenger.R;
import hk.edu.cityu.appslab.calmessenger.adapter.LatestConversationAdapter;
import hk.edu.cityu.appslab.calmessenger.database.DatabaseHelper;
import hk.edu.cityu.appslab.calmessenger.model.Message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	private final static String TAG = "MainActivity";

	private ListView conversationListView;

	private ArrayList<Message> latestConversations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();

	}

	@Override
	protected void onResume() {
		super.onResume();

		fetchLatestConversations();
	}

	private void initUI() {
		setContentView(R.layout.activity_main);

		conversationListView = (ListView) findViewById(R.id.conversation_list);
		conversationListView.setEmptyView(findViewById(R.id.empty));
	}

	private void fetchLatestConversations() {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
		latestConversations = dbHelper.getLatestConversations();
		LatestConversationAdapter adapter = new LatestConversationAdapter(this,
				latestConversations);
		conversationListView.setAdapter(adapter);
		conversationListView.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
		case R.id.action_new_message:
			Intent intent = new Intent(this, ContactListActivity.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			// Preform logout
			// Remove token
			App.getInstance().clearToken();
			Toast.makeText(this, "Token cleared", Toast.LENGTH_SHORT).show();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ConversationActivity.class);
		Message message = latestConversations.get(position);
		if (message.getDisplayName() != null) {
			intent.putExtra(ConversationActivity.DISPLAY_NAME,
					message.getDisplayName());
		} else {
			intent.putExtra(ConversationActivity.DISPLAY_NAME,
					String.valueOf(message.getUid()));
		}
		
		intent.putExtra(ConversationActivity.UID, message.getUid());
		startActivity(intent);

	}

}
