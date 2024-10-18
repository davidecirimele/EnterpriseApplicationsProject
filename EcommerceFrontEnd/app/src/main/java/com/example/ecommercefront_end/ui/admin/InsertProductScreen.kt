package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.BookFormat
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookLanguage
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.ui.home.ChoiceSelector
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import com.example.ecommercefront_end.viewmodels.BookViewModel
import com.example.ecommercefront_end.viewmodels.RegistrationViewModel
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

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
    var publishDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp).weight(1f),
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
                    OutlinedTextField(
                        value = publishDate,
                        onValueChange = {
                            publishDate = it
                        },
                        label = { Text("publishDate") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )

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
                        publishDate = LocalDate.parse(publishDate),
                        weight = weight.toDouble()
                    )
                )
                navController.navigate("admin-home") {
                    popUpTo("admin-home") {
                        saveState = true
                    }
                }
            },
            enabled = title.isNotEmpty() && author.isNotEmpty() && publisher.isNotEmpty() &&
                    price.isNotEmpty() && stock.isNotEmpty() && isbn.isNotEmpty() &&
                    pages.isNotEmpty() && edition.isNotEmpty() && format.isNotEmpty() &&
                    genre.isNotEmpty() && language.isNotEmpty() && age.isNotEmpty() &&
                    publishDate.isNotEmpty() && weight.isNotEmpty()
        ) {
            Text("Save Product", style = MaterialTheme.typography.bodyLarge)
        }
    }

}