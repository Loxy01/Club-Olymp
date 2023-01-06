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

    // The newView method is used to fill the new layout layout_cycle_for_items
    // But no data is being transmitted here yet
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.layout_cycle_for_items, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Linking TextView with our items in layout_cycle_for_items:
        TextView name = (TextView) view.findViewById(R.id.cycleName);
        TextView surname = (TextView) view.findViewById(R.id.cycleSurName);
        TextView categoryOfSport = (TextView) view.findViewById(R.id.cycleCategorySport);

        // Creating an index by which we will then access the cursor
        int FirstNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
        int LastNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
        int SportIndex = cursor.getColumnIndex(MemberEntry.COLUMN_SPORT);

        // Extracting values from cursor:
        String sName = cursor.getString(FirstNameIndex);
        String sSurName = cursor.getString(LastNameIndex);
        String sCategoryOfSport = cursor.getString(SportIndex);

        // Fill in the fields with the values extracted above:
        name.setText(sName);
        surname.setText(sSurName);
        categoryOfSport.setText(sCategoryOfSport);
    }
}
