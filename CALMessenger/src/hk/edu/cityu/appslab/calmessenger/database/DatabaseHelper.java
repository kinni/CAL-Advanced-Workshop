package hk.edu.cityu.appslab.calmessenger.database;

import hk.edu.cityu.appslab.calmessenger.model.Contact;
import hk.edu.cityu.appslab.calmessenger.model.Message;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper sInstance;

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "cal_messenger";

	private static final String CONTACTS_TABLE_NAME = "contacts";
	private static final String CONTACTS_COLUMN_ID = "id";
	private static final String CONTACTS_COLUMN_USERNAME = "username";
	private static final String CONTACTS_COLUMN_DISPLAY_NAME = "display_name";
	private static final String CONTACTS_COLUMN_UID = "uid";

	private static final String CONTACTS_TABLE_CREATE = "CREATE TABLE "
			+ CONTACTS_TABLE_NAME + " (" + CONTACTS_COLUMN_ID
			+ " INTEGER PRIMARY KEY, " + CONTACTS_COLUMN_DISPLAY_NAME
			+ " TEXT, " + CONTACTS_COLUMN_USERNAME + " TEXT, "
			+ CONTACTS_COLUMN_UID + " INTEGER" + ");";

	

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}

		return sInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CONTACTS_TABLE_CREATE);
		//FIXME: create message table
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// We first remove all existing tables, then re-create them one by one
		db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);

		// Re-create all the tables
		onCreate(db);
	}

	public long insertContact(Contact contact) {
		return insertContact(contact.getUsername(), contact.getDisplayName(),
				contact.getUid());
	}

	public long insertContact(String username, String displayName, int uid) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(CONTACTS_COLUMN_USERNAME, username);
		contentValues.put(CONTACTS_COLUMN_DISPLAY_NAME, displayName);
		contentValues.put(CONTACTS_COLUMN_UID, uid);

		return db.insert("contacts", null, contentValues);
	}

	public Contact getContactByUid(int uid) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = String.format("select * from %s WHERE %s=%d",
				CONTACTS_TABLE_NAME, CONTACTS_COLUMN_UID, uid);
		Cursor c = db.rawQuery(sql, null);
		if (c != null)
			c.moveToFirst();
		else
			return null;

		return cursorToContact(c);

	}

	public ArrayList<Contact> getAllContacts() {

		ArrayList<Contact> contactList = new ArrayList<Contact>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + CONTACTS_TABLE_NAME, null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			contactList.add(cursorToContact(c));
			c.moveToNext();
		}
		return contactList;
	}

	private Contact cursorToContact(Cursor c) {
		int id = c.getInt(c.getColumnIndex(CONTACTS_COLUMN_ID));
		String username = c.getString(c
				.getColumnIndex(CONTACTS_COLUMN_USERNAME));
		String displayName = c.getString(c
				.getColumnIndex(CONTACTS_COLUMN_DISPLAY_NAME));
		int uid = c.getInt(c.getColumnIndex(CONTACTS_COLUMN_UID));
		Contact contact = new Contact(id, username, displayName, uid);
		return contact;
	}

	

}
