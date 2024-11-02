package com.example.ecommercefront_end.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import java.util.UUID

@Composable
fun insertButton(userId: UUID?=null, navController: NavController, onButtonClicked : ()->Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            onButtonClicked()
        })) {
        Icon(
            Icons.Filled.AddBox,
            contentDescription = "Insert",
            modifier = Modifier
                .size(50.dp).align(Alignment.CenterHorizontally)
        )
    }
}