package com.example.application13

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.application13.database.AppDb
import com.example.application13.database.Product
import java.io.File

class photoViewModel : ViewModel() {
    val photoUri: MutableState<Uri?> = mutableStateOf(null)

    fun setPhotoUri(uri: Uri?) {
        photoUri.value = uri
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Panel(navController: NavController) {
    val context = LocalContext.current
    val viewModel: photoViewModel = viewModel()
    val photoUri: MutableState<Uri?> = viewModel.photoUri
    var isFetched by remember { mutableStateOf(false) }
    val products = remember { mutableStateListOf<Product>() }
    val db = AppDb.getDatabase()

    if (!isFetched) {
        products.clear()
        products.addAll(db.productDAO().getAll())
        isFetched = true
    }

    var product by remember { mutableStateOf<Product>(Product(0, "", 0.0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(modifier = Modifier,
                onClick = { navController.navigate("home") }) {
                Text(text = "Alışveriş")
            }
            Button(modifier = Modifier,
                onClick = { navController.navigate("report") }) {
                Text(text = "Raporlar")
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.LightGray)
                    .padding(8.dp)
                    .fillMaxHeight()
                    .weight(2f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PhotoPickerDemoScreen()
                Text("Ürün id")
                TextField(modifier = Modifier
                    .padding(end = 4.dp)
                    .fillMaxWidth(),
                    value = product.uid.toString(),
                    onValueChange = { }, label = {
                        Text("Ürün id")
                    })
                Text("Ürün adı")
                TextField(modifier = Modifier
                    .padding(end = 4.dp)
                    .fillMaxWidth(),
                    value = product.name.toString(),
                    onValueChange = { x -> product = product.copy(name = x) }, label = {
                        Text("Ürün adı")
                    })
                Text("Fiyat")
                TextField(modifier = Modifier
                    .padding(end = 4.dp)
                    .fillMaxWidth(),
                    value = product.price.toString(),
                    onValueChange = { x -> product = product.copy(price = x.toDouble()) }, label = {
                        Text("Fiyat")
                    })

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        var imageId = product.uid;
                        if (product.uid == 0L)
                            imageId = db.productDAO().insert(product).first();
                        else
                            db.productDAO().update(product);
                        if(photoUri.value != null){
                            saveImageToInternalStorage(context, photoUri.value, imageId.toString())
                        }
                        viewModel.setPhotoUri(null);
                        products.clear()
                        products.addAll(db.productDAO().getAll());
                    }) {
                        Text(text = "Kaydet")
                    }

                    Button(onClick = {
                        product = Product(0, "", 0.0)
                        viewModel.setPhotoUri(null);
                    }) {
                        Text(text = "Yeni kayıt")
                    }
                }
            }
            LazyVerticalGrid(
                modifier = Modifier.weight(4f),
                columns = GridCells.Fixed(3),
                content = {
                    items(products.size) { index ->
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(color = Color.LightGray)
                                .padding(8.dp)
                                .height(160.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var _current = LocalContext.current;
                            val file: File = LocalContext.current.getFileStreamPath(products[index].uid.toString())
                            if (file.exists()) {
                                Image(
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .height(80.dp)
                                        .fillMaxWidth(),
                                    painter = rememberAsyncImagePainter(_current.openFileInput(products[index].uid.toString())?.readAllBytes()),
                                    contentDescription = products[index].name
                                )
                            }
                            Text(text = products[index].name.toString())
                            Text(text = "Fiyat:" + products[index].price.toString())
                            Row {
                                Button(modifier = Modifier.padding(end = 4.dp),
                                    onClick = {
                                        db.orderDAO().deleteByProduct(products[index].uid)
                                        db.productDAO().delete(products[index])
                                        val file: File = _current.getFileStreamPath(products[index].uid.toString())
                                        if (file.exists()) {
                                            _current.deleteFile(products[index].uid.toString())
                                        }
                                        products.clear()
                                        products.addAll(db.productDAO().getAll())
                                    }) {
                                    Text(text = "Sil")
                                }
                                Button(onClick = {
                                    product = products[index].copy()
                                }) {
                                    Text(text = "Düzenle")
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun PhotoPickerDemoScreen() {

    val viewModel: photoViewModel = viewModel()
    val photoUri: MutableState<Uri?> = viewModel.photoUri
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        viewModel.setPhotoUri(uri)
    }

    Row(modifier = Modifier.fillMaxWidth().height(80.dp)) {
        Button(
            onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Text("Resim Seç")
        }

        if (photoUri.value != null) {
            //Use Coil to display the selected image
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = photoUri.value)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .border(6.0.dp, Color.Gray),
                contentScale = ContentScale.Fit
            )
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri?, name: String) {
    val inputStream = context.contentResolver.openInputStream(uri!!)
    val outputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}