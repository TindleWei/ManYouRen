package de.greenrobot.daoexample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.daoexample.Share;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SHARE.
*/
public class ShareDao extends AbstractDao<Share, Long> {

    public static final String TABLENAME = "SHARE";

    /**
     * Properties of entity Share.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property CreateTime = new Property(2, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property Location = new Property(3, String.class, "location", false, "LOCATION");
        public final static Property City = new Property(4, String.class, "city", false, "CITY");
        public final static Property Score = new Property(5, Integer.class, "score", false, "SCORE");
        public final static Property UserId = new Property(6, Long.class, "userId", false, "USER_ID");
        public final static Property Images = new Property(7, String.class, "images", false, "IMAGES");
        public final static Property GuideId = new Property(8, Integer.class, "guideId", false, "GUIDE_ID");
    };


    public ShareDao(DaoConfig config) {
        super(config);
    }
    
    public ShareDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SHARE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CONTENT' TEXT," + // 1: content
                "'CREATE_TIME' INTEGER," + // 2: createTime
                "'LOCATION' TEXT," + // 3: location
                "'CITY' TEXT," + // 4: city
                "'SCORE' INTEGER," + // 5: score
                "'USER_ID' INTEGER," + // 6: userId
                "'IMAGES' TEXT," + // 7: images
                "'GUIDE_ID' INTEGER);"); // 8: guideId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SHARE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Share entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(3, createTime);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(4, location);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(5, city);
        }
 
        Integer score = entity.getScore();
        if (score != null) {
            stmt.bindLong(6, score);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(7, userId);
        }
 
        String images = entity.getImages();
        if (images != null) {
            stmt.bindString(8, images);
        }
 
        Integer guideId = entity.getGuideId();
        if (guideId != null) {
            stmt.bindLong(9, guideId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Share readEntity(Cursor cursor, int offset) {
        Share entity = new Share( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // content
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // createTime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // location
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // city
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // score
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // userId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // images
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // guideId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Share entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreateTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setLocation(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCity(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setScore(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setUserId(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setImages(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setGuideId(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Share entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Share entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}