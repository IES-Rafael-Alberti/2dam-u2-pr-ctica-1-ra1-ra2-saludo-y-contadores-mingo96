package com.example.practica1

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.lifecycle.ViewModel
import com.example.practica1.ui.theme.Practica1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    inicio()
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun inicio(){

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    
    var cambiaShow = {
        showDialog=!showDialog
    }

    var contenidoTexto by rememberSaveable {
        mutableStateOf("")
    }

    var cambiaContenido = { Text : String->
        contenidoTexto = Text
    }

    var contadorAceptar by rememberSaveable {
        mutableStateOf(0)
    }

    var contadorCancelar by rememberSaveable {
        mutableStateOf(0)
    }

    var sumar = {opcion :String-> if (opcion=="A") contadorAceptar +=1 else contadorCancelar+=1}

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Button(onClick = cambiaShow) {
            Text(text = "Saludar")
        }
        Text(text = "")
        Text("A$contadorAceptar, C$contadorCancelar")


        if (showDialog){
            dialogo(cambiaShow, cambiaContenido, sumar)
        }

        if (contenidoTexto.isNotEmpty()&& !showDialog){
            Text(text = "bienvenido $contenidoTexto")
        }
    }
}

@Composable
fun dialogo(editorShow:()->Unit, editorContenido:(String)->Unit, sumar : (String)->Unit){
    AlertDialog(
        modifier = Modifier.padding(top = 15.dp, bottom = 20.dp),
        onDismissRequest = {},
        confirmButton = { contenido(editorShow, editorContenido, sumar)},
        title = { tituloDialogo() },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun contenido(editorShow:()->Unit, editorContenido:(String)->Unit, sumar : (String)->Unit){
    var texto by rememberSaveable{ mutableStateOf("")}

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = texto,
            onValueChange = {
                texto = it
                editorContenido(it)
                            },
            label = {Text("Introduce tu nombre")}
        )
        
        lineaDeBotones(textos = arrayOf("A", "L", "C"),
            cerrar = editorShow,
            accionesConParametros = arrayOf(sumar, editorContenido))
        
    }
}


@Composable
fun lineaDeBotones(textos : Array<String>, cerrar : ()->Unit, accionesConParametros : Array<(String)->Unit>){
    Row (
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        Button(onClick = {
            cerrar()
            accionesConParametros[0]("A")
        }) {
            Text(text = textos[0])
        }
        Button(onClick = {
            accionesConParametros[1]("")
        }) {
            Text(text = textos[1])
        }
        Button(onClick = {
            cerrar()
            accionesConParametros[1](" ")
            accionesConParametros[0]("C")
        }) {
            Text(text = textos[2])
        }
    }
}

@Composable
fun tituloDialogo(){
    Row (horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()){
        Text(text = "Configuracion", fontSize =30.sp)
    }
}