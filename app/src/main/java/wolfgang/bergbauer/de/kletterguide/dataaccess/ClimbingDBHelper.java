package wolfgang.bergbauer.de.kletterguide.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import wolfgang.bergbauer.de.kletterguide.R;

/**
 * This class is an implementation of {@link android.database.sqlite.SQLiteOpenHelper}
 * It manages the creation, update and deletion of the whole database.
 */
public class ClimbingDBHelper extends SQLiteOpenHelper {

    /**
     * Database name
     */
    public static final String DATABASE_NAME = "ClimbingDBHelper.db";

    /**
     * The database Version. Increment this number if changes to the database have been done and
     * you need to update the users database.
     */
    public static final int DATABASE_VERSION = 1;

    /** The name and column index of each column in your database. These should be descriptive.*/
    public static final String TABLE_CLIMBINGAREAS = "ClimbingAreas";
    public static final String TABLE_ROUTES = "Routes";

    public static final String COLUMN_CLIMBINGAREAS_ID = "id";
    public static final String COLUMN_CLIMBINGAREAS_NAME = "name";
    public static final String COLUMN_CLIMBINGAREAS_RANKING = "ranking";
    public static final String COLUMN_CLIMBINGAREAS_LATITUDE = "latitude";
    public static final String COLUMN_CLIMBINGAREAS_LONGITUDE = "longitude";
    public static final String COLUMN_CLIMBINGAREAS_TYPE = "type";
    public static final String COLUMN_CLIMBINGAREAS_DESCRIPTION = "description";
    public static final String COLUMN_CLIMBINGAREAS_IMAGE_URL = "imageurl";

    public static final String COLUMN_ROUTES_ID = "id";
    public static final String COLUMN_ROUTES_NAME = "name";
    public static final String COLUMN_ROUTES_UIAA = "uiaa";
    public static final String COLUMN_ROUTES_RANKING = "ranking";
    public static final String COLUMN_ROUTES_LATITUDE = "latitude";
    public static final String COLUMN_ROUTES_LONGITUDE = "longitude";
    public static final String COLUMN_ROUTES_IMAGE_URL = "imageurl";
    public static final String COLUMN_ROUTES_ROUTE_ACCOMPLISHED = "routeAccomplished";
    public static final String COLUMN_ROUTES_LEADING_CLIMB_ACCOMPLISHED = "leadingClimbAccomplished";
    public static final String COLUMN_ROUTES_FOLLOW_CLIMB_ACCOMPLISHED = "followClimbAccomplished";
    public static final String COLUMN_ROUTES_CLIMBINGAREA_ID = "climbingarea";

    private Context context;

    /**
     * Constructor. This constructor should not be called manually. It will be automatically created
     * on first database access.
     * @param context the current Context
     * @param name the name of the database
     * @param factory the {@link android.database.sqlite.SQLiteDatabase.CursorFactory}
     * @param version the database version
     */
    public ClimbingDBHelper(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Constructor to init the database
     * @param context the current Context
     */
    public ClimbingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Called when no databse exists in disk and the helper class needs to create a new one.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //init db with SQL Files
        try {
            insertFromFile(db, R.raw.ddl);
            insertFromFile(db, R.raw.dml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method clears the whole database and recreates it.
     * @param db the db which should be cleared
     */
    public void clearDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + TABLE_ROUTES);
        db.execSQL("DROP TABLE " + TABLE_CLIMBINGAREAS);
        onCreate(db);
    }

    /**
     * Called when there is a database version mismatch meaning that
     the version of the database on disk needs to be upgraded to
     the current version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " +
                newVersion + ", which will destroy all old data");

        // Upgrade the existing de.bergbauer.wolfgang.ma.Stringare.de.bergbauer.wolfgang.passaubus.database to conform to the new
        // version. Multiple previous versions can be handled by
        // comparing oldVersion and newVersion values.

        // The simplest case is to drop the old table and create a new one.
        db.execSQL("DROP TABLE " + TABLE_ROUTES);
        db.execSQL("DROP TABLE " + TABLE_CLIMBINGAREAS);

        // Create a new one.
        onCreate(db);
    }

    /**
     * This reads a file from the given Resource-Id and calls every line of it as a SQL-Statement
     *
     * @param db the db where the statements should be executed on
     *
     * @param resourceId of the sql file
     *  e.g. R.raw.food_db
     *
     * @return Number of SQL-Statements run
     * @throws IOException
     */
    public int insertFromFile(SQLiteDatabase db, int resourceId) throws IOException {
        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            if (insertStmt != null) {
                db.execSQL(insertStmt);
                result++;
            }
        }
        insertReader.close();
        // returning number of inserted rows
        return result;
    }

}