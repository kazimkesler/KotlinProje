package com.example.application13

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.application13.database.AppDb
import com.example.application13.database.Order
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Basket(
    val uid: Long,
    var count: Int
)


@Composable
fun Home(navController: NavController){
    val db = AppDb.getDatabase()
    val products = db.productDAO().getAll()
    val basket = remember { mutableStateListOf<Basket>()}
    Column(modifier= Modifier
        .fillMaxSize()){
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Button(modifier = Modifier,
                onClick = { navController.navigate("panel") }) {
                Text(text = "Panel")
            }
            Button(modifier = Modifier,
                onClick = { navController.navigate("report")  }) {
                Text(text = "Raporlar")
            }
        }
        Row(modifier= Modifier
            .padding(8.dp)){
            Column(modifier= Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.LightGray)
                .padding(8.dp)
                .fillMaxHeight()
                .weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                for(i in basket.sortedBy { x -> x.uid }){
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = products.find { x -> x.uid == i.uid }?.name.toString(),
                        )
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = i.count.toString() + " adet",
                        )

                        Text(modifier = Modifier.padding(end=4.dp),
                            text = (i.count * products.find { x -> x.uid == i.uid }!!.price).toString() + " tutar"
                        )

                        Button(onClick = {
                            val item = basket.find{x -> x.uid == i.uid}
                            basket.remove(item)
                        }) {
                            Text(text = "x")
                        }
                    }
                }
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "TOPLAM TUTAR: " + basket.sumOf { x -> x.count * products.find { y -> y.uid == x.uid }!!.price }
                        .toString(),
                )
                Button(onClick = {
                    basket.forEach {
                        db.orderDAO().insert(Order(0,it.uid,it.count,LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy"))))
                    }
                    basket.clear()
                }) {
                    Text(text = "Alışverişi tamamla")
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.weight(4f),
                columns = GridCells.Fixed(3),
                content = {
                    items(products.size) { index ->
                        Column(modifier= Modifier
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = Color.LightGray)
                            .padding(8.dp)
                            .height(160.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally){
                            val file: File = LocalContext.current.getFileStreamPath(products[index].uid.toString())
                            if (file.exists()) {
                                Image(
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .height(80.dp)
                                        .fillMaxWidth(),
                                    painter = rememberAsyncImagePainter(LocalContext.current.openFileInput(products[index].uid.toString())?.readAllBytes()),
                                    contentDescription = products[index].name
                                )
                            }
                            Text(text = products[index].name.toString())
                            Text(text = "Fiyat:" + products[index].price.toString())
                            Button(onClick = {
                                var count = 1
                                val item = basket.find{x -> x.uid == products[index].uid}
                                if(item != null){
                                    count = item.count + 1;
                                    basket.remove(item)
                                }
                                basket.add(Basket(products[index].uid, count))
                            }) {
                                Text(text = "Sepete Ekle")
                            }
                        }
                    }
                }
            )
        }
    }
}