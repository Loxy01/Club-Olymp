package com.example.clubolimp.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.clubolimp.R;
import com.example.clubolimp.data.ClubOlimpContract.MemberEntry;

public class MembersCursorAdapter extends CursorAdapter {
    public MembersCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // Метод newView используется для наполнения нового layout layout_cycle_for_items
    // Но здесь пока данные не передаются
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.layout_cycle_for_items, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Связываем TextView c нашими items в layout_cycle_for_items:
        TextView name = (TextView) view.findViewById(R.id.cycleName);
        TextView surname = (TextView) view.findViewById(R.id.cycleSurName);
        TextView categoryOfSport = (TextView) view.findViewById(R.id.cycleCategorySport);

        // Создем индекс по которому потом будем обращаться к cursor
        int FirstNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
        int LastNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
        int SportIndex = cursor.getColumnIndex(MemberEntry.COLUMN_SPORT);

        // Извлекаем значения из cursor:
        String sName = cursor.getString(FirstNameIndex);
        String sSurName = cursor.getString(LastNameIndex);
        String sCategoryOfSport = cursor.getString(SportIndex);

        // Заполняем поля извлеченными выше значениями
        name.setText(sName);
        surname.setText(sSurName);
        categoryOfSport.setText(sCategoryOfSport);
    }
}
