package com.projects.notdefterim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "not_database";

    private static final String TABLE_NAME = "not_listesi";
    private static String NOT_ID = "id";
    private static String NOT_BASLIK = "not_baslik";
    private static String NOT_ICERIK = "not_icerik";
    private static String NOT_TARIHI = "not_tarih";
    private static String NOT_SAAT = "not_saat";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Databesi oluşturuyoruz.Bu methodu biz oluşturmuyoruz. Databese de obje oluşturduğumuzda otamatik oluşturuluyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + NOT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOT_BASLIK + " TEXT,"
                + NOT_ICERIK + " TEXT,"
                + NOT_TARIHI + " TEXT,"
                + NOT_SAAT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void notSil(int id) { //id si belli olan row u silmek için
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, NOT_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void notEkle(String not_baslik, String not_icerik, String not_tarihi, String not_saat) {
        //notEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOT_BASLIK, not_baslik);
        values.put(NOT_ICERIK, not_icerik);
        values.put(NOT_TARIHI, not_tarihi);
        values.put(NOT_SAAT, not_saat);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık
    }

    public HashMap<String, String> notDetay(int id) {
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String, String> not = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            not.put(NOT_BASLIK, cursor.getString(1));
            not.put(NOT_ICERIK, cursor.getString(2));
            not.put(NOT_TARIHI, cursor.getString(3));
            not.put(NOT_SAAT, cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return not
        return not;
    }


    public ArrayList<Not> notList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Not> notlar = new ArrayList<Not>();

        if (cursor.moveToFirst()) {
            do {
                Not not = new Not();
                not.setId(cursor.getString(0));
                not.setBaslik(cursor.getString(1));
                not.setIcerik(cursor.getString(2));
                not.setTarih(cursor.getString(3));
                not.setSaat(cursor.getString(4));
                notlar.add(not);
            } while (cursor.moveToNext());
        }

        db.close();
        return notlar;
    }

    public ArrayList<HashMap<String, String>> notlar() {
        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> notlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                notlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return not liste
        return notlist;
    }

    public void notDuzenle(int id, String not_baslik, String not_icerik, String not_tarihi, String not_saat) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(NOT_BASLIK, not_baslik);
        values.put(NOT_ICERIK, not_icerik);
        values.put(NOT_TARIHI, not_tarihi);
        values.put(NOT_SAAT, not_saat);

        // updating row
        db.update(TABLE_NAME, values, NOT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void resetTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
