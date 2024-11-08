package com.example.ecommercefront_end.ui.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.BookFormat
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookLanguage
import com.example.ecommercefront_end.viewmodels.BookViewModel
import java.time.LocalDate
import java.util.Locale


@Composable
fun BooksFilterScreen(viewModel: BookViewModel, onSearchBooks: (BookFilter) -> Unit, currentRoute: String?, onDismiss: () -> Unit){

    val minBookPrice by viewModel.minPrice.collectAsState()
    val maxBookPrice by viewModel.maxPrice.collectAsState()

    val minBookAge by viewModel.minAge.collectAsState()
    val maxBookAge by viewModel.maxAge.collectAsState()

    val minBookPages by viewModel.minPages.collectAsState()
    val maxBookPages by viewModel.maxPages.collectAsState()

    val minWeight by viewModel.minWeight.collectAsState()
    val maxWeight by viewModel.maxWeight.collectAsState()

    var filterOptions by remember{mutableStateOf(BookFilter())}

    val startingPublicationYear by viewModel.startingPublicationYear.collectAsState()

    val isLoading by viewModel.isLoadingData.collectAsState()
    
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
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 600.dp)
                        .padding(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text(
                            text = "Filters",
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        IconButton(modifier = Modifier.align(Alignment.CenterVertically),onClick = {
                            onDismiss()
                        }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close Filters",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.padding(8.dp).weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        if(currentRoute == "admin-catalogue") {
                            item {

                                val choices: List<Boolean> = listOf(true, false)

                                ChoiceSelector(
                                    parameter = "Available",
                                    choiches = choices.map { it.toString() },
                                    selectedChoice = choices.first().toString(),
                                    onChoiceSelected = { selectedChoice ->
                                        val booleanChoice = selectedChoice.toBoolean()
                                        filterOptions = filterOptions.copy(available = booleanChoice)
                                    }
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        item {
                            RangeSelector(
                                viewModel,
                                "Price (Euros)",
                                minBookPrice.toFloat(),
                                maxBookPrice.toFloat(),
                                onValueChange = { start, end ->
                                    val formattedStart =
                                        String.format(Locale.ITALY, "%.2f", start).replace(",", ".")
                                            .toFloat();
                                    val formattedEnd =
                                        String.format(Locale.ITALY, "%.2f", end).replace(",", ".")
                                            .toFloat();
                                    filterOptions = filterOptions.copy(minPrice = formattedStart.toDouble())
                                    filterOptions = filterOptions.copy(maxPrice = formattedEnd.toDouble())
                                }, oldValue1 = filterOptions.minPrice, oldValue2 = filterOptions.maxPrice)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        item {
                            RangeSelector(
                                viewModel,
                                "Age",
                                minBookAge.toFloat(),
                                maxBookAge.toFloat(),
                                onValueChange = { start, end ->
                                    val formattedStart =
                                        String.format(Locale.ITALY, "%.0f", start).replace(",", ".")
                                            .toFloat();
                                    val formattedEnd =
                                        String.format(Locale.ITALY, "%.0f", end).replace(",", ".")
                                            .toFloat();

                                    filterOptions = filterOptions.copy(minAge = formattedStart.toInt())
                                    filterOptions = filterOptions.copy(maxAge = formattedEnd.toInt())
                                }, oldValue1 = filterOptions.minAge, oldValue2 = filterOptions.maxAge)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        var genres: ArrayList<String> = ArrayList();

                        BookGenre.entries.forEach { genre ->
                            genres.add(genre.toString())
                        }

                        item {
                            ChoiceSelector(
                                parameter = "Genre",
                                choiches = genres,
                                selectedChoice = filterOptions.genre.toString(),
                                onChoiceSelected = { selectedChoice ->
                                    filterOptions = filterOptions.copy(genre = BookGenre.valueOf(selectedChoice))
                                })

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        item {
                            RangeSelector(
                                viewModel,
                                "Number of pages",
                                minBookPages.toFloat(),
                                maxBookPages.toFloat(),
                                onValueChange = { start, end ->
                                    val formattedStart =
                                        String.format(Locale.ITALY, "%.0f", start).replace(",", ".")
                                            .toFloat();
                                    val formattedEnd =
                                        String.format(Locale.ITALY, "%.0f", end).replace(",", ".")
                                            .toFloat();

                                    filterOptions = filterOptions.copy(minPages = formattedStart.toInt())
                                    filterOptions = filterOptions.copy(maxPages = formattedEnd.toInt())
                                }, oldValue1 = filterOptions.minPages, oldValue2 = filterOptions.maxPages)

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        item {
                            ValueSlider(
                                viewModel,
                                "Weight (Kg)",
                                minWeight.toFloat(),
                                maxWeight.toFloat(),
                                filterOptions.weight
                            );

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        var formats: ArrayList<String> = ArrayList();

                        BookFormat.entries.forEach { format ->
                            formats.add(format.toString())
                        }
                        item {
                            ChoiceSelector(
                                parameter = "Format",
                                choiches = formats,
                                selectedChoice = filterOptions.format.toString(),
                                onChoiceSelected = { selectedChoice ->
                                    filterOptions = filterOptions.copy(format = BookFormat.valueOf(selectedChoice))
                                })

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        var languages: ArrayList<String> = ArrayList();

                        BookLanguage.entries.forEach { language ->
                            languages.add(language.toString())
                        }
                        item {
                            ChoiceSelector(
                                parameter = "Language",
                                choiches = languages,
                                selectedChoice = filterOptions.language.toString(),
                                onChoiceSelected = { selectedChoice ->
                                    filterOptions = filterOptions.copy(language = BookLanguage.valueOf(selectedChoice))
                                })

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        item {

                            RangeSelector(
                                viewModel,
                                "Publication Year",
                                startingPublicationYear.year.toFloat(),
                                LocalDate.now().year.toFloat(),
                                onValueChange = { start, end ->
                                    val formattedStart =
                                        String.format(Locale.ITALY, "%.0f", start).replace(",", ".")
                                            .toFloat();
                                    val formattedEnd =
                                        String.format(Locale.ITALY, "%.0f", end).replace(",", ".")
                                            .toFloat();

                                    filterOptions = filterOptions.copy(minPublishDate = LocalDate.parse("${formattedStart.toInt()}-01-01"))
                                    filterOptions = filterOptions.copy(maxPublishDate = LocalDate.parse("${formattedEnd.toInt()}-12-31"))
                                }, oldValue1 = filterOptions.minPublishDate?.year, oldValue2 = filterOptions.maxPublishDate?.year
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                        }

                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        Button(onClick = {
                            onSearchBooks(filterOptions)
                            onDismiss()}) {
                            Text(text = "Filter Books")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T : Number> RangeSelector(viewModel: BookViewModel,parameter: String, value1: Float, value2: Float, onValueChange: (Float, Float) -> Unit, oldValue1: T?, oldValue2: T?){

    var sliderPosition by remember { mutableStateOf(value1..value2) }

    LaunchedEffect(value1, value2, oldValue1, oldValue2) {
        sliderPosition = if (oldValue1 != null && oldValue2 != null) {
            oldValue1.toFloat()..oldValue2.toFloat()
        } else if (oldValue1 != null) {
            oldValue1.toFloat()..value2
        } else if (oldValue2 != null) {
            value1..oldValue2.toFloat()
        } else {
            value1..value2
        }
    }


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
            if (parameter == "Price (Euros)") {
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
    parameter: String,
    choiches: List<String>,
    selectedChoice: String,
    onChoiceSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf("All") }

    LaunchedEffect(selectedChoice) {
        if (selectedChoice != "null" && selectedChoice != null) {
            selectedText = selectedChoice
        }
    }

    Column {
        Text(
            text = parameter,
            fontWeight = FontWeight.Bold
        )
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
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()  // This helps ensure correct positioning of the dropdown menu
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                choiches.forEach { choice ->
                    DropdownMenuItem(
                        text = { Text(choice) },
                        onClick = {
                            selectedText = choice
                            onChoiceSelected(choice)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun <T : Number> ValueSlider(viewModel: BookViewModel,parameter: String, value1: Float, value2: Float, oldValue1: T?) {
    var sliderPosition by remember { mutableFloatStateOf(value1) }

    LaunchedEffect(oldValue1) {
        if (oldValue1 != null)
            sliderPosition = oldValue1.toFloat()
    }

    Column {
        Text(text = parameter, fontWeight = FontWeight.Bold)
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = value1..value2
        )
        Text(text = "%.2f".format(Locale.ITALY, sliderPosition).replace(",", "."))
    }
}