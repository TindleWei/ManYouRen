package com.manyouren.manyouren.ui.chatnew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.entity.Msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lzw on 14-5-28.
 */
public class DBMsg {
	public static final String MESSAGES = "messages";
	public static final String FROM_PEER_ID = "fromPeerId";
	public static final String CONVID = "convid";
	public static final String TIMESTAMP = "timestamp";
	public static final String OBJECT_ID = "objectId";
	public static final String CONTENT = "content";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String TO_PEER_ID = "toPeerId";
	public static final String ROOM_TYPE = "roomType";
	public static final String OWNER_ID = "ownerId";
	// 针对性的加6个字段
	public static final String FROM_USER_ID = "fromUserId";
	public static final String FROM_USER_NAME = "fromUserName";
	public static final String FROM_USER_AVATAR = "fromUserAvatar";
	public static final String TO_USER_ID = "toUserId";
	public static final String TO_USER_NAME = "toUserName";
	public static final String TO_USER_AVATAR = "toUserAvatar";

	public static void createTable(SQLiteDatabase db) {
		db.execSQL("create table if not exists messages (id integer primary key, objectId varchar(63) unique,"
				+ "ownerId varchar(255),fromPeerId varchar(255), convid varchar(255),toPeerId varchar(255), "
				+ "content varchar(1023),"
				+ " status integer,type integer,roomType integer,timestamp varchar(63),"
				+ "fromUserId varchar(50), fromUserName varchar(100 ), fromUserAvatar varchar(255),"
				+ "toUserId varchar(50), toUserName varchar(100 ), toUserAvatar varchar(255))");
	}

	public static void dropTable(SQLiteDatabase db) {
		db.execSQL("drop table if exists messages");
	}
	
	public static void deleteMsg(Msg msg) {  
		DBHelper dbHelper = new DBHelper(RootApplication.getInstance(),
				RootApplication.DB_NAME, RootApplication.DB_VER);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MESSAGES,  OBJECT_ID + "=?", new String[]{String.valueOf(msg.getObjectId())});  
    }  

	public static int insertMsg(Msg msg) {
		List<Msg> msgs = new ArrayList<Msg>();
		msgs.add(msg);
		return insertMsgs(msgs);
	}

	public static int insertMsgs(List<Msg> msgs) {
		DBHelper dbHelper = new DBHelper(RootApplication.getInstance(),
				RootApplication.DB_NAME, RootApplication.DB_VER);
		if (msgs == null || msgs.size() == 0) {
			return 0;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		int n = 0;
		try {
			for (Msg msg : msgs) {
				ContentValues cv = new ContentValues();
				cv.put(OBJECT_ID, msg.getObjectId());
				cv.put(TIMESTAMP, msg.getTimestamp() + "");
				cv.put(FROM_PEER_ID, msg.getFromPeerId());
				cv.put(STATUS, msg.getStatus().getValue());
				cv.put(ROOM_TYPE, msg.getRoomType().getValue());
				cv.put(CONVID, msg.getConvid());
				cv.put(TO_PEER_ID, msg.getToPeerId());
				cv.put(OWNER_ID, User.curUserId());
				cv.put(TYPE, msg.getType().getValue());
				cv.put(CONTENT, msg.getContent());
				// 我加的三6个字段
				cv.put(FROM_USER_ID, msg.getFromUserId());
				cv.put(FROM_USER_NAME, msg.getFromName());
				cv.put(FROM_USER_AVATAR, msg.getFromAvatar());
				cv.put(TO_USER_ID, msg.getToUserId());
				cv.put(TO_USER_NAME, msg.getToName());
				cv.put(TO_USER_AVATAR, msg.getToAvatar());

				db.insert(MESSAGES, null, cv);
				n++;
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		return n;
	}

	public static List<Msg> getMsgs(DBHelper dbHelper, String convid, int size) {
		List<Msg> msgs = new ArrayList<Msg>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assert db != null;
		Cursor c = db.query(MESSAGES, null, "convid=?",
				new String[] { convid }, null, null, TIMESTAMP + " desc", size
						+ "");
		while (c.moveToNext()) {
			Msg msg = createMsgByCursor(c);
			msgs.add(msg);
		}
		c.close();
		Collections.reverse(msgs);
		db.close();
		return msgs;
	}

	public static Msg createMsgByCursor(Cursor c) {
		Msg msg = new Msg();
		msg.setFromPeerId(c.getString(c.getColumnIndex(FROM_PEER_ID)));
		msg.setContent(c.getString(c.getColumnIndex(CONTENT)));
		Msg.Status status = Msg.Status.fromInt(c.getInt(c
				.getColumnIndex(STATUS)));
		msg.setStatus(status);
		msg.setConvid(c.getString(c.getColumnIndex(CONVID)));
		msg.setObjectId(c.getString(c.getColumnIndex(OBJECT_ID)));
		int roomTypeInt = c.getInt(c.getColumnIndex(ROOM_TYPE));
		Msg.RoomType roomType = Msg.RoomType.fromInt(roomTypeInt);
		msg.setRoomType(roomType);
		String toPeerId = c.getString(c.getColumnIndex(TO_PEER_ID));
		msg.setToPeerId(toPeerId);
		msg.setTimestamp(Long.parseLong(c.getString(c.getColumnIndex(TIMESTAMP))));
		Msg.Type type = Msg.Type.fromInt(c.getInt(c.getColumnIndex(TYPE)));
		msg.setType(type);
		// 自定义的 6 个属性
		msg.setFromUserId(c.getString(c.getColumnIndex(FROM_USER_ID)));
		msg.setFromName(c.getString(c.getColumnIndex(FROM_USER_NAME)));
		msg.setFromAvatar(c.getString(c.getColumnIndex(FROM_USER_AVATAR)));
		msg.setToUserId(c.getString(c.getColumnIndex(TO_USER_ID)));
		msg.setToName(c.getString(c.getColumnIndex(TO_USER_NAME)));
		msg.setToAvatar(c.getString(c.getColumnIndex(TO_USER_AVATAR)));

		return msg;
	}

	public static List<Msg> getRecentMsgs(String ownerId) {
		DBHelper dbHelper = new DBHelper(RootApplication.getInstance(),
				RootApplication.DB_NAME, RootApplication.DB_VER);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = null;
		List<Msg> msgs = new ArrayList<Msg>();
		try {
			c = db.query(true, MESSAGES, null, "ownerId=?",
					new String[] { ownerId }, CONVID, null,
					TIMESTAMP + " desc", null);
			while (c.moveToNext()) {
				Msg msg = createMsgByCursor(c);
				msgs.add(msg);
			}
		} catch (Exception e) {

		} finally {
			if (c != null)
				c.close();
			db.close();
		}
		return msgs;
	}

	public static int updateStatusAndTimestamp(Msg msg) {
		ContentValues cv = new ContentValues();
		cv.put(STATUS, Msg.Status.SendReceived.getValue());
		cv.put(TIMESTAMP, msg.getContent());
		String objectId = msg.getObjectId();
		return updateMessage(objectId, cv);
	}

	public static int updateMessage(String objectId, ContentValues cv) {
		DBHelper dbHelper = new DBHelper(RootApplication.getInstance(),
				RootApplication.DB_NAME, RootApplication.DB_VER);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int updateN = db.update(MESSAGES, cv, "objectId=?",
				new String[] { objectId });
		db.close();
		return updateN;
	}

	public static int updateStatus(Msg msg) {
		ContentValues cv = new ContentValues();
		cv.put(STATUS, msg.getStatus().getValue());
		return updateMessage(msg.getObjectId(), cv);
	}
}
