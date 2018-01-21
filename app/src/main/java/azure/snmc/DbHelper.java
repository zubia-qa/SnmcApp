package azure.snmc;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by acer on 2016-11-29.
 */
public class DbHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "";
    private static final int DATABASE_VERSION = 11;
    //        private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";
    private static String DB_NAME = "snmc4.sqlite";
    static final String TABLE_Name = "Prayertimetable";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private ArrayList<String> prayers;


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DbHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);// 1? its Database Version


        if (android.os.Build.VERSION.SDK_INT >= 4.2) {
            this.DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            Log.e("Path 2", DB_PATH);//de
        } else {
            this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            Log.e("Path 1", DB_PATH);
        }
        this.myContext = context;

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        SQLiteDatabase db_Read = null;

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
//    private boolean checkDataBase() {
//
//        SQLiteDatabase checkDB = null;
//
//        try {
//            String myPath = DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);//TODO E/SQLiteDatabase﹕ Failed to open database
//
//        } catch (SQLiteException e) {
//
//            //database does't exist yet.
//        }
//
//        if (checkDB != null) {
//
//            checkDB.close();
//
//        }
//
//        return checkDB != null ? true : false;
//    }
    private boolean checkDataBase() {
        File databasePath = myContext.getDatabasePath(DB_NAME);
        return databasePath.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

//        try {
//            myDataBase = this.getWritableDatabase();
//        } catch (SQLiteException ex) {
//            Toast.makeText(myContext, "DB with filename " + DB_NAME + "coudn't be opend!", Toast.LENGTH_SHORT);
//        }ry {
//            myDataBase = this.getWritableDatabase();
//        } catch (SQLiteException ex) {
//            Toast.makeText(myContext, "DB with filename " + DB_NAME + "coudn't be opend!", Toast.LENGTH_SHORT);
//        }
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        openDataBase();
        if (newVersion > oldVersion)

            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.


    //    public Cursor query (String table, String[] columns, String selection, String[] selctionsArgs, String groupBy, String having, String orderBy){
//        return myDataBase.query(TABLE_Name, null, null, null, null, null, null);//no such table name TODO
//    }
    public List<String> getAllPrayersList() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DbHelper.TABLE_Name + " WHERE Date <= DATE('now') ORDER BY _id DESC  LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<String> mArrayList = new ArrayList<String>();

//    if (mArrayList != null && !mArrayList.isEmpty()) {

        if (cursor.moveToFirst()) {
//        for(int i=0; i<cursor.getColumnCount();i++)
//        {
//            mArrayList.add(cursor.getString(cursor.getColumnIndex(String.valueOf(i))));}

//        for(int i = 0;i<mArrayList.size();i++)
//        {
//            if(mArrayList.get(i).getName().equals(acTextView.getText().toString()))
//                catids=list.get(position).getId();}

            mArrayList.add(cursor.getString(cursor.getColumnIndex("Date")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("FajrA")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("FajrI")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("ZuhrA")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("ZuhrI")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("AsarA")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("AsarI")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("MeghribA")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("MegribI"))); //spelling
            mArrayList.add(cursor.getString(cursor.getColumnIndex("IshaA")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("IshaI")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("Jumah1")));
            mArrayList.add(cursor.getString(cursor.getColumnIndex("Jumah2")));
        }

        db.close();
        return mArrayList;
    }


//    public ArrayList<HashMap<String, String>> getAllPrayers() {
//
////        prayers = new ArrayList<String>();
//        SQLiteDatabase db = this.getReadableDatabase();
////        SQLiteDatabase db = this.getWritableDatabase();
//
//        String selectQuery = "SELECT * FROM "+DbHelper.TABLE_Name+" WHERE Date <= date('now') ORDER BY _id DESC  LIMIT 1";
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        ArrayList<HashMap<String, String>> prayers = new ArrayList<HashMap<String, String>>();
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                HashMap<String, String> map = new HashMap<String, String>();
//                for(int i=0; i<cursor.getColumnCount();i++)
//                {
//                    map.put(cursor.getColumnName(i), cursor.getString(i));
//                }
//
//                prayers.add(map);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        // return contact list
//        return prayers;
//
//        // closing connection
//    }

}
