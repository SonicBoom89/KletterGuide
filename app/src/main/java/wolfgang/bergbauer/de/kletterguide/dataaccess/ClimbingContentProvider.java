package wolfgang.bergbauer.de.kletterguide.dataaccess;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * This class is an implementation of {@link android.content.ContentProvider}.
 * It manages the access to the app´s database and implements methods for insertions, deletions and updates.
 */
public class ClimbingContentProvider extends ContentProvider {

    /**
     * The content {@link android.net.Uri} for busstops
     */
    public static final Uri CLIMBINGAREAS_URI = Uri.parse("content://wolfgang.bergbauer.de.kletterguide/climbingareas");

    /**
     * The content {@link android.net.Uri} for routes
     */
    public static final Uri ROUTES_URI = Uri.parse("content://wolfgang.bergbauer.de.kletterguide/routes");


    /**
     * Defining a UriMatcher to determine if a request is for all elements or a single row
     * Create the constants used to differentiate between the different URI requests.
     */
    public static final int CLIMBINGAREAS_URI_ALLROWS = 0;
    public static final int CLIMBINGAREAS_URI_SINGLE_ROW = 1;
    public static final int ROUTES_URI_ALLROWS = 3;
    public static final int ROUTES_URI_SINGLE_ROW = 4;

    private static final UriMatcher uriMatcher;

    /**
     * Populate the UriMatcher object, where a URI ending in 'elements' will correspond to a
     * request for all items, and 'elements/[rowID]' represents a single row.
     */
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("wolfgang.bergbauer.de.kletterguide",
                "climbingareas", CLIMBINGAREAS_URI_ALLROWS);
        uriMatcher.addURI("wolfgang.bergbauer.de.kletterguide",
                "climbingareas/#", CLIMBINGAREAS_URI_SINGLE_ROW);
        uriMatcher.addURI("wolfgang.bergbauer.de.kletterguide",
                "routes", ROUTES_URI_ALLROWS);
        uriMatcher.addURI("wolfgang.bergbauer.de.kletterguide",
                "routes/#", ROUTES_URI_SINGLE_ROW);
    }

    private ClimbingDBHelper myOpenHelper;

    @Override
    public boolean onCreate() {
        // Construct the underlying database.
        // Defer opening the database until you need to perform
        // a query or transaction.
        myOpenHelper = new ClimbingDBHelper(getContext(),
                ClimbingDBHelper.DATABASE_NAME, null,
                ClimbingDBHelper.DATABASE_VERSION);

        return true;
    }

    /**
     * Implementing queries and transactions within a Content Provider
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Open the database.
        SQLiteDatabase db;
        try {
            db = myOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = myOpenHelper.getReadableDatabase();
        }

        // Replace these with valid SQL statements if necessary.
        String groupBy = null;
        String having = null;

        // Use an SQLite Query Builder to simplify constructing the
        // database query.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String rowID = null;
        // If this is a row query, limit the result set to the passed in row.
        switch (uriMatcher.match(uri)) {
            case CLIMBINGAREAS_URI_SINGLE_ROW :
                rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(ClimbingDBHelper.COLUMN_CLIMBINGAREAS_ID + "=" + rowID);
                // Specify the table on which to perform the query. This can
                // be a specific table or a join as required.
                queryBuilder.setTables(ClimbingDBHelper.TABLE_CLIMBINGAREAS);
                break;
            case CLIMBINGAREAS_URI_ALLROWS :
                queryBuilder.setTables(ClimbingDBHelper.TABLE_CLIMBINGAREAS);
                break;
            case ROUTES_URI_SINGLE_ROW :
                rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(ClimbingDBHelper.COLUMN_ROUTES_ID + "=" + rowID);
                queryBuilder.setTables(ClimbingDBHelper.TABLE_ROUTES);
                break;
            case ROUTES_URI_ALLROWS:
                queryBuilder.setTables(ClimbingDBHelper.TABLE_ROUTES);
                break;
            default: break;
        }

        // Execute the query.
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);

        // Return the result Cursor.
        return cursor;
    }

    /**
     * Returning a Content Provider MIME type
     */
    @Override
    public String getType(Uri uri) {
        // Return a string that identifies the MIME type
        // for a Content Provider URI
        switch (uriMatcher.match(uri)) {
            case CLIMBINGAREAS_URI_ALLROWS:
                return "vnd.android.cursor.dir/vnd.paad.elemental";
            case CLIMBINGAREAS_URI_SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.paad.elemental";
            case ROUTES_URI_ALLROWS:
                return "vnd.android.cursor.dir/vnd.paad.elemental";
            case ROUTES_URI_SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.paad.elemental";
            default:
                throw new IllegalArgumentException("Unsupported URI: " +
                        uri);
        }
    }

    /**
     * Content Provider transaction implementations
     */
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String table = null;
        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case CLIMBINGAREAS_URI_ALLROWS:
                table = ClimbingDBHelper.TABLE_CLIMBINGAREAS;
                break;
            case CLIMBINGAREAS_URI_SINGLE_ROW :
                table = ClimbingDBHelper.TABLE_CLIMBINGAREAS;
                String rowID = uri.getPathSegments().get(1);
                where = ClimbingDBHelper.COLUMN_CLIMBINGAREAS_ID + "=" + rowID
                        + (!TextUtils.isEmpty(where) ?
                        " AND (" + where + ')' : "");
                break;
            case ROUTES_URI_SINGLE_ROW:
                table = ClimbingDBHelper.TABLE_ROUTES;
                rowID = uri.getPathSegments().get(1);
                where = ClimbingDBHelper.COLUMN_ROUTES_ID + "=" + rowID
                        + (!TextUtils.isEmpty(where) ?
                        " AND (" + where + ')' : "");
            case ROUTES_URI_ALLROWS:
                table = ClimbingDBHelper.TABLE_ROUTES;
                break;
            default: break;
        }

        // To return the number of deleted items you must specify a where
        // clause. To delete all rows and return a value pass in "1".
        if (where == null)
            where = "1";

        // Perform the deletion.
        int deleteCount = db.delete(table,
                where, whereArgs);

        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the number of deleted items.
        return deleteCount;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        // To add empty rows to your database by passing in an empty
        // Content Values object you must use the null column hack
        // parameter to specify the name of the column that can be
        // set to null.
        String nullColumnHack = null;

        // Insert the values into the table
        try {
            String table = null;
            Uri returnUri = null;
            switch (uriMatcher.match(uri)) {
                case CLIMBINGAREAS_URI_ALLROWS:
                    table = ClimbingDBHelper.TABLE_CLIMBINGAREAS;
                    returnUri = CLIMBINGAREAS_URI;
                    break;
                case ROUTES_URI_ALLROWS:
                    table = ClimbingDBHelper.TABLE_ROUTES;
                    returnUri = ROUTES_URI;
                    break;
                default: break;
            }

            long id = db.insert(table, nullColumnHack, values);
            // Construct and return the URI of the newly inserted row.
            if (id > -1) {
                // Construct and return the URI of the newly inserted row.
                Uri insertedId = ContentUris.withAppendedId(returnUri, id);

                // Notify any observers of the change in the data set.
                getContext().getContentResolver().notifyChange(insertedId, null);

                return insertedId;
            }
            else
                return null;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Open a read / write database to support the transaction.
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String selectedTable = null;
        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case ROUTES_URI_SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                selection = ClimbingDBHelper.COLUMN_ROUTES_ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                selectedTable = ClimbingDBHelper.TABLE_ROUTES;
            case CLIMBINGAREAS_URI_SINGLE_ROW :
                rowID = uri.getPathSegments().get(1);
                selection = ClimbingDBHelper.COLUMN_CLIMBINGAREAS_ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                selectedTable = ClimbingDBHelper.TABLE_CLIMBINGAREAS;
            default: break;
        }

        // Perform the update.
        int updateCount = db.update(selectedTable,
                values, selection, selectionArgs);

        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

}
