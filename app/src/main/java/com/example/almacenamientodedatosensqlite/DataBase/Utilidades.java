package com.example.almacenamientodedatosensqlite.DataBase;

//Esta clase es para facilitar el uso de las consultas
public class Utilidades {
    public static String CREAR_TABLA_VEHICULO = "CREATE TABLE \"vehiculos\" (\"patente\"\tTEXT,\"marca\" TEXT UNIQUE,\"modelo\" INTEGER, \"precio\"\tREAL, PRIMARY KEY(\"patente\"));";

}
