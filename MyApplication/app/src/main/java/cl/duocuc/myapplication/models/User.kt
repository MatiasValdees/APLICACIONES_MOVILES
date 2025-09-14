package cl.duocuc.myapplication.models

data class User(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val genero: String,
    val pais: String
)