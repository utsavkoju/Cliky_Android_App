package ca.cliky.cliky.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class User_Offers extends DBHandler{
    private static final String TAG = "database.offers";
    public User_Offers(Context context) {super(context);}

    public boolean User_OffersExist(){
        int count = 0;
        String select = "SELECT * FROM "+TABLE_USER_OFFERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        count = cursor.getCount();
        db.close();
        if(count == 0) { return false; }
        return true;
    }

    public void saveUserOffers(String user_id, String offer_id, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(KEY_USER_ID, user_id);
        val.put(KEY_OFFER_ID, offer_id);
        val.put(KEY_DATE, date);

        db.insert(TABLE_USER_OFFERS, null, val);

        db.close();
        Log.i(TAG, "Stored Successfully");
    }
}
