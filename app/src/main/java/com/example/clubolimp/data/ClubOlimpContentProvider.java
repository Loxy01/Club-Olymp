package com.example.clubolimp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clubolimp.data.ClubOlimpContract.*;

public class ClubOlimpContentProvider extends ContentProvider {

    OlimpDbHelper dbOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(ClubOlimpContract.AUTHORITY,
                ClubOlimpContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubOlimpContract.AUTHORITY,
                ClubOlimpContract.PATH_MEMBERS
                        + "/#", MEMBER_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new OlimpDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]
                        {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can't query incorrect URI "
                        + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /*public void searchMemberByName(String nameFromEditText, ListView listView){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cor = db.rawQuery("SELECT * FROM students WHERE name='"+ nameFromEditText +"'", null);
            try{
                cor.moveToFirst();
                listView.setAdapter(null);
                records.add(cor.getString(cor.getColumnIndex("name"))+""+cor.getString(cor.getColumnIndex("college")));
                adapter.notifyDataSetChanged();
            }catch (Exception e){
                LoadToList();
                Toast.makeText(getApplicationContext(),"Record not found",Toast.LENGTH_LONG).show();
            }
    }*/
    public void searchMemberBySurName(){

    }
    public void searchMemberByRole(){

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String firstName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
        String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
        Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
        String sportCategory = values.getAsString(MemberEntry.COLUMN_SPORT);

        if(firstName == null){
            throw new IllegalArgumentException("You have to input correct Name");
        } else if (lastName == null){
            throw new IllegalArgumentException("You have to input correct Surname");
        } else if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE))
            {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        else if(sportCategory == null){
            throw new IllegalArgumentException("You have to input correct Sport Category!");
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME,
                        null, values);
                if (id == -1) {
                    Log.e("insertMethod",
                            "Insertion of data in the table failed for "
                                    + uri);

                    getContext().getContentResolver().notifyChange(uri, null);
                    return null;
                }

                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion of data in " +
                        "the table failed for " + uri);

        }
    }
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case MEMBERS:
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, s, strings);
                break;
            case MEMBER_ID:
                s = MemberEntry._ID + "=?";
                strings = new String[]
                    {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't delete incorrect URI" + uri);
        }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        String firstName = contentValues.getAsString(MemberEntry.COLUMN_FIRST_NAME);
        String lastName = contentValues.getAsString(MemberEntry.COLUMN_LAST_NAME);
        Integer gender = contentValues.getAsInteger(MemberEntry.COLUMN_GENDER);
        String sportCategory = contentValues.getAsString(MemberEntry.COLUMN_SPORT);

        if(contentValues.containsKey(MemberEntry.COLUMN_FIRST_NAME)){//Если contentValues содержит firstName,то тогда идет вот эта проверка ⬇
            if(firstName == null){
                throw new IllegalArgumentException("You have to input correct Name");
            }
        } else if (contentValues.containsKey(MemberEntry.COLUMN_LAST_NAME)){
            if (lastName == null){
                throw new IllegalArgumentException("You have to input correct Surname");
            }
        } else if (contentValues.containsKey(MemberEntry.COLUMN_GENDER)){
            if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE))
            {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        } else if (contentValues.containsKey(MemberEntry.COLUMN_SPORT)){
            if(sportCategory == null){
                throw new IllegalArgumentException("You have to input correct Sport Category!");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MEMBERS:
                rowsUpdated = db.update(MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;

            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                s = MemberEntry._ID + "=?";
                strings = new String[]
                        {String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;

            default:
                throw new IllegalArgumentException("Can't update incorrect URI "
                        + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBER_ID:
                return MemberEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Can't getType incorrect URI " + uri);
        }
    }
}
