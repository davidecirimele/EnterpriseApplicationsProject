package com.example.ecommercefront_end.ui.user

import android.content.ClipData.Item
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel


@Composable
fun AddressesScreen(viewModel: AddressViewModel, navHostController: NavHostController) {

    val addresses by viewModel.addresses.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Saved Addresses: ",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        addNewAddressCard(navHostController)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if(addresses != null && !addresses!!.isEmpty())
                for (address in addresses!!)
                    item {
                        AddressView(address = address, viewModel, navHostController, true)
                    }
        }

    }

}

@Composable
fun addNewAddressCard(navHostController: NavHostController){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            navHostController.navigate("insert-address") {
                popUpTo("addresses") {
                    saveState = true
                }
            }
        })) {
        Icon(
                Icons.Filled.AddBox,
                contentDescription = "Insert address",
                modifier = Modifier
                    .size(50.dp).align(Alignment.CenterHorizontally)
        )
    }
}
