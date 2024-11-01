package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.ui.books.ChoiceSelector
import com.example.ecommercefront_end.viewmodels.BookViewModel

@Composable
fun OrderOptionsScreen(viewModel: BookViewModel, onDismiss: () -> Unit){

    val sortOption by viewModel.sortOption.collectAsState()
    val sortOptions = remember {
        listOf("Price: Low to High", "Price: High to Low", "Weight: Low to High", "Weight: High to Low", "Number of pages: Low to High", "Number of pages: High to Low", "Age: Low to High", "Age: High to Low", "Newest", "Oldest")
    }

    Dialog(
        onDismissRequest = {onDismiss()}
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 600.dp)
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    IconButton(modifier = Modifier.align(Alignment.CenterVertically),onClick = {
                        onDismiss()
                    }) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close Order Options",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
                ChoiceSelector(
                    parameter = "Sort by",
                    choiches = sortOptions,
                    selectedChoice = sortOption,
                    onChoiceSelected = { selectedChoice ->
                        viewModel.setOrderOption(selectedChoice)
                        onDismiss()
                    })

                Spacer(modifier = Modifier.height(10.dp))
            }

            }
        }
}
