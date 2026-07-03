package com.example.gamersrl_meetup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DatabaseHelper_Game class
 *
 * Provides an intermediary between the app and the database for Games.
 * Specifies the Game type in place of the generic T type [24].
 */
public class DatabaseHelper_Game extends DatabaseHelper<Game>
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "GAME DATABASE HELPER - ";

    // Set the table name
    private static final String TABLE_NAME = "game";

    // Set the table columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DEVELOPER = "developer";
    private static final String KEY_PUBLISHER = "publisher";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MIN_PLAYERS = "min_players";
    private static final String KEY_MAX_PLAYERS = "max_players";
    // The picture is indicated via a URI so that the code can refer to its file location
    private static final String KEY_PICTURE_URI = "picture_uri";

    /**
     * Constructor to create the database helper, with the database name and version.
     *
     * @param context The context to make the database helper in
     */
    public DatabaseHelper_Game(Context context)
    {
        super(context);
    }

    /**
     * Create the query to make the pertinent table.
     *
     * @return The query to create the pertinent table
     */
    @Override
    public String createTable()
    {
        String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_TITLE + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_DEVELOPER + " TEXT, " +
                KEY_PUBLISHER + " TEXT, " +
                KEY_RELEASE_DATE + " DATETIME, " +
                KEY_MIN_PLAYERS + " INTEGER, " +
                KEY_MAX_PLAYERS + " INTEGER, " +
                KEY_PICTURE_URI + " TEXT" +
                ")";
        Log.d(LOG_TAG, "Created query to create table: " + QUERY_CREATE_TABLE);
        return QUERY_CREATE_TABLE;
    }

    /**
     * Execute the query from createTable() to create the table in the database.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        super.onCreate(db);
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
        super.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Determine whether the table is empty or not.
     *
     * @param in_TableName The name of the table to check
     * @return Whether the table is empty or not
     */
    @Override
    public boolean isTableEmpty(String in_TableName) {
        return super.isTableEmpty(in_TableName);
    }

    /**
     * Helper method to retrieve Games from the database.
     * The Game type is specified in place of the generic T type [24].
     *
     * @return The list of Games from the db
     */
    @Override
    public List<Game> getItemsFromDB(String in_Query, String[] in_SelectionArgsForQuery)
    {
        return super.getItemsFromDB(in_Query, in_SelectionArgsForQuery);
    }

    /**
     * Helper method to construct a Game from the db row.
     * The Game type is specified in place of the generic T type [24].
     *
     * @param in_Cursor Used to iterate through the table
     * @return a Game from the db
     */
    @Override
    public Game constructItemFromDBRow(Cursor in_Cursor)
    {
        Log.d(LOG_TAG, "Constructing the Game from the db row");
        return new Game(
                in_Cursor.getInt(0), // Get id from current table row
                in_Cursor.getString(1), // Get title from current table row
                in_Cursor.getString(2), // Get description from current table row
                in_Cursor.getString(3), // Get developer from current table row
                in_Cursor.getString(4), // Get publisher from current table row
                // Read the data for release date from the current table row as a Long and convert to a Date to improve performance [5] [6]
                new Date(in_Cursor.getLong(5)),
                in_Cursor.getInt(6), // Get max players from current table row
                in_Cursor.getInt(7), // Get min players from current table row
                in_Cursor.getInt(8) // Get picture URI from current table row
        );
    }

    /**
     * Retrieve all Games from the database.
     *
     * @return All Games from the database
     */
    public List<Game> getAllGames()
    {
        // Make the query to get all data
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Log.d(LOG_TAG, "Created query to get all games: " + selectQuery);

        return getItemsFromDB(selectQuery, null);
    }

