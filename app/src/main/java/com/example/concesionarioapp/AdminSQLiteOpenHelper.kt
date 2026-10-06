package com.example.concesionarioapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AdminSQLiteOpenHelper(
    contexto: Context?,
    nombreBD: String?,
    fabrica: SQLiteDatabase.CursorFactory?,
    versionBD: Int
) : SQLiteOpenHelper(contexto, nombreBD, fabrica, versionBD) {


    override fun onCreate(baseDeDatos: SQLiteDatabase?) {

        val crearTablaAutos = "CREATE TABLE autos (" +
                "placa TEXT PRIMARY KEY, " +
                "marca TEXT, " +
                "modelo TEXT, " +
                "precio REAL)"
        baseDeDatos?.execSQL(crearTablaAutos)
    }


    override fun onUpgrade(baseDeDatos: SQLiteDatabase?, versionAnterior: Int, versionNueva: Int) {
        baseDeDatos?.execSQL("DROP TABLE IF EXISTS autos")
        onCreate(baseDeDatos)
    }
}