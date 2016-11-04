package com.example.sufin.mochila;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.sufin.mochila.R.id.url;

/**
 * Created by sufin on 11/10/2016.
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    private static int cont=0;
    private static final int VERSION_BASEDATOS=1;
    private static final String NOMBRE_BASEDATOS="BD_Historial.db";
    private static final String NOMBRE_TABLA="Historial";

    public AdminSQLiteOpenHelper(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE historial(id INT PRIMARY KEY, nombre VARCHAR(20), url VARCHAR(50)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertar(String url){

        long nreg_afectados=-1;

        SQLiteDatabase db=getWritableDatabase();

        if(db!=null){
            ContentValues valores= new ContentValues();
            cont++;
            valores.put("id",cont);
            valores.put("nombre",url.startsWith("."));
            valores.put("url",url);

            nreg_afectados=db.insert("HISTORIAL",null,valores);
        }
        db.close();
        return nreg_afectados;
    }
}
