package hk.edu.cityu.appslab.calmessenger.activity;

import hk.edu.cityu.appslab.calmessenger.R;
import hk.edu.cityu.appslab.calmessenger.database.DatabaseHelper;
import hk.edu.cityu.appslab.calmessenger.model.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContactListActivity extends Activity implements
		OnItemClickListener {

	private final static String TAG = "ContactListActivity";

	private ListView contactListView;
	private ArrayList<Contact> contactList;

	private final static int REQUEST_ADD_CONTACT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		fetchContactList();

	}

	private void initUI() {
		// Set Action Bar
		setTitle(R.string.title_find_friends);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activtiy_contact_list);

		contactListView = (ListView) findViewById(R.id.contact_list);
		contactListView.setEmptyView(findViewById(R.id.empty));
	}

	private void fetchContactList() {
		DatabaseHelper dbhelper = DatabaseHelper
				.getInstance(getApplicationContext());
		contactList = dbhelper.getAllContacts();
		ArrayList<Map<String, String>> contactListInHashMap = contactListToHashMap(contactList);

		String[] from = { "displayName" };
		int[] to = { android.R.id.text1 };
		SimpleAdapter adapter = new SimpleAdapter(this, contactListInHashMap,
				android.R.layout.simple_list_item_1, from, to);

		contactListView.setAdapter(adapter);
		contactListView.setOnItemClickListener(this);

	}

	private ArrayList<Map<String, String>> contactListToHashMap(
			ArrayList<Contact> contactList) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Contact contact : contactList) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("displayName", contact.getDisplayName());
			list.add(item);
		}

		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_new_contact:
			goToAddContactActivitiy();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.add_your_friend) {
			goToAddContactActivitiy();
		}
	}

	private void goToAddContactActivitiy() {
		Intent addContactIntent = new Intent(this, AddContactActivity.class);
		startActivityForResult(addContactIntent, REQUEST_ADD_CONTACT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ADD_CONTACT) {
			if (resultCode == RESULT_OK) {
				// New contact(s) has been added
				fetchContactList();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Contact selectedContact = contactList.get(position);
		Intent intent = new Intent(this, ConversationActivity.class);
		intent.putExtra(ConversationActivity.DISPLAY_NAME,
				selectedContact.getDisplayName());
		intent.putExtra(ConversationActivity.UID, selectedContact.getUid());
		startActivity(intent);

	}

}
