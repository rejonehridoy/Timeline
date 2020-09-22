package com.example.timeline;

import android.provider.BaseColumns;

public class TimelineDB {
    public TimelineDB() {
    }

    public static final class Activity implements BaseColumns{
        public static final String TABLE_NAME = "Activity";
        public static final String COLUMN_USERNAME = "userName";
        public static final String COLUMN_ACTIVITY_TYPE = "activityType";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_YOUR_TEAM = "yourTeam";
        public static final String COLUMN_OPPONENT_TEAM = "opponentTeam";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_REVIEW = "review";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DAYOFWEEK = "dayOfWeek";
        public static final String COLUMN_RESULT = "result";
        public static final String COLUMN_MODIFIED_DATE = "modifiedDate";
        public static final String COLUMN_BACKUP = "backup";

    }

    public static final class PasswordManager implements BaseColumns{
        public static final String TABLE_NAME = "PasswordManager";
        public static final String COLUMN_USERNAME = "userName";
        public static final String COLUMN_ACCOUNT_NAME = "accName";
        public static final String COLUMN_ACCOUNT_LOGO = "accLogo";
        public static final String COLUMN_ACCOUNT_USERNAME = "accUserName";
        public static final String COLUMN_ACCOUNT_EMAIL = "accEmail";
        public static final String COLUMN_ACCOUNT_PHONE = "accPhone";
        public static final String COLUMN_ACCOUNT_PASSWORD = "accPassword";
        public static final String COLUMN_ACCOUNT_PRIORITY = "accPriority";
        public static final String COLUMN_ACCOUNT_NOTES = "accNotes";
        public static final String COLUMN_ACCOUNT_CREATED_DATE = "accCreatedDate";
        public static final String COLUMN_ACCOUNT_MODIFIED_DATE = "accModifiedDate";

    }
    public static final class Notes implements BaseColumns{
        public static final String TABLE_NAME = "Notes";
        public static final String COLUMN_USERNAME = "userName";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DAYOFWEEK = "dayOfWeek";
        public static final String COLUMN_MODIFIED_DATE = "modifiedDate";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_BACKUP = "backup";
    }


}
