package com.example.clubolimp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.clubolimp.data.ClubOlimpContract.*;
import com.example.clubolimp.data.MembersCursorAdapter;
import com.example.clubolimp.data.OlimpDbHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class TableMembers extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    OlimpDbHelper dbOpenHelper;
    private static final int MEMBERS_LOADER = 123;
    MembersCursorAdapter membersCursorAdapter;
    ListView lvItems;
    SearchView sv;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpenHelper = new OlimpDbHelper(TableMembers.this);
        setTitle("Table of Members");
        setContentView(R.layout.activity_table_members);
        lvItems = findViewById(R.id.lvItems);
        sv = findViewById(R.id.searchViewID);

        db = dbOpenHelper.getReadableDatabase();

        membersCursorAdapter = new MembersCursorAdapter(this, null);
        lvItems.setAdapter(membersCursorAdapter);

        getSupportLoaderManager().initLoader(MEMBERS_LOADER, null, this);// инициализация CursorLoader
    }

    @Override
    protected void onStart() {
        super.onStart();
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TableMembers.this, setInfo_ofClubMember.class);
                Uri currentMemberUri = ContentUris.withAppendedId(MemberEntry.CONTENT_URI, l);
                intent.setData(currentMemberUri);
                startActivity(intent);
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Cursor cor4 = null;

                //Search by Name and SurName
                try{

                    //This is the code for separating words in String ⬇
                    String[] words;
                    String i1 = null;
                    String i2 = null;
                    if((StringUtils.containsWhitespace(s))){ //StringUtils is an apache class, so it needs to be imported into build.gradle
                        words = s.split("\\W+");
                        ArrayList<String> arrayListWord = new ArrayList<>(Arrays.asList(words));
                        i1 = arrayListWord.get(0);
                        i2 = arrayListWord.get(1);
                    }
                    cor4 = db.rawQuery("SELECT*FROM members where firstName='"+i1+"' and lastName='"+i2+"'", null);
                    cor4.moveToLast();
                    if (cor4 != null && cor4.moveToFirst()) {
                        int firstNameIndex = cor4.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
                        int lastNameIndex = cor4.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
                        String firstName = cor4.getString(firstNameIndex);
                        String lastName = cor4.getString(lastNameIndex);
                        if(firstName.equals(i1)&&lastName.equals(i2)){
                            membersCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                @Override
                                public Cursor runQuery(CharSequence charSequence) {
                                    return db.rawQuery("SELECT*FROM members where firstName='"+firstName+"' and lastName='"+lastName+"'", null);
                                }
                            });
                        } else { getSupportLoaderManager().restartLoader(MEMBERS_LOADER, null, TableMembers.this); }
                        // This is the launch of the filter
                        membersCursorAdapter.getFilter().filter(s);
                        membersCursorAdapter.notifyDataSetChanged();
                    }
                }finally {
                    if(cor4!=null){
                        cor4.close();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cor = null;
                Cursor cor2 = null;
                Cursor cor3 = null;

                //Search by name
                try {
                    cor = db.rawQuery("SELECT * FROM members WHERE firstName='"+ s +"'", null);
                    cor.moveToNext();
                    if( cor != null && cor.moveToFirst()) {
                        int firstNameIndex = cor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
                        String firstName = cor.getString(firstNameIndex);
                        if(firstName.equals(s)){
                            membersCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                @Override
                                public Cursor runQuery(CharSequence charSequence) {
                                    return db.rawQuery("SELECT * FROM members WHERE firstName='"+ charSequence +"'", null);
                                }
                            });
                        }else { getSupportLoaderManager().restartLoader(MEMBERS_LOADER, null, TableMembers.this); }
                        // This is the launch of the filter
                        membersCursorAdapter.getFilter().filter(s);
                        membersCursorAdapter.notifyDataSetChanged();
                    }
                } finally {
                    if(cor !=null){
                        cor.close();
                    }
                }

                //Search by surname
                try {
                    cor2 = db.rawQuery("SELECT * FROM members WHERE lastName='"+ s +"'", null);
                    cor2.moveToNext();
                    if(cor2!=null && cor2.moveToFirst()){
                        int surNameIndex = cor2.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
                        String surName = cor2.getString(surNameIndex);
                        if(surName.equals(s)){
                            membersCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                @Override
                                public Cursor runQuery(CharSequence charSequence) {
                                    return db.rawQuery("SELECT * FROM members WHERE lastName='" + charSequence + "'", null);
                                }
                            });
                        }else { getSupportLoaderManager().restartLoader(MEMBERS_LOADER, null, TableMembers.this); }
                        // This is the launch of the filter
                        membersCursorAdapter.getFilter().filter(s);
                        membersCursorAdapter.notifyDataSetChanged();
                    }
                } finally {
                    if(cor2 !=null){
                        cor2.close();
                    }
                }

                //Search by Role in Hockey
                try {
                    cor3 = db.rawQuery("SELECT * FROM members WHERE sport='"+ s +"'",null);
                    cor3.moveToNext();
                    if (cor3 != null && cor3.moveToFirst()){
                        int indexRoleInHockey = cor3.getColumnIndex(MemberEntry.COLUMN_SPORT);
                        String RoleInHockey = cor3.getString(indexRoleInHockey);
                        if (RoleInHockey.equals(s)){
                            membersCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                @Override
                                public Cursor runQuery(CharSequence charSequence) {
                                    return db.rawQuery("SELECT * FROM members WHERE sport='"+charSequence+"'", null);
                                }
                            });
                        }else { getSupportLoaderManager().restartLoader(MEMBERS_LOADER, null, TableMembers.this); }
                        // This is the launch of the filter
                        membersCursorAdapter.getFilter().filter(s);
                        membersCursorAdapter.notifyDataSetChanged();
                    }
                }finally {
                    if (cor3 != null){
                        cor3.close();
                    }
                }

                sv.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        getSupportLoaderManager().restartLoader(MEMBERS_LOADER, null, TableMembers.this);
                        return false;
                    }
                });
                if(s.equals("")){
                    getSupportLoaderManager().restartLoader(MEMBERS_LOADER, null, TableMembers.this);
                }
                return false;
                }
            });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) { // Фоновый поток обработки данных
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_SPORT
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        Intent newIntent = new Intent();
        newIntent.putExtra("projection", projection);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) { // When data processing is completed
        membersCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader){
        //To delete invalid (incorrect) objects, that is, objects of which, for example, were deleted from the database, etc.
        membersCursorAdapter.swapCursor(null);
    }
}