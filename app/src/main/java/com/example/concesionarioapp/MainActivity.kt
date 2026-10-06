package com.example.concesionarioapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private lateinit var campoPlaca: EditText
    private lateinit var campoMarca: EditText
    private lateinit var campoModelo: EditText
    private lateinit var campoPrecio: EditText

    private lateinit var botonRegistrar: Button
    private lateinit var botonConsultar: Button
    private lateinit var botonModificar: Button
    private lateinit var botonEliminar: Button
    private lateinit var botonLimpiar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        campoPlaca = findViewById(R.id.etPlaca)
        campoMarca = findViewById(R.id.etMarca)
        campoModelo = findViewById(R.id.etModelo)
        campoPrecio = findViewById(R.id.etPrecio)

        botonRegistrar = findViewById(R.id.btnRegistrar)
        botonConsultar = findViewById(R.id.btnConsultar)
        botonModificar = findViewById(R.id.btnModificar)
        botonEliminar = findViewById(R.id.btnEliminar)
        botonLimpiar = findViewById(R.id.btnLimpiar)


        botonRegistrar.setOnClickListener { registrarAuto() }
        botonConsultar.setOnClickListener { consultarAuto() }
        botonModificar.setOnClickListener { modificarAuto() }
        botonEliminar.setOnClickListener { eliminarAuto() }
        botonLimpiar.setOnClickListener { limpiarCampos() }
    }


    private fun registrarAuto() {

        val administradorBD = AdminSQLiteOpenHelper(this, "concesionario", null, 1)
        val baseDeDatos = administradorBD.writableDatabase

        val placa = campoPlaca.text.toString().trim()
        val marca = campoMarca.text.toString().trim()
        val modelo = campoModelo.text.toString().trim()
        val precio = campoPrecio.text.toString().trim()


        if (placa.isNotEmpty() && marca.isNotEmpty() && modelo.isNotEmpty() && precio.isNotEmpty()) {

            val registro = ContentValues()
            registro.put("placa", placa)
            registro.put("marca", marca)
            registro.put("modelo", modelo)
            registro.put("precio", precio.toDouble())


            val resultado = baseDeDatos.insert("autos", null, registro)
            baseDeDatos.close()

            if (resultado != -1L) {
                Toast.makeText(this, "Auto registrado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error: Ya existe un auto con esa placa", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }



    private fun consultarAuto() {
        val administradorBD = AdminSQLiteOpenHelper(this, "concesionario", null, 1)
        val baseDeDatos = administradorBD.readableDatabase

        val placa = campoPlaca.text.toString().trim()

        if (placa.isNotEmpty()) {
            val fila = baseDeDatos.rawQuery(
                "SELECT marca, modelo, precio FROM autos WHERE placa = '$placa'",
                null
            )

            if (fila.moveToFirst()) {
                campoMarca.setText(fila.getString(0))
                campoModelo.setText(fila.getString(1))


                val precioDouble = fila.getDouble(2)
                val precioFormateado = String.format("%.0f", precioDouble)
                campoPrecio.setText(precioFormateado)

                Toast.makeText(this, "Auto encontrado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se encontró ningún auto con esa placa", Toast.LENGTH_SHORT).show()
            }
            fila.close()
            baseDeDatos.close()
        } else {
            Toast.makeText(this, "Ingrese la placa para buscar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun modificarAuto() {
        val administradorBD = AdminSQLiteOpenHelper(this, "concesionario", null, 1)
        val baseDeDatos = administradorBD.writableDatabase

        val placa = campoPlaca.text.toString().trim()
        val marca = campoMarca.text.toString().trim()
        val modelo = campoModelo.text.toString().trim()
        val precio = campoPrecio.text.toString().trim()

        if (placa.isNotEmpty() && marca.isNotEmpty() && modelo.isNotEmpty() && precio.isNotEmpty()) {
            val registro = ContentValues()
            registro.put("marca", marca)
            registro.put("modelo", modelo)
            registro.put("precio", precio.toDouble())


            val cantidadModificados = baseDeDatos.update("autos", registro, "placa = '$placa'", null)
            baseDeDatos.close()

            if (cantidadModificados > 0) {
                Toast.makeText(this, "Auto modificado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "No existe un auto registrado con esa placa", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Complete todos los campos para modificar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun eliminarAuto() {
        val administradorBD = AdminSQLiteOpenHelper(this, "concesionario", null, 1)
        val baseDeDatos = administradorBD.writableDatabase

        val placa = campoPlaca.text.toString().trim()

        if (placa.isNotEmpty()) {

            val cantidadEliminados = baseDeDatos.delete("autos", "placa = '$placa'", null)
            baseDeDatos.close()

            if (cantidadEliminados > 0) {
                Toast.makeText(this, "Auto eliminado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "No existe un auto con esa placa", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Ingrese la placa del auto a eliminar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun limpiarCampos() {
        campoPlaca.setText("")
        campoMarca.setText("")
        campoModelo.setText("")
        campoPrecio.setText("")
        campoPlaca.requestFocus()
    }
}