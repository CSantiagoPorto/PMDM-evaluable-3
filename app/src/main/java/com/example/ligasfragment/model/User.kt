package com.example.ligasfragment.model

import java.io.Serializable

data class User(
    val email: String,
    val password: String? = null // Firebase no almacena la contraseña directamente
):Serializable
