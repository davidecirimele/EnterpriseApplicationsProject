package com.example.ecommercefront_end.utils


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.round

@Composable
fun BookCoverPlaceholder(title: String, author: String, color: Int) {
    var cardWidth by remember { mutableIntStateOf(0) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(color),
        ),
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                cardWidth = layoutCoordinates.size.width
            }.padding(end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(
            topStart = 0.dp,       // Angolo sinistro superiore ad angolo retto
            bottomStart = 0.dp,     // Angolo sinistro inferiore ad angolo retto
            topEnd = 8.dp,          // Angolo destro superiore leggermente arrotondato
            bottomEnd = 8.dp        // Angolo destro inferiore leggermente arrotondato
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                fontSize = (cardWidth/30).sp,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = author,
                style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center
                ),
                fontSize = (cardWidth/35).sp,
                color = Color.White,
            )
        }
    }

    Log.d("BOOK_DEBUG", "BookCoverPlaceholder: WIDTH-> $cardWidth")
}