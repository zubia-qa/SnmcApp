package azure.snmc;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2016-11-29.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static final int DATABASE_VERSION = 29;//TODO increment with new database release for prayers
    private static String DB_NAME = "snmc4.sqlite";
    private static final String TABLE_Name = "Prayertimetable";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


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
            Log.e("Path 2", DB_PATH);
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
            this.getWritableDatabase();//todo changed this from readable  on 21st May 2018

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }


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

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        int ver = myDataBase.getVersion();
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        myDataBase.execSQL(DATABASE_CREATE_Prayertimetable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        openDataBase();
        if (newVersion > oldVersion)

            try {
//                myContext.deleteDatabase(DB_NAME);

                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    public List<String> getAllPrayersList() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DbHelper.TABLE_Name + " WHERE Date <= DATE('now','localtime') ORDER BY _id DESC  LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<String> mArrayList = new ArrayList<String>();

//    if (mArrayList != null && !mArrayList.isEmpty()) {

        if (cursor.moveToFirst()) {

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

        cursor.close();
        db.close();
        return mArrayList;
    }


}
