package hk.edu.cityu.appslab.calmessenger.gcm;

import hk.edu.cityu.appslab.calmessenger.activity.ConversationActivity;
import hk.edu.cityu.appslab.calmessenger.database.DatabaseHelper;
import hk.edu.cityu.appslab.calmessenger.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

public class MyGcmIntentService extends GcmIntentService {

	@Override
	protected void handleJsonMessage(String jsonString) {
		try {
			JSONObject json = new JSONObject(jsonString);
			int uid = json.getInt("uid");
			String displayName = json.getString("display_name");
			String what = json.getString("what");

			Message message = new Message(uid, Message.TYPE_INCOMING, what);
			storeMessage(message);

			Intent conversationIntent = new Intent(this,
					ConversationActivity.class);
			conversationIntent.putExtra(ConversationActivity.DISPLAY_NAME,
					displayName);
			conversationIntent.putExtra(ConversationActivity.UID, uid);

			sendNotification(displayName, what, conversationIntent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void storeMessage(Message message) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
		dbHelper.insertMessage(message);
	}

}
