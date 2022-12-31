package com.example.clubolimp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubolimp.data.ClubOlimpContract;
import com.example.clubolimp.data.ClubOlimpContract.*;
import com.example.clubolimp.data.MembersCursorAdapter;

import java.util.ArrayList;

public class TableMembers extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEMBERS_LOADER = 123;
    MembersCursorAdapter membersCursorAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Table of Members");
        setContentView(R.layout.activity_table_members);
        lvItems = (ListView) findViewById(R.id.lvItems);

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
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {  Поиск участника я осуществулю уже в 2023г.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_member, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void searchMember(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TableMembers.this);
        LayoutInflater inflater = TableMembers.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_search, null);
        builder.setView(view).show();
    }*/

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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) { // Когда обработка данных завершена
        membersCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader){
        //Чтобы удалить невалидные(некорректные) объекты,то есть объекты которых например удалили из БД и прочее..
        membersCursorAdapter.swapCursor(null);
    }
}