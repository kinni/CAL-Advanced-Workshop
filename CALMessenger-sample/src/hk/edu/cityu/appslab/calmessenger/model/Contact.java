package hk.edu.cityu.appslab.calmessenger.model;

public class Contact {

	private int id;
	private String username;
	private String displayName;
	private int uid;

	public Contact() {
	}

	public Contact(String username, String displayName, int uid) {
		this.username = username;
		this.displayName = displayName;
		this.uid = uid;
	}

	public Contact(int id, String username, String displayName, int uid) {
		this.id = id;
		this.username = username;
		this.displayName = displayName;
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getId() {
		return id;
	}
	
	

}
