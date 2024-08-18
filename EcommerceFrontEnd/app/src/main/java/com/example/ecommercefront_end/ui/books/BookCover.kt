package com.example.ecommercefront_end.ui.books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookCover(title: String, author: String) {
    Card(
        modifier = Modifier
            .size(150.dp, 220.dp)
            .padding(8.dp)
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicText(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = author,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}