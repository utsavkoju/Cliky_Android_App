package ca.cliky.cliky.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "cliky";

    protected static final String TABLE_USER = "tb_user";
    protected static final String TABLE_RETAILER = "tb_retailer";
    protected static final String TABLE_DEPARTMENT = "tb_department";
    protected static final String TABLE_TYPE = "tb_type";
    protected static final String TABLE_USER_OFFERS = "tb_user_offers";

    protected static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CREDENTIALS = "credentials";
    public static final String KEY_STREET_ADDRESS = "street_address";
    public static final String KEY_CITY = "city";
    public static final String KEY_ZIP_CODE = "zip_code";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_URL = "url";
    public static final String KEY_LOGO = "logo";
    public static final String KEY_NOS_OFFERS = "offers";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_OFFER_ID = "offer_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_SYNC_DATE = "sync_date";


    private DBHandler dbHandler;
    private SQLiteHelper sqLiteHelper;
    private Context context;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_FIRST_NAME + " TEXT, "
                + KEY_LAST_NAME + " TEXT, "
                + KEY_EMAIL + " TEXT, "
                + KEY_CREDENTIALS + " TEXT, "
                + KEY_STREET_ADDRESS + " TEXT, "
                + KEY_ZIP_CODE + " TEXT, "
                + KEY_CITY + " TEXT, "
                + KEY_COUNTRY + " TEXT, "
                + KEY_PHONE + " TEXT )";

        String CREATE_TABLE_RETAILER = "CREATE TABLE " + TABLE_RETAILER + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_URL + " TEXT, "
                + KEY_LOGO + " TEXT, "
                + KEY_NOS_OFFERS + " TEXT) ";

        String CREATE_TABLE_DEPARTMENT = "CREATE TABLE "+TABLE_DEPARTMENT + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT)";

        String CREATE_TABLE_TYPE = "CREATE TABLE "+TABLE_TYPE+" ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT)";

        String CREATE_USER_OFFER = "CREATE TABLE " + TABLE_USER_OFFERS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_USER_ID + " TEXT, "
                + KEY_OFFER_ID + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_SYNC_DATE + " TEXT ) ";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TABLE_RETAILER);
        db.execSQL(CREATE_USER_OFFER);
        db.execSQL(CREATE_TABLE_DEPARTMENT);
        db.execSQL(CREATE_TABLE_TYPE);

    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_RETAILER);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_USER_OFFERS);
            db.execSQL("DROP TABLE IF EXIST" + TABLE_DEPARTMENT);
            db.execSQL("DROP TABLE IF EXIST" + TABLE_TYPE);
            onCreate(db);
        }

        public DBHandler open() throws SQLException {
            this.getWritableDatabase();
            return this;
        }


        public void close() {
            if (dbHandler != null)
                dbHandler.close();
        }

    //SQLiteHelper
    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }
}
