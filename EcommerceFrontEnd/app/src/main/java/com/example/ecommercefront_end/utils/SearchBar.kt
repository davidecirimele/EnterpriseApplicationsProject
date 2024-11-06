package com.example.ecommercefront_end.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.viewmodels.BookViewModel

@Composable
fun SearchBar(filterOptions: Boolean, onFilterOptionsChange: (Boolean) -> Unit, onSearchValueChange: (String)-> Unit) {
    var searchValue by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchValue,
            onValueChange = { searchValue = it;
                onSearchValueChange(it)
                            },
            label = { Text("Search by Title, Author or Publisher") },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp,top = 8.dp,bottom = 8.dp, end = 2.dp)
        )

        IconButton(modifier = Modifier.align(Alignment.CenterVertically),onClick = {
            if(!filterOptions)
                onFilterOptionsChange(true)
            else
                onFilterOptionsChange(false)
        }) {
            Icon(
                Icons.Filled.FilterAlt,
                contentDescription = "Filter Books",
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }
    }
}