package com.ptit.filmdictionary.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.ObservableArrayList;

import com.ptit.filmdictionary.data.model.Movie;
import com.ptit.filmdictionary.data.source.local.contract.FavoriteContract.FavoriteEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movie.db";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS";
    private static final String SELECTION_SUFFIX = " LIKE ?";
    private static final String SQL_CREATE_FAVORITE =
            String.format(Locale.US, "CREATE TABLE %s (%s INTEGER PRIMARY KEY," +
                            "%s TEXT, %s TEXT, %s TEXT)",
                    FavoriteEntry.TABLE_FAVORITE,
                    FavoriteEntry.COLUMN_NAME_ID,
                    FavoriteEntry.COLUMN_NAME_MOVIE,
                    FavoriteEntry.COLUMN_NAME_VOTE,
                    FavoriteEntry.COLUMN_NAME_POSTER);
    private static final String SQL_DELETE_FAVORITE =
            TABLE_DROP + FavoriteEntry.TABLE_FAVORITE;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITE);
        onCreate(db);
    }

    public ObservableArrayList<Movie> getAllFavorite() {
        SQLiteDatabase db = getReadableDatabase();
        List<Movie> movies = new ArrayList<>();
        ObservableArrayList<Movie> moviesObservable = new ObservableArrayList<>();
        String[] projection = {
                FavoriteEntry.COLUMN_NAME_ID,
                FavoriteEntry.COLUMN_NAME_MOVIE,
                FavoriteEntry.COLUMN_NAME_VOTE,
                FavoriteEntry.COLUMN_NAME_POSTER
        };
        Cursor cursor = db.query(FavoriteEntry.TABLE_FAVORITE,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (cursor != null) cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor
                    .getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_ID));
            String title = cursor.getString(cursor
                    .getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_MOVIE));
            Double vote_average = cursor.getDouble(cursor
                    .getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_VOTE));
            String poster_path = cursor.getString(cursor
                    .getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_POSTER));
            Movie movie = new Movie();
            movie.setId(id);
            movie.setTitle(title);
            movie.setVoteAverage(vote_average);
            movie.setPosterPath(poster_path);

            movies.add(movie);
            cursor.moveToNext();
        }
        moviesObservable.addAll(movies);
        return moviesObservable;
    }

    public boolean addFavorite(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteEntry.COLUMN_NAME_ID, movie.getId());
        values.put(FavoriteEntry.COLUMN_NAME_MOVIE, movie.getTitle());
        values.put(FavoriteEntry.COLUMN_NAME_VOTE, movie.getVoteAverage());
        values.put(FavoriteEntry.COLUMN_NAME_POSTER, movie.getPosterPath());
        long result = db.insert(FavoriteEntry.TABLE_FAVORITE, null, values);
        return (result > 0);
    }

    public boolean deleteFavorite(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = FavoriteEntry.COLUMN_NAME_ID + SELECTION_SUFFIX;
        String[] selectionArgs = {String.valueOf(movie.getId())};
        int result = db.delete(FavoriteEntry.TABLE_FAVORITE, selection, selectionArgs);
        return (result > 0);
    }

    public boolean isFavorite(Movie movie) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {FavoriteEntry.COLUMN_NAME_ID};
        String selection = FavoriteEntry.COLUMN_NAME_ID + SELECTION_SUFFIX;
        String[] selectionArgs = {String.valueOf(movie.getId())};
        Cursor cursor = db.query(FavoriteEntry.TABLE_FAVORITE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        return (cursor != null && !cursor.isAfterLast());
    }
}