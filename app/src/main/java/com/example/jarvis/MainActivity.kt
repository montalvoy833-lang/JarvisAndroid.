
package com.example.jarvis

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private var voz: TextToSpeech? = null
    private lateinit var respuesta: TextView
    private lateinit var entrada: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        voz = TextToSpeech(this, this)

        val pantalla = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 40, 32, 32)
            setBackgroundColor(0xFF101820.toInt())
        }

        val titulo = TextView(this).apply {
            text = "J.A.R.V.I.S."
            textSize = 32f
            setTextColor(0xFF00E5FF.toInt())
            gravity = Gravity.CENTER
        }

        val subtitulo = TextView(this).apply {
            text = "Tu asistente personal"
            textSize = 16f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
        }

        entrada = EditText(this).apply {
            hint = "Escribe un mensaje..."
            textSize = 16f
            setSingleLine(true)
            setTextColor(0xFFFFFFFF.toInt())
            setHintTextColor(0xFFAAAAAA.toInt())
        }

        val boton = Button(this).apply {
            text = "Preguntar a JARVIS"
            setOnClickListener { responder() }
        }

        respuesta = TextView(this).apply {
            text = "Hola. Soy JARVIS. ¿En qué puedo ayudarte?"
            textSize = 18f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
        }

        pantalla.addView(titulo)
        pantalla.addView(subtitulo)
        pantalla.addView(entrada)
        pantalla.addView(boton)
        pantalla.addView(respuesta)

        setContentView(pantalla)
    }

    private fun responder() {
        val mensaje = entrada.text.toString().trim()

        if (mensaje.isEmpty()) {
            respuesta.text = "Escribe algo para que pueda ayudarte."
            return
        }

        val texto = when {
            mensaje.contains("hola", ignoreCase = true) ->
                "Hola. Estoy listo para ayudarte."

            mensaje.contains("cómo estás", ignoreCase = true) ->
                "Funcionando correctamente. ¿Qué necesitas?"

            mensaje.contains("tu nombre", ignoreCase = true) ->
                "Soy JARVIS, tu asistente personal."

            mensaje.contains("hora", ignoreCase = true) ->
                "La hora de tu teléfono es " +
                    java.text.SimpleDateFormat(
                        "HH:mm", Locale.getDefault()
                    ).format(java.util.Date())

            else ->
                "He recibido tu mensaje: $mensaje. " +
                    "Todavía estoy aprendiendo nuevas funciones."
        }

        respuesta.text = texto
        voz?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "jarvis")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            voz?.language = Locale("es", "MX")
        }
    }

    override fun onDestroy() {
        voz?.stop()
        voz?.shutdown()
        super.onDestroy()
    }
}