//    /**
//     * Add a new Game to the database [12].
//     *
//     * @param game The Game to add to the database
//     */
//    public void addToDatabase(Game game)
//    {
//        // Get the database so it can be accessed/written to
//        SQLiteDatabase db = getWritableDatabase();
//
//        // Initialize the ContentValues
//        ContentValues values = new ContentValues();
//
//        /** Set the data for the new Game */
//        values.put(KEY_TITLE, game.getTitle());
//        Log.d(LOG_TAG, "Using title: " + game.getTitle());
//
//        values.put(KEY_DESCRIPTION, game.getDescription());
//        Log.d(LOG_TAG, "Using description: " + game.getDescription());
//
//        values.put(KEY_DEVELOPER, game.getDeveloper());
//        Log.d(LOG_TAG, "Using developer: " + game.getDeveloper());
//
//        values.put(KEY_PUBLISHER, game.getPublisher());
//        Log.d(LOG_TAG, "Using publisher: " + game.getPublisher());
//
//        values.put(KEY_RELEASE_DATE, game.getReleaseDate().getTime());
//        Log.d(LOG_TAG, "Using release date: " + game.getReleaseDate().getTime());
//
//        values.put(KEY_MIN_PLAYERS, game.getMinPlayers());
//        Log.d(LOG_TAG, "Using min players: " + String.valueOf(game.getMinPlayers()));
//
//        values.put(KEY_MAX_PLAYERS, game.getMaxPlayers());
//        Log.d(LOG_TAG, "Using max players: " + String.valueOf(game.getMaxPlayers()));
//
//        values.put(KEY_PICTURE_URI, game.getPictureURI());
//        Log.d(LOG_TAG, "Using picture URI: " + String.valueOf(game.getPictureURI()));
//
//        // Insert the values into the table
//        Log.d(LOG_TAG, "Inserting new game into db");
//        db.insert(TABLE_NAME, null, values);
//
//        // Close the database connection
//       // db.close();
//    }

    /**
     * Retrieve the table name.
     *
     * @return The table name
     */
    public String getTableName()
    {
        return TABLE_NAME;
    }

    /**
     * Populate the database with the pre-determined Games [13] [14] [15] [16] [17].
     */
    @Override
    public void populateDatabase()
    {
        // Get the database so it can be accessed/written to
        SQLiteDatabase database = getWritableDatabase();

        // Initialize the ContentValues
        ContentValues values = new ContentValues();

        // Refresh the ContentValues and add to the table
        values = new ContentValues();
        values.put(KEY_TITLE, "Borderlands 4");
        values.put(KEY_DESCRIPTION, "A looter-shooter set in a science-fantasy setting where the players play as Vault Hunters hunting for treasure in Vaults.");
        values.put(KEY_DEVELOPER, "Gearbox");
        values.put(KEY_PUBLISHER, "2K");
        values.put(KEY_RELEASE_DATE, "9/11/2025 00:00:00");
        values.put(KEY_MIN_PLAYERS, 1);
        values.put(KEY_MAX_PLAYERS, 4);
        values.put(KEY_PICTURE_URI, R.drawable.borderlands4);
        // Insert the values into the table
        Log.d(LOG_TAG, "Inserting Borderlands 4 into db");
        database.insert(TABLE_NAME, null, values);

        // Refresh the ContentValues and add to the table
        values = new ContentValues();
        values.put(KEY_TITLE, "Overcooked");
        values.put(KEY_DESCRIPTION, "Overcooked is a chaotic couch co-op cooking game for one to four players. Working as a team, you and your fellow chefs must prepare, cook and serve up a variety of tasty orders before the baying customers storm out in a huff.");
        values.put(KEY_DEVELOPER, "Ghost Town Games Ltd.");
        values.put(KEY_PUBLISHER, "Team17 Digital Ltd");
        values.put(KEY_RELEASE_DATE, "8/3/2016 00:00:00");
        values.put(KEY_MIN_PLAYERS, 1);
        values.put(KEY_MAX_PLAYERS, 4);
        values.put(KEY_PICTURE_URI, R.drawable.overcooked);
        // Insert the values into the table
        Log.d(LOG_TAG, "Inserting Overcooked into db");
        database.insert(TABLE_NAME, null, values);

        // Refresh the ContentValues and add to the table
        values = new ContentValues();
        values.put(KEY_TITLE, "God of War");
        values.put(KEY_DESCRIPTION, "His vengeance against the Gods of Olympus years behind him, Kratos now lives as a man in the realm of Norse Gods and monsters. It is in this harsh, unforgiving world that he must fight to survive… and teach his son to do the same.");
        values.put(KEY_DEVELOPER, "Santa Monica Studio, Jetpack Interactive");
        values.put(KEY_PUBLISHER, "PlayStation Publishing LLC");
        values.put(KEY_RELEASE_DATE, "1/14/2022 00:00:00");
        values.put(KEY_MIN_PLAYERS, 1);
        values.put(KEY_MAX_PLAYERS, 1);
        values.put(KEY_PICTURE_URI, R.drawable.god_of_war);
        // Insert the values into the table
        Log.d(LOG_TAG, "Inserting God of War into db");
        database.insert(TABLE_NAME, null, values);

        // Refresh the ContentValues and add to the table
        values = new ContentValues();
        values.put(KEY_TITLE, "Lego Star Wars: The Skywalker Saga");
        values.put(KEY_DESCRIPTION, " Play through all nine Skywalker saga films in a game unlike any other. With over 300 playable characters, over 100 vehicles, and 23 planets to explore, a galaxy far, far away has never been more fun!");
        values.put(KEY_DEVELOPER, "TT Games");
        values.put(KEY_PUBLISHER, "Warner Bros.");
        values.put(KEY_RELEASE_DATE, "4/5/2022 00:00:00");
        values.put(KEY_MIN_PLAYERS, 1);
        values.put(KEY_MAX_PLAYERS, 2);
        values.put(KEY_PICTURE_URI, R.drawable.lego_star_wars_the_skywalker_saga);
        // Insert the values into the table
        Log.d(LOG_TAG, "Inserting Lego Star Wars: The Skywalker Saga into db");
        database.insert(TABLE_NAME, null, values);

        // Refresh the ContentValues and add to the table
        values = new ContentValues();
        values.put(KEY_TITLE, "Dead by Daylight");
        values.put(KEY_DESCRIPTION, "10 years of horror. 10 years of jump scares and close calls. 10 years of brutal sacrifices and thrilling escapes. Trapped forever in a realm of eldritch evil where even death is not an escape, four determined Survivors face a bloodthirsty Killer in a vicious game of nerve and wits.");
        values.put(KEY_DEVELOPER, "Behaviour Interactive Inc.");
        values.put(KEY_PUBLISHER, "Behaviour Interactive Inc.");
        values.put(KEY_RELEASE_DATE, "6/14/2016 00:00:00");
        values.put(KEY_MIN_PLAYERS, 1);
        values.put(KEY_MAX_PLAYERS, 5);
        values.put(KEY_PICTURE_URI, R.drawable.dead_by_daylight);
        // Insert the values into the table
        Log.d(LOG_TAG, "Inserting Dead by Daylight into db");
        database.insert(TABLE_NAME, null, values);
    }
}