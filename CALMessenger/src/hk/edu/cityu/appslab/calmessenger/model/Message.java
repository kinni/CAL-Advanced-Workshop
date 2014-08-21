package hk.edu.cityu.appslab.calmessenger.model;

public class Message {

	public final static int TYPE_INCOMING = 1;
	public final static int TYPE_OUTGOING = 2;

	private int id;
	private int uid;
	private int type;
	private String what;
	private String created_at;

	private String displayName;

}
