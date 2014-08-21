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

public class MessageAdapter extends BaseAdapter{
	
	private ArrayList<Message> messageList;
	private Context mContext;
	
	public MessageAdapter(Context context, ArrayList<Message> messageList){
		this.mContext = context;
		this.messageList = messageList;
	}

	@Override
	public int getCount() {
		return this.messageList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.messageList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		
		Message message = messageList.get(position);
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		
		if (message.getType() == Message.TYPE_INCOMING)
			view = inflater.inflate(R.layout.item_message_received, null);
		else
			view = inflater.inflate(R.layout.item_message_sent, null);
		
		
		TextView messageTV = (TextView) view.findViewById(R.id.message);
		TextView createdAtTV = (TextView) view.findViewById(R.id.created_at);
		
		messageTV.setText(message.getWhat());
		createdAtTV.setText(message.getCreated_at());
		
		return view;
	}
	
	

}
