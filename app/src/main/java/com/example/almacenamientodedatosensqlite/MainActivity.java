package com.example.almacenamientodedatosensqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almacenamientodedatosensqlite.DataBase.AdminSQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {
    //    Se crea un objeto de la clase AdminSQLiteOpenHelper que fue la que se creó para manejar la bd
    private AdminSQLiteOpenHelper admin;
    //    Se crean las entradas de datos
    private EditText patente, marca, modelo, precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Se referencia todo del xml
        patente = findViewById(R.id.inputPatente);
        marca = findViewById(R.id.inputMarca);
        modelo = findViewById(R.id.inputModelo);
        precio = findViewById(R.id.inputPrecio);
//        Al objeto admin de la BD se le pasa this como contexto, el nombre de la base de datos que debe crear, null y la version 1 de la BD
        admin = new AdminSQLiteOpenHelper(this, "bd", null, 1);
    }

    //    Metodo para agregar vehiculo
    public void agregarVehiculo(View v) {
//        Se verifica si todos los campos tienen informacion
        if (patente.getText().toString().isEmpty() || marca.getText().toString().isEmpty() || modelo.getText().toString().isEmpty() || precio.getText().toString().isEmpty()) {
            Toast.makeText(this, "Hay campos vacios", Toast.LENGTH_SHORT).show();
        } else {
//            Se cre un objeto SQLiteDatabase para manipular la base de datos
            SQLiteDatabase db = admin.getWritableDatabase();//Se llama al metodo getWritableDatabase() del objeto admin ya que se va a modificar la BD
//        Se crea el objeto que va a guardar los datos de la tabla
            ContentValues datos = new ContentValues();
//        Ingresar los datos
            datos.put("patente", patente.getText().toString());
            datos.put("marca", marca.getText().toString());
            datos.put("modelo", modelo.getText().toString());
            datos.put("precio", precio.getText().toString());
//        Insertar en la base de datos
            db.insert("vehiculos", null, datos);
//            Se cierra la BD
            db.close();
            patente.setText("");
            marca.setText("");
            modelo.setText("");
            precio.setText("");
            Toast.makeText(this, "Se ha agregado a la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    //    Consultar informacion de los vehiculos
    public void consultar(View v) {
//        Se valida si el campo de la patente está vacío
        if (patente.getText().toString().isEmpty()) {
            Toast.makeText(this, "No se puede buscar sin la patente", Toast.LENGTH_SHORT).show();
        } else {
//            Se crea el objeto que manipula la BD
            SQLiteDatabase db = admin.getReadableDatabase();//Se llama al método getReadableDatabase() ya que solo se va a leer de la base de datos
//            Se obtiene el valor de la patente
            String pat = patente.getText().toString();
//            Se crea un cursor para recorrer todas las opciones que devuelve la base de datos
            /*En este caso se llama al metodo rawQuery() del objeto que manipula la BD, este recibe 2 parametros,
             * el 1ero es la consulta SQL que se va a ejecutar y el 2do es null*/
            Cursor fila = db.rawQuery("SELECT marca,modelo,precio FROM vehiculos WHERE vehiculos.patente=\"" + pat + "\"", null);
//            Se verifica si el Cursor tiene información
            if (fila.moveToFirst()) {
//                Se escriben los valores en la interfaz
                marca.setText(fila.getString(0));
                modelo.setText(fila.getString(1));
                precio.setText(fila.getString(2));
//                Se cierra la BD
                db.close();
            } else {
//                Sino se encuentran valores en el cursor se lanza un mensaje
                Toast.makeText(this, "No existe ningun vehiculo con esa patente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    Metodo para modificar los vehiculos
    public void modificar(View v) {
//        Validar que la patente tiene datos
        if (patente.getText().toString().isEmpty()) {
//            Si está vacio se lanza un mensaje
            Toast.makeText(this, "Se necesita la patente para buscar el vehiculo", Toast.LENGTH_SHORT).show();
        } else {
//            Se crea un objeto para manipular la BD
            SQLiteDatabase db = admin.getWritableDatabase();
//            Se obtiene la patente
            String pat = patente.getText().toString();
//            Se crea un objeto que guarde los nuevos valores que se van a poner en la base de datos
            ContentValues datos = new ContentValues();
//            Se agregan los datos al objeto
            datos.put("marca", marca.getText().toString());
            datos.put("modelo", modelo.getText().toString());
            datos.put("precio", precio.getText().toString());
//            Modificar datos
            /*Del objeto que manipula la BD se llama al metodo update() que recibe 4 valores, el 1ero es el nombre de la tabla
             * que se va a eliminar, el 2do es la coleccion con los datos, el 3ero es la condición por la que se va a actualizar
             * y el 4to es null, ya que no es necesario en este caso*/
//            Esto devuelve un entero que representa la cantidad de registros que se actualizaron
            int cant = db.update("vehiculos", datos, "patente=\"" + pat + "\"", null);
//            Si la cantidad que devuelve el mayor de 0 es que se actualizaron
            if (cant > 0) {
                Toast.makeText(this, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró ningún vehiculo para actualizar", Toast.LENGTH_SHORT).show();
            }
//            Se cierra la base de datos
            db.close();
        }
    }

    //    Metodo para eliminar vehiculos
    public void eliminar(View v) {
//        Se valida si la patente está vacia
        if (patente.getText().toString().isEmpty()) {
//            Si está vacio se lanza un mensaje de error
            Toast.makeText(this, "No se puede eliminar sin la patente", Toast.LENGTH_SHORT).show();
        } else {
//            Se crea un objeto para manipular la BD
            SQLiteDatabase db = admin.getWritableDatabase();
//            Se obtiene la patente
            String pat = patente.getText().toString();
//            Se llama al metodo delete de la clase SQLiteDatabase, este retorna un entero con la cant de filas eliminadas
            /*Recibe 3 parametros, el 1ero es el nombre de la tabla a eliminar, el 2do es la condicion y el 3ero es null */
            int cant = db.delete("vehiculos", "patente=\"" + pat + "\"", null);
            if (cant > 0) {
                Toast.makeText(this, "Se ha eliminado el vehiculo", Toast.LENGTH_SHORT).show();
                patente.setText("");
                marca.setText("");
                modelo.setText("");
                precio.setText("");
            } else {
                Toast.makeText(this, "No se pudo eliminar el vehiculo", Toast.LENGTH_SHORT).show();
            }
//            Se cierra la base de datos
            db.close();
        }
    }
}