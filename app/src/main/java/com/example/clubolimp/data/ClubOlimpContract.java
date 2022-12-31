package com.example.clubolimp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ClubOlimpContract {

    private ClubOlimpContract() {

    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "olympus";

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.clubolimp";
    public static final String PATH_MEMBERS = "members";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTHORITY);

    public static final class MemberEntry implements BaseColumns {

        public static final String TABLE_NAME = "members";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SPORT = "sport";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEMS = "vnd.android.cursor.dir/" + AUTHORITY +"/"+ PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM = "vnd.android.cursor.item" + AUTHORITY +"/"+ PATH_MEMBERS;

        /** Выше указанные 2 строки прописываются для метода getType(этот метод получает тип данных из URI)
        **/

        /** Но можно и с помощью ContentResolver.CURSOR_DIR_BASE_TYPE(Для нескольких данных) ⬇
            Для одной строки --> ContentResolver.CURSOR_ITEM_BASE_TYPE ⬇
        **/
        /*public static final String CONTENT_MULTIPLE_ITEMS1 = ContentResolver.CURSOR_DIR_BASE_TYPE + AUTHORITY +"/"+ PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM2 = ContentResolver.CURSOR_ITEM_BASE_TYPE + AUTHORITY +"/"+ PATH_MEMBERS;*/
    }

}
