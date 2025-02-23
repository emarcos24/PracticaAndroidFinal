package com.euroformac.practicafinal

fun validarTexto(texto: String): Boolean {
    return texto.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
}