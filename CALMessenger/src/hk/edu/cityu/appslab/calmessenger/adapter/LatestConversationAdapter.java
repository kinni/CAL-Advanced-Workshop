package hk.edu.cityu.appslab.calmessenger.adapter;

import hk.edu.cityu.appslab.calmessenger.R;
import hk.edu.cityu.appslab.calmessenger.model.Message;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LatestConversationAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Message> messageList;

	public LatestConversationAdapter(Context mContext,
			ArrayList<Message> messageList) {
		this.mContext = mContext;
		this.messageList = messageList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_conversation, null);
			viewHolder = new ViewHolder();
			viewHolder.displayNameTV = (TextView) convertView
					.findViewById(R.id.display_name);
			viewHolder.latestMessageTV = (TextView) convertView
					.findViewById(R.id.latest_message);
			viewHolder.messageTimeTV = (TextView) convertView
					.findViewById(R.id.message_time);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Message message = messageList.get(position);

		if (message.getDisplayName() != null) {
			viewHolder.displayNameTV.setText(message.getDisplayName());
		} else {
			viewHolder.displayNameTV.setText("Stranger(UID:"+String.valueOf(message.getUid())+")");
		}
		viewHolder.latestMessageTV.setText(message.getWhat());
		viewHolder.messageTimeTV.setText(message.getCreated_at());

		return convertView;
	}

	static class ViewHolder {

		TextView displayNameTV;
		TextView latestMessageTV;
		TextView messageTimeTV;
	}

}
