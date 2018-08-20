package ca.cliky.cliky.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.cliky.cliky.objects.Retailer;

public class Retailers extends DBHandler{
    private static final String TAG = "database.Retailers";
    public Retailers(Context context) {super(context);}

    public boolean RetailersExist() {
        int count = 0;
        String select = "SELECT * FROM "+TABLE_RETAILER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        count = cursor.getCount();
        db.close();
        if(count == 0) { return false; }
        return true;
    }

    public void saveRetailers(String id, String name, String url, String logo, String offers)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(KEY_ID, id);
        val.put(KEY_NAME, name);
        val.put(KEY_URL, url);
        val.put(KEY_LOGO, logo);
        val.put(KEY_NOS_OFFERS, offers);

        db.insert(TABLE_RETAILER, null, val);

        db.close();
        Log.i(TAG, "Stored Successfully");
    }

    public HashMap<String, String> getRetailersDetails(){
        HashMap<String,String> retailer = new HashMap<String,String>();
        String selectQuery = "SELECT id,name,url,logo,offers FROM " + TABLE_RETAILER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            retailer.put("name", cursor.getString(1));
            retailer.put("url", cursor.getString(2));
            retailer.put("logo", cursor.getString(3));
            retailer.put("offers", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return retailer
        return retailer;
    }

    public ArrayList<String> getRetailer() {
        ArrayList<String> retailer = new ArrayList<>();
        String select = "SELECT name, url, logo, offers FROM " + TABLE_RETAILER;
        Log.e("Query: ",select);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if(cursor.getColumnCount() == 1) {
            cursor.moveToFirst();
            retailer.add(0, cursor.getString(0));
            retailer.add(1, cursor.getString(1));
            retailer.add(2,cursor.getString(2));
            retailer.add(3,cursor.getString(3));
        }
        cursor.close();
        db.close();
        return retailer;
    }

    public List<Retailer> getRetails() {
        List<Retailer> retailer = new ArrayList<Retailer>();
        String Query = "SELECT id, name FROM " + TABLE_RETAILER+ " ORDER BY name";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        retailer.add(new Retailer("", "Select Retailer"));
        if (cursor.moveToFirst()) {
            do {
                retailer.add(new Retailer(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        Log.e(TAG, String.valueOf(retailer));
        return retailer;
    }

    public void deleteRetailers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RETAILER);
        db.close();
        Log.i(TAG,"DELETED");
    }
}
