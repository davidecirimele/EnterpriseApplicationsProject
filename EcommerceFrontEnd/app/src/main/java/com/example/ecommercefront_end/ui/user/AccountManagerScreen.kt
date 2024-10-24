package com.example.ecommercefront_end.ui.user

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.AccountViewModel

@Composable
fun AccountManagerScreen(viewModel: AccountViewModel, navHostController: NavHostController) {

    val userDetails by viewModel.userDetails.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp), verticalArrangement = Arrangement.SpaceEvenly){
        userDetails?.let { UserCard(it) }
        OptionsSection(navHostController)
        if(SessionManager.user != null && SessionManager.user!!.role!="ROLE_ADMIN")
        HistorySection()
        Buttons(navHostController)
    }
}

@Composable
fun UserCard(userDetails: UserDetails){
    Row(modifier = Modifier.fillMaxWidth()) {
        Column() {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = "Welcome back, ${userDetails.firstName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

        }
    }
}


@Composable
fun OptionsSection(navHostController: NavHostController){
    Box(modifier = Modifier.fillMaxWidth(),  contentAlignment = Alignment.Center)
    {
        Column {
            Row {
                Button(onClick = {navHostController.navigate("my-account"){
                    popUpTo("account-manager") { saveState = true }
                }}) {
                    Text(text = "My Account")
                }
                if(SessionManager.user != null && SessionManager.user!!.role!="ROLE_ADMIN") {
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "My Orders")
                    }
                }
            }
            if(SessionManager.user != null && SessionManager.user!!.role!="ROLE_ADMIN") {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "My Groups")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "My Reviews")
                    }
                }
            }
        }
    }
}


@Composable
fun PurchasedHistoryCard(history: List<Pair<String,String>>){
    Column {
        Text(
            text = "Purchased Books",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(state = ScrollState(1))
        ) {
            history.forEach { (title, author) ->
                BookCover(title = title, author = author)
            }

        }
    }
}

@Composable
fun HistorySection(){
    val books = listOf(
        "Book One" to "Author A",
        "Book Two" to "Author B",
        "Book Three" to "Author C"
    )
    Column(verticalArrangement = Arrangement.SpaceEvenly){
        PurchasedHistoryCard(books)
    }
}

@Composable
fun Buttons(navHostController: NavHostController){
    Column {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Change Password")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { SessionManager.clearSession()}) {
                Text(text = "Logout")
            }
        }
    }
}

/*
@Preview
@Composable
fun AccountScreenPreview(){
    val _userApiService = RetrofitClient.userApiService
    val repository = AccountRepository(_userApiService)
    val viewModel = AccountViewModel(repository)
    val navController = rememberNavController()
    AccountManagerScreen(viewModel, navHostController = navController)
}*/