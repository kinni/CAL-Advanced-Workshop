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

	public Message() {
	}

	public Message(int uid, int type, String what) {
		super();
		this.uid = uid;
		this.type = type;
		this.what = what;
	}

	public Message(int uid, int type, String what, String created_at) {
		super();
		this.uid = uid;
		this.type = type;
		this.what = what;
		this.created_at = created_at;
	}

	public Message(int id, int uid, int type, String what, String created_at) {
		super();
		this.id = id;
		this.uid = uid;
		this.type = type;
		this.what = what;
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
