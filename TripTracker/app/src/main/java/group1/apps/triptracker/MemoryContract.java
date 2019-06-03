package group1.apps.triptracker;

import android.provider.BaseColumns;

public final class MemoryContract {

    private MemoryContract() {

    }

    public static class MemoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "memories";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE_ADDED = "date_added";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_LOCATION = "location";
    }

    public static final String SQL_CREATE_MEMORIES =
            "CREATE TABLE " + MemoryEntry.TABLE_NAME + " (" +
                    MemoryEntry._ID + " INTEGER PRIMARY KEY," +
                    MemoryEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MemoryEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    MemoryEntry.COLUMN_NAME_DATE_ADDED + " TEXT," +
                    MemoryEntry.COLUMN_NAME_IMAGE + " BLOB," +
                    MemoryEntry.COLUMN_NAME_LOCATION + " TEXT)";

    public static final String SQL_DELETE_MEMORIES =
            "DROP TABLE IF EXISTS " + MemoryEntry.TABLE_NAME;
}
