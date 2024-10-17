package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.viewmodels.BookViewModel
import java.util.Locale


@Preview
@Composable
fun BooksFilterScreen(viewModel: BookViewModel, onDismiss: () -> Unit){

    LaunchedEffect(Unit) {
        viewModel.fetchBooksData();
    }

    val minBookPrice by viewModel.minPrice.collectAsState()
    val maxBookPrice by viewModel.maxPrice.collectAsState()

    val minBookAge by viewModel.minAge.collectAsState()
    val maxBookAge by viewModel.maxAge.collectAsState()

    val minBookPages by viewModel.minPages.collectAsState()
    val maxBookPages by viewModel.maxPages.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    
    if(isLoading)
    {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else {
        Dialog(
            onDismissRequest = {onDismiss()}
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    RangeSelector(
                        viewModel,
                        "Price (Euros)",
                        minBookPrice!!.toFloat(),
                        maxBookPrice!!.toFloat(),
                        onValueChange = { start, end ->
                            val formattedStart =
                                String.format(Locale.ITALY, "%.2f", start).replace(",", ".")
                                    .toFloat();
                            val formattedEnd =
                                String.format(Locale.ITALY, "%.2f", end).replace(",", ".")
                                    .toFloat();
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    RangeSelector(
                        viewModel,
                        "Age",
                        minBookAge!!.toFloat(),
                        maxBookAge!!.toFloat(),
                        onValueChange = { start, end ->
                            val formattedStart =
                                String.format(Locale.ITALY, "%.2f", start).replace(",", ".")
                                    .toFloat();
                            val formattedEnd =
                                String.format(Locale.ITALY, "%.2f", end).replace(",", ".")
                                    .toFloat();
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    var genres = emptyArray<String>()

                    /*BookGenre.entries.forEach{
                        genre ->genres.
                    }
                    ChoiceSelector(choiches = BookGenre.entries, selectedChoice = "All", onChoiceSelected = {})*/

                    Spacer(modifier = Modifier.height(10.dp))

                    RangeSelector(
                        viewModel,
                        "Number of pages",
                        minBookPages!!.toFloat(),
                        maxBookPages!!.toFloat(),
                        onValueChange = { start, end ->
                            val formattedStart =
                                String.format(Locale.ITALY, "%.2f", start).replace(",", ".")
                                    .toFloat();
                            val formattedEnd =
                                String.format(Locale.ITALY, "%.2f", end).replace(",", ".")
                                    .toFloat();
                        })

                    Spacer(modifier = Modifier.height(10.dp))

                    //BARRA PER PESO

                    Spacer(modifier = Modifier.height(10.dp))

                    //SCELTA PER FORMATO

                    Spacer(modifier = Modifier.height(10.dp))

                    //BARRA PER ANNO PUBBLICAZIONE

                    Spacer(modifier = Modifier.height(10.dp))

                    //BARRA PER ANNO INSERIMENTO
                }
            }
        }
    }
}

@Composable
fun RangeSelector(viewModel: BookViewModel,parameter: String, value1: Float, value2: Float, onValueChange: (Float, Float) -> Unit){

    var sliderPosition by remember { mutableStateOf(value1..value2) }

    Column {
        Text(text = parameter,
            fontWeight = FontWeight.Bold
        )
        RangeSlider(
            value = sliderPosition,
            onValueChange = { range -> sliderPosition = range
                onValueChange(range.start, range.endInclusive)},
            valueRange = value1..value2,
            onValueChangeFinished = {
            //viewModel.updatePriceSliderValue(sliderPosition)
            },
            )
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            if (parameter == "Price") {
                Text(text = "€ %.2f".format(Locale.ITALY, sliderPosition.start).replace(",", "."))
                Text(text = "€ %.2f".format(Locale.ITALY, sliderPosition.endInclusive).replace(",", "."))
            } else {
                Text(text = "${sliderPosition.start.toInt()}")
                Text(text = "${sliderPosition.endInclusive.toInt()}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceSelector(
    choiches: ArrayList<String>,
    selectedChoice: String,      // Genere attualmente selezionato
    onChoiceSelected: (String) -> Unit  // Callback per la selezione del genere
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedChoice) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select an option") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            choiches.forEach(){
                choice ->
                DropdownMenuItem(
                text = {
                    Text(text = choice)},
                onClick = {
                    selectedText = choice
                    onChoiceSelected(choice)
                    expanded = false
                }
            ) }
        }
    }
}