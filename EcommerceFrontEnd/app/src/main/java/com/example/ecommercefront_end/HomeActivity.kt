package com.example.ecommercefront_end

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeProducts(){
    var searchText by remember { mutableStateOf("") }
    val previousQueries by remember { mutableStateOf(searchText) }
    var expanded by remember { mutableStateOf(false) }

    if (searchText.trim().isEmpty())
        true
    else {
        var match = true
        val res = searchText.trim().split(regex = "(\\s)+".toRegex())

    }

    LazyColumn (modifier = Modifier.padding(all = 3.dp)){
        item {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    value = searchText,
                    onValueChange = { searchText = it; expanded = it.isNotEmpty() },
                    placeholder = { Text(text = stringResource(R.string.search)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false},
                    modifier = Modifier.fillMaxWidth(),
                    properties = PopupProperties(focusable = false)
                ) {
                    previousQueries.forEach { label ->
                        DropdownMenuItem(
                            text = {"ciao"},
                            onClick = { searchText = label.toString(); expanded = false })
                    }
                }

            }
        }
    }

}

