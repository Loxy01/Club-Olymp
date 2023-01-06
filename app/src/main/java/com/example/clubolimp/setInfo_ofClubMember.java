package com.example.clubolimp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubolimp.data.ClubOlimpContract.*;

import java.util.ArrayList;
import java.util.List;

public class setInfo_ofClubMember extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText FirstName;
    EditText SurName;
    EditText Sport;

    Spinner Gender;
    int intGender;
    ArrayAdapter<String> arrayAdapter;
    List<String> genders;

    Button addMember;

    private static final int EDIT_MEMBERS_LOADER=111;
    Uri currentMemberUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info);

        FirstName = findViewById(R.id.firstName);
        SurName = findViewById(R.id.surName);
        Sport = findViewById(R.id.sport);

        Gender = findViewById(R.id.gender); //this var is spinner type

        addMember = findViewById(R.id.AddMemberButton); //this var is button type

        Intent intent = getIntent();
        /** Тут у нас идет получение данных через intent
         *  Присваивание значение uri через getData (setData требует данных uri)
         *  Далее смотрим, если данные не получены(null),то значит эта активность запустилась через кнопку
         *  А если данные есть(not null|| uri), то тогда меняем заголовок активности на "Edit"
         */
        currentMemberUri = intent.getData();

        if(currentMemberUri == null){
            setTitle("Add Member");
            addMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String firstNameString = FirstName.getText().toString();
                    String SurNameString = SurName.getText().toString();
                    String SportString = Sport.getText().toString().trim();

                    int firstNameLength = firstNameString.length();
                    int SurNameLength = SurNameString.length();

                    if(firstNameString.isEmpty() || SurNameString.isEmpty() || SportString.isEmpty()){
                        Toast.makeText(setInfo_ofClubMember.this, "Not all text fields are filled!", Toast.LENGTH_SHORT).show();
                    } else if (firstNameLength < 2 || SurNameLength < 2) {
                        Toast.makeText(setInfo_ofClubMember.this, "Incorrect text entry!", Toast.LENGTH_SHORT).show();
                    } else if (!(FirstName.getText().toString().matches("[a-zA-Z]+") || SurName.getText().toString().matches("[a-zA-Z]+"))) {
                        Toast.makeText(setInfo_ofClubMember.this, "Whitespaces and Numbers are not allowed!", Toast.LENGTH_SHORT).show();
                    }
                    //Ограничение вводимых данных ⬇
                    else if
                    (!(
                        SportString.equals("Goaltender")
                        || SportString.equals("Left Defenseman")
                        || SportString.equals("Left defenseman")
                        || SportString.equals("Right Defenseman")
                        || SportString.equals("Right defenseman")
                        || SportString.equals("Left Defense")
                        || SportString.equals("Left defense")
                        || SportString.equals("Right Defense")
                        || SportString.equals("Right defense")
                        || SportString.equals("Left Winger")
                        || SportString.equals("Left winger")
                        || SportString.equals("Right Winger")
                        || SportString.equals("Right winger")
                        || SportString.equals("Center")
                        || SportString.equals("Center forward")
                        || SportString.equals("Center Forward")
                        || SportString.equals("Centre")
                        || SportString.equals("Centre forward")
                        || SportString.equals("Centre Forward")
                        || SportString.equals("Coach")
                        || SportString.equals("Cheerleader")
                    ))
                    {
                        Toast.makeText(setInfo_ofClubMember.this, "Error", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(setInfo_ofClubMember.this);
                        builder.setTitle("List of roles in hockey:")
                                .setMessage("Goaltender\n"+"Left Defenseman\n"+"Right Defenseman\n"+"Left Winger\n"+"Right Winger\n"+"Center forward\n"+"Coach\n"+"Cheerleader")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Закрываем диалоговое окно
                                        dialog.cancel();
                                    }
                                });
                        builder.show();
                    }
                    else {
                        insertMembers();
                    }
                }
            });
        } else{
            setTitle("Edit the Member");
            addMember.setText("Save edited data", TextView.BufferType.EDITABLE);
            addMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertMembers();
                }
            });
            getSupportLoaderManager().initLoader(EDIT_MEMBERS_LOADER, null, this);
        }

        genders = new ArrayList<String>();
            genders.add("Male");
            genders.add("Female");

        arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genders);
        Gender.setAdapter(arrayAdapter);

        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGender = (String) adapterView.getItemAtPosition(i);
                if(!TextUtils.isEmpty(selectedGender)){
                    if(selectedGender.equals("Male")){
                        intGender = MemberEntry.GENDER_MALE;
                    } else if (selectedGender.equals("Female")){
                        intGender = MemberEntry.GENDER_FEMALE;
                    } else{intGender = MemberEntry.GENDER_UNKNOWN;}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                intGender = 0;
            }
        });
    }

    private void insertMembers(){
        String name = FirstName.getText().toString().trim();
        String surName = SurName.getText().toString().trim();
        String sport = Sport.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(MemberEntry.COLUMN_FIRST_NAME, name);
        values.put(MemberEntry.COLUMN_LAST_NAME, surName);
        values.put(MemberEntry.COLUMN_SPORT, sport);
        values.put(MemberEntry.COLUMN_GENDER, intGender);
        /**
         Класс ContentResolver предоставляет доступ к таблице нашей по Uri
         Поскольку метод .insert выходец класса Uri,
         То можем присвоить Uri uri = getContentResolver(). и тут какой-нибудь метод реализовать
        */

        if (currentMemberUri == null){
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI,
                    values);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Data saved", Toast.LENGTH_LONG).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(currentMemberUri, values, null, null);
            if(rowsChanged == 0){
                Toast.makeText(this,
                        "Saving of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member Updated", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**Все, что ниже это код для отображения наших данных в Активности редактирования*/

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_GENDER,
                MemberEntry.COLUMN_SPORT
        };
        CursorLoader cursorLoader2 = new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null);
        return cursorLoader2;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.moveToNext()){
            int firstNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
            int surNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
            int genderColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_GENDER);
            int sportColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_SPORT);

            String sFirstName = data.getString(firstNameColumnIndex);
            String sSurName = data.getString(surNameColumnIndex);
            int iGender = data.getInt(genderColumnIndex);
            String sSportRole = data.getString(sportColumnIndex);

            FirstName.setText(sFirstName);
            SurName.setText(sSurName);
            switch (iGender){
                case MemberEntry.GENDER_MALE:
                    Gender.setSelection(0);
                    break;
                case MemberEntry.GENDER_FEMALE:
                    Gender.setSelection(1);
                    break;
            }
            Sport.setText(sSportRole);
        }
        if (data.isLast()){
            data.close();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    public void deleteMember(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);

        AlertDialog dialog = builder//Тут идет действие с диалогом
                .setView(view).show();

        Button CancelButton = view.findViewById(R.id.materialButtonCancel);
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button OkButton = view.findViewById(R.id.materialButtonOk);
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss();
                int rowsDeleted = getContentResolver().delete(currentMemberUri, null, null);
                if (rowsDeleted == 0){
                    Toast.makeText(setInfo_ofClubMember.this, "Deleting an entry from the table failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(setInfo_ofClubMember.this, "Member deleted", Toast.LENGTH_SHORT).show();
                    Intent toTableActivity = new Intent(setInfo_ofClubMember.this, TableMembers.class);
                    startActivity(toTableActivity);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(currentMemberUri != null){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.delete_member, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}