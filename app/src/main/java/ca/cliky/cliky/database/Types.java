package ca.cliky.cliky.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.cliky.cliky.objects.Type;

public class Types extends DBHandler {

    private static final String TAG = "database.type";

    public Types(Context context) {super(context);}

    public boolean TypeExist() {
        int count = 0;
        String select = "SELECT * FROM "+TABLE_TYPE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        count = cursor.getCount();
        db.close();
        if(count == 0) { return false; }
        return true;
    }

    public void saveType(String id, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(KEY_ID, id);
        val.put(KEY_NAME, name);

        db.insert(TABLE_TYPE, null, val);

        db.close();
        Log.i(TAG, "Stored Successfully");
    }

    public HashMap<String, String> getType(){
        HashMap<String,String> retailer = new HashMap<String,String>();
        String selectQuery = "SELECT id,name FROM " + TABLE_TYPE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            retailer.put("name", cursor.getString(1));
        }
        cursor.close();
        db.close();
        // return retailer
        return retailer;
    }

    public ArrayList<String> getTypes() {
        ArrayList<String> department = new ArrayList<>();
        String select = "SELECT name FROM " + TABLE_TYPE;
        Log.e("Query: ",select);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if(cursor.getColumnCount() == 1) {
            cursor.moveToFirst();
            department.add(0, cursor.getString(0));
        }
        cursor.close();
        db.close();
        return department;
    }

    public List<Type> getTyps() {
        List<Type> types = new ArrayList<Type>();
        String Query = "SELECT id, name FROM " + TABLE_TYPE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        types.add(new Type("", "Select Update Type"));
        if (cursor.moveToFirst()) {
            do {
                types.add(new Type(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        Log.e(TAG, String.valueOf(types));
        return types;
    }

    public void deleteType() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TYPE);
        db.close();
        Log.i(TAG,"DELETED");
    }
}
