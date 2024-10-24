package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.model.BookFormat
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookLanguage
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.ui.books.ChoiceSelector
import com.example.ecommercefront_end.viewmodels.BookViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun InsertProductScreen(viewModel: BookViewModel, navController: NavHostController){

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var publisher by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var pages by remember { mutableStateOf("") }
    var edition by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var publishDate by remember { mutableStateOf(LocalDate.now()) }
    var weight by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    OutlinedTextField(
                        value = publisher,
                        onValueChange = {
                            publisher = it
                        },
                        label = { Text("Publisher") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = price,
                        onValueChange = {
                            price = it
                        },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = stock,
                        onValueChange = {
                            stock = it
                        },
                        label = { Text("Stock") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = isbn,
                        onValueChange = {
                            isbn = it
                        },
                        label = { Text("ISBN") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = pages,
                        onValueChange = {
                            pages = it
                        },
                        label = { Text("Pages") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = edition,
                        onValueChange = {
                            edition = it
                        },
                        label = { Text("Edition") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                var formats: ArrayList<String> = ArrayList();

                BookFormat.entries.forEach { format ->
                    formats.add(format.toString())
                }

                item {
                    ChoiceSelector(
                        parameter = "format",
                        choiches = formats,
                        selectedChoice = "Select Format",
                        onChoiceSelected = { selectedChoice ->
                            format = selectedChoice
                        })

                    Spacer(modifier = Modifier.height(10.dp))
                }

                var genres: ArrayList<String> = ArrayList();

                BookGenre.entries.forEach { genre ->
                    genres.add(genre.toString())
                }

                item {
                    ChoiceSelector(
                        parameter = "genre",
                        choiches = genres,
                        selectedChoice = "Select Genre",
                        onChoiceSelected = { selectedChoice ->
                            genre = selectedChoice
                        })

                    Spacer(modifier = Modifier.height(10.dp))
                }

                var languages: ArrayList<String> = ArrayList();

                BookLanguage.entries.forEach { language ->
                    languages.add(language.toString())
                }

                item {
                    ChoiceSelector(
                        parameter = "language",
                        choiches = languages,
                        selectedChoice = "Select Language",
                        onChoiceSelected = { selectedChoice ->
                            language = selectedChoice
                        })

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = age,
                        onValueChange = {
                            age = it
                        },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    insertDate{selectedDate -> publishDate = selectedDate}

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = {
                            weight = it
                        },
                        label = { Text("Weight") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.insertBook(
                    SaveBook(
                        title = title,
                        author = author,
                        publisher = publisher,
                        price = price.toDouble(),
                        stock = stock.toInt(),
                        ISBN = isbn,
                        pages = pages.toInt(),
                        edition = edition,
                        format = BookFormat.valueOf(format),
                        genre = BookGenre.valueOf(genre),
                        language = BookLanguage.valueOf(language),
                        age = age.toInt(),
                        publishDate = publishDate,
                        weight = weight.toDouble()
                    )
                )
                viewModel.fetchBooksData()
                navController.navigate("admin-home") {
                    popUpTo("admin-home") {
                        saveState = true
                    }
                }
            },
            enabled = title.isNotEmpty() && author.isNotEmpty() && publisher.isNotEmpty() &&
                    price.isNotEmpty() && stock.isNotEmpty() && isbn.isNotEmpty() &&
                    pages.isNotEmpty() && edition.isNotEmpty() && format.isNotEmpty() &&
                    genre.isNotEmpty() && language.isNotEmpty() && age.isNotEmpty()
                    && weight.isNotEmpty()
        ) {
            Text("Save Product", style = MaterialTheme.typography.bodyLarge)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun insertDate(onDateSelected: (LocalDate) -> Unit) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        onValueChange = { },
        label = { Text("Publish Date") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Filled.CalendarToday, contentDescription = "Select date")
            }
        }
    )
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Back")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant().toEpochMilli(),
                yearRange = IntRange(1900, LocalDate.now().year),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val selected = Instant.ofEpochMilli(utcTimeMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        return selected.isBefore(LocalDate.now()) || selected.isEqual(LocalDate.now())
                    }
                }
            )

            // Aggiorna selectedDate quando la data selezionata cambia
            LaunchedEffect(datePickerState.selectedDateMillis) {
                if (datePickerState.selectedDateMillis != null) {
                    val newDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    selectedDate = newDate
                    // Passa la data selezionata al lambda
                    onDateSelected(newDate)
                }
            }

            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}