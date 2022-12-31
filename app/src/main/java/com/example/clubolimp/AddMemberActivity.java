package com.example.clubolimp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.clubolimp.data.ClubOlimpContract.MemberEntry;
import com.example.clubolimp.data.MembersCursorAdapter;

public class AddMemberActivity extends AppCompatActivity {

    Button openTableMembers;

    private static final int MEMBER_LOADER = 123;
    MembersCursorAdapter membersCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openTableMembers = findViewById(R.id.openTableMembers);

        openTableMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddMemberActivity.this, TableMembers.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_member, menu);
        return true;
    }

    public void AddMember(MenuItem item) {
        Intent toNextActivity = new Intent(AddMemberActivity.this, setInfo_ofClubMember.class);
        startActivity(toNextActivity);
    }



    /*private void displayData() {

        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_GENDER,
                MemberEntry.COLUMN_SPORT
        };

        Cursor cursor = getContentResolver().query(
                MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        members.setText("All members\n\n");
        members.append(MemberEntry._ID + " " +
                MemberEntry.COLUMN_FIRST_NAME + " " +
                MemberEntry.COLUMN_LAST_NAME + " " +
                MemberEntry.COLUMN_GENDER + " " +
                MemberEntry.COLUMN_SPORT
        );

        int idIndex = cursor.getColumnIndex(MemberEntry._ID);
        int idFirstName = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
        int idLastName = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
        int idGender = cursor.getColumnIndex(MemberEntry.COLUMN_GENDER);
        int idSport = cursor.getColumnIndex(MemberEntry.COLUMN_SPORT);

        while (cursor.moveToNext()) {
            int currentId = cursor.getInt(idIndex);
            String currentFirstName = cursor.getString(idFirstName);
            String currentLastName = cursor.getString(idLastName);
            int currentGender = cursor.getInt(idGender);
            String currentSport = cursor.getString(idSport);

            members.append("\n" +
                    currentId + " " +
                    currentFirstName + " " +
                    currentLastName + " " +
                    currentGender + " " +
                    currentSport
            );
        }

        cursor.close();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }*/
}