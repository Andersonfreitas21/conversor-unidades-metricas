package br.com.unifor.conversordeunidades

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var insiraValor: EditText
    private lateinit var butaoDeConverter: Button
    private lateinit var resultadoValorConvertido: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        insiraValor = findViewById(R.id.editTextValue)
        butaoDeConverter = findViewById(R.id.buttonConvert)
        resultadoValorConvertido = findViewById(R.id.textViewResult)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.medidas,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        butaoDeConverter.setOnClickListener {
            convertUnits()
        }
        findViewById<View>(R.id.main).setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
                v.clearFocus()
            }
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun convertUnits() {
        val daUnidade = spinnerFrom.selectedItem.toString()
        val paraUnidade = spinnerTo.selectedItem.toString()
        val valorInserido = insiraValor.text.toString()

        if (valorInserido.isEmpty()) {
            resultadoValorConvertido.text = "Por favor insira um valor válido."
            return
        }

        val valorDeEntrada = valorInserido.toDouble()
        val resultadoConvertido = converter(daUnidade, paraUnidade, valorDeEntrada)

        resultadoValorConvertido.text =
            String.format(Locale.ROOT, "Resultado: %.2f %s", resultadoConvertido, paraUnidade)
    }

    private fun converter(daUnidade: String, paraUnidade: String, valor: Double): Double {
        val metros = when (daUnidade) {
            "Centímetros" -> valor / 100
            "Metros" -> valor
            "Quilômetros" -> valor * 1000
            "Milhas" -> valor * 1609.34
            else -> 0.0
        }

        return when (paraUnidade) {
            "Centímetros" -> metros * 100
            "Metros" -> metros
            "Quilômetros" -> metros / 1000
            "Milhas" -> metros / 1609.34
            else -> 0.0
        }
    }
    private fun Activity.hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}