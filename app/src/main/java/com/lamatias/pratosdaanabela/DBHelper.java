package com.lamatias.pratosdaanabela;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;
import com.lamatias.pratosdaanabela.logic.App;
import com.lamatias.pratosdaanabela.logic.AppClass;
import com.lamatias.pratosdaanabela.logic.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PratosDaAnabela.sqlite";
    public static final String USERS_TABLE = "users";
    public static final String FOODS_TABLE = "foods";
    public static final String EATING_TABLE = "eating";
    public static final String FOOD_COLUMN = "food";
    public static final String NAME_COLUMN = "name";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table users " +
                        "(name text primary key)"
        );

        db.execSQL(
                "create table foods " +
                        "(food text primary key)"
        );

        db.execSQL(
                "create table eating " +
                        "(name text, food text, " +
                        "primary key (name,food), " +
                        "foreign key (name) references users (name)," +
                        "foreign key (food) references foods (food) on delete cascade)"
        );
        insertUser("Bia", db);
        insertUser("Gonçalo", db);
        insertUser("Márcia", db);
        insertUser("Pedro Biléu", db);
        insertUser("Pedro Grilo", db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    public App load() throws FoodAlreadyExists {
        SQLiteDatabase db = this.getReadableDatabase();
        App app = new AppClass();
        ArrayList<String> al = getAllFoods();
        if (al != null) {
            for (String f : al) {
                List<User> users = new ArrayList<>();
                Cursor res = db.rawQuery("select * from eating where food = '" + f + "'", null);
                res.moveToFirst();

                while (!res.isAfterLast()) {
                    users.add(app.getUser(res.getString(res.getColumnIndex(NAME_COLUMN))));
                    res.moveToNext();
                }
                app.insertFood(f, users.iterator());
                res.close();
            }
        }
        return app;
    }

    public void insertUser(String name, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, name);
        db.insert(USERS_TABLE, null, contentValues);
    }

    public void insertFood(String food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOOD_COLUMN, food);
        db.insert(FOODS_TABLE, null, contentValues);
    }

    public void insertEating(String name, String food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, name);
        contentValues.put(FOOD_COLUMN, food);
        db.insert(EATING_TABLE, null, contentValues);
    }

    public void deleteFood(String food) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("foods",
                "food = '" + food + "'", null);
    }

    public ArrayList<String> getAllFoods() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from foods", null);
        if (res.getCount() == 0)
            return null;
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(FOOD_COLUMN)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
