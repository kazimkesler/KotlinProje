package com.example.application13

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.application13.database.AppDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Report(navController: NavController) {

    val db = AppDb.getDatabase()
    val orders = db.orderDAO().getAll()
    val products = db.productDAO().getAll()

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
                onClick = { navController.navigate("panel") }) {
                Text(text = "Panel")
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.LightGray)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.DarkGray)
                        .padding(8.dp)
                        .height(40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Text(color = Color.White, text = "Ürünlere Göre Satış")
                }

                for (i in orders.groupBy { x -> x.productId }) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White)
                            .padding(8.dp)
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Text(products.find { x -> x.uid == i.key }?.name.toString() + " ")
                        Text(i.value.sumOf { x -> x.quantity }.toString() + " Adet")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.DarkGray)
                        .padding(8.dp)
                        .height(40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Text(color = Color.White, text = "Tarihe Göre Satış")
                }

                for (i in orders.groupBy { x -> x.date }) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White)
                            .padding(8.dp)
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Text(i.key + " ")
                        Text(i.value.sumOf { x -> x.quantity }.toString() + " Adet")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.DarkGray)
                        .padding(8.dp)
                        .height(40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Text(color = Color.White, text = "Son Siparişler")
                }

                for (i in orders.sortedByDescending { x -> x.uid }) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White)
                            .padding(8.dp)
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Text(products.find { x -> x.uid == i.productId }?.name + " ")
                        Text(i.quantity.toString() + " Adet ")
                        Text(i.date)
                    }
                }
            }
        }
    }
}
