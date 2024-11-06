package com.example.ecommercefront_end.ui.user

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.ui.home.ProductCard
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.BookViewModel

@Composable
fun AccountManagerScreen(viewModel: AccountViewModel, bookViewModel: BookViewModel, navHostController: NavHostController, onLogout: () -> Unit) {

    val userDetails by viewModel.userDetails.collectAsState()

    val purchasedBooks by viewModel.purchasedBooks.collectAsState()

    val isLoggingOut by viewModel.isLoggingOut.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPurchasedBooks()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Account Manager") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    }){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), verticalArrangement = Arrangement.SpaceAround
        ) {
            if(isLoggingOut){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }else {
                userDetails?.let { UserCard(it) }
                OptionsSection(navHostController)
                if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN" && purchasedBooks.isNotEmpty())
                    PurchasedHistory(
                        purchasedBooks,
                        bookViewModel = bookViewModel,
                        navController = navHostController
                    )
                Buttons(viewModel, navHostController, onLogout = onLogout)
            }
        }
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
                    Button(onClick = {navHostController.navigate("orders"){
                        popUpTo("account-manager") { saveState = true }
                    }}) {
                        Text(text = "My Orders")
                    }
                }
            }
            if(SessionManager.user != null && SessionManager.user!!.role!="ROLE_ADMIN") {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(onClick = { navHostController.navigate("groups"){
                        popUpTo("account-manager") { saveState = true }
                    }}) {
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
fun PurchasedHistory(history: List<Book>, bookViewModel: BookViewModel, navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Purchased Books",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            items(history, key = {it.id}) { book ->
                BookCover(book, bookViewModel, navController)
            }
        }
    }

}

@Composable
fun Buttons(viewModel: AccountViewModel, navHostController: NavHostController, onLogout: ()->Unit){
    val context = LocalContext.current

    Column {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { navHostController.navigate("change-password"){
                popUpTo("account-manager") { saveState = true }
            }}) {
                Text(text = "Change Password")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                Log.d("LOGOUT DEBUG", "User: ${SessionManager.getUser()}")
                SessionManager.getUser().let { viewModel.logoutUser(it.userId, onLogout = {
                    onLogout()
                    SessionManager.clearSession()
                    })
                }
                navHostController.navigate("home"){
                    popUpTo(0) {inclusive= true}
                }
            }) {
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