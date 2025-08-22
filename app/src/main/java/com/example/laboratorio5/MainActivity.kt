/* Alejandra Avilés - 24722
*  Laboratorio 4.2
*  Programación de Plataformas Móviles
* Fecha de entrega : 21/08/2025 */

package com.example.laboratorio5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    /* Inicialización del programa */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthyLivingApp()
        }
    }
}

@Composable
fun HealthyLivingApp() {
    /* Declaración de variables mutables */
    var itemList = remember { mutableStateListOf<Pair<String, String>>() }
    var recipeName by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField( /* Para el textbox del nombre de la receta */
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField( /* Para el textbox de la url de la receta */
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL de la imagen") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Para el botón que añadirá las recetas al listado */
            if (recipeName.isNotEmpty() && imageUrl.isNotEmpty()) { /* Validación de campos llenos */
                if (!itemList.any { it.first == recipeName || it.second == imageUrl}) { /* Validación de elementos únicos*/
                    itemList.add(Pair(recipeName, imageUrl)) /* Añadiendo receta al listado */
                    /* Reseteando variables (limpiando textbox) */
                    recipeName = ""
                    imageUrl = ""
                    errorMessage = ""
                } else { /* Por si el nombre o URL se repiten */
                    errorMessage = "El nombre o la URL ya existe."
                }
            } else { /* Por si el usuario no llena uno de los textbox */
                errorMessage = "Por favor, completa ambos campos."
            }
        }) {
            Text("Agregar") /* Texto del botón para añadir recetas */
        }
        if (errorMessage.isNotEmpty()) { /* Devolución de mensajes de error (por si hubiese) */
            Text(text = errorMessage, color = MaterialTheme.colors.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn { /* Impresión de la columna con las recetas */
            items(itemList) { item ->
                CustomCard(recipeName = item.first, imageUrl = item.second) {
                    itemList.remove(item)
                }
            }
        }
    }
}

/* Para cada una de las recetas añadidas*/
@Composable
fun CustomCard(recipeName: String, imageUrl: String, onDelete: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column {
            AsyncImage( /* Imagen de la receta */
                model = imageUrl, /* Según el enlace que reciba*/
                contentDescription = null,
                modifier = Modifier /*Para el tamaño de la imagen */
                    .fillMaxWidth()
                    .height(150.dp),
                /* Imagen predeterminada en lo que se obtiene la imagen de la url */
                placeholder =  painterResource(id = R.drawable.ic_placeholder),
                /* Imagen predeterminada en caso no se haya obtenido la imagen agregada por el usuario */
                error = painterResource(id = R.drawable.ic_error)
            )
            Text(text = recipeName, modifier = Modifier.padding(8.dp)) /* Para el nombre de la recete */
            TextButton(onClick = onDelete) { /* Para el botón de eliminar una receta */
                Text("Eliminar")
            }
        }
    }
}