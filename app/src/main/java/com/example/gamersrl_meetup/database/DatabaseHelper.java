package com.example.gamersrl_meetup.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper abstract class
 *
 * Provides a template for intermediaries between the app and the database.
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper
{
    // Set up the Log tag for the Game class
    private final String LOG_TAG = "GAME - ";

    // Set the database name and version
    private static final String DATABASE_NAME = "gamers_irl_database";
    private static final int DATABASE_VERSION = 1;

    /**
     * Abstract method to create the query to make the pertinent table [8] [9] [10].
     *
     * @return The query to create the pertinent table
     */
    public abstract String createTable();

    /**
     * Constructor to create the database helper, with the database name and version.
     *
     * @param context The context to make the database helper in
     */
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Execute the query from createProductsTable() to create the Products table in the database.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createTable());
    }

    /**
     * Upgrade the database to version 2 if it is still on version 1.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /**
         * If the old version is less than 2, then upgrade it to version 2.
         * Otherwise, do nothing.
         */
        if (oldVersion < 2)
        {
            // TODO - Update to the database to version 2
        }
    }

    /**
     * Determine whether the table is empty or not.
     *
     * @return Whether the table is empty or not - TRUE is empty; FALSE is populated
     */
    public boolean isTableEmpty(String in_TableName)
    {
        // Initialize isEmpty - database is initially determined to be empty before checking
        boolean isEmpty = true;

        // Get the database so it can be accessed/written to
        SQLiteDatabase db = getWritableDatabase();

        // Retrieve the count of the rows in the table via SQL
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + in_TableName, null);

        /**
         * If the cursor is populated with the count of rows, then verify that more than 0 rows were retrieved.
         * Otherwise, return that the database is empty.
         */
        if (cursor != null)
        {
            // Move the cursor to the first row in the table
            cursor.moveToFirst();
            // Get contents of the column - If something is there, the result will be greater than 0
            int count = cursor.getInt(0);

            /**
             * If the first column of the first row is populated (count is more than 0), then indicate that the database is populated.
             * Otherwise, do nothing (the database is empty)
             */
            if (count > 0)
            {
                isEmpty = false;
            }

            // Close the cursor connection
            cursor.close();
        }

        // Return whether the database is empty or not - TRUE is empty; FALSE is populated
        return isEmpty;
    }

    /**
     * Abstract method to populate the database with some pre-determined data [8] [9] [10].
     */
    public abstract void populateDatabase();
}