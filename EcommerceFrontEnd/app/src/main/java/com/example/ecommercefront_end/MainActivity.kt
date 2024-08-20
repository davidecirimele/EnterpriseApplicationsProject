package com.example.ecommercefront_end


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommercefront_end.network.CartApiService
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.CartRepository
import com.example.ecommercefront_end.repository.HomeRepository
import com.example.ecommercefront_end.ui.User.UserAuthScreen
import com.example.ecommercefront_end.ui.cart.CartScreen
import com.example.ecommercefront_end.ui.theme.EcommerceFrontEndTheme
import com.example.ecommercefront_end.viewmodels.CartViewModel
import com.example.ecommercefront_end.viewmodels.HomeViewModel
import com.example.ecommercefront_end.network.BooksApiService
import com.example.ecommercefront_end.ui.home.BookDetailsScreen
import com.example.ecommercefront_end.ui.home.HomeScreen
import androidx.compose.runtime.collectAsState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceFrontEndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationView(navController)
                }
            }
        }
    }
}


@Composable
fun NavigationView(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val selectedIndex = remember { mutableIntStateOf(0) }

    // Usa remember per mantenere i ViewModel
    val homeViewModel = remember { HomeViewModel(repository = HomeRepository(RetrofitClient.booksApiService)) }
    val cartViewModel = remember { CartViewModel(repository = CartRepository(RetrofitClient.cartApiService)) }


    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(selectedIndex, navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                selectedIndex.value = 0
                HomeScreen(homeViewModel = homeViewModel, navController)
            }

            composable("/books_details/{idBook}", arguments = listOf(navArgument("idBook") { type = NavType.LongType })) { backStackEntry ->
                val idBook = backStackEntry.arguments?.getLong("idBook") ?: 0L

                // Carica il libro corrispondente all'id
                LaunchedEffect(idBook) {
                    homeViewModel.loadBook(idBook)
                }

                // Osserva i cambiamenti del libro
                val book by homeViewModel.bookFlow.collectAsState()

                book?.let {
                    BookDetailsScreen(book = it)
                } ?: Text("Libro non trovato")
            }

            composable("cart") {
                selectedIndex.value = 2
                CartScreen(viewModel = cartViewModel, onCheckoutClick = { /* Add your action here */ })
            }
            composable("favorite") {
                selectedIndex.value = 3
                FavoriteScreen()
            }
            composable("userAuth") {
                selectedIndex.value = 1
                UserAuthScreen(navController)
            }

        composable("account-manager") {
            val _userApiService = RetrofitClient.userApiService
            val repository = AccountRepository(_userApiService)
            AccountManagerScreen(viewModel = AccountViewModel(repository), navHostController)
        }
        composable("my-account") {
            val _userApiService = RetrofitClient.userApiService
            val repository = AccountRepository(_userApiService)
            MyAccountScreen(viewModel = AccountViewModel(repository), onCheckoutClick = { /* Add your action here */ })

        }

    }
}



@Composable
fun HomeProducts() {
    TODO("Not yet implemented")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navHostController: NavHostController) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBackIcon by remember(currentBackStackEntry) { derivedStateOf { navHostController.previousBackStackEntry != null } }
    val isSearchVisible = currentRoute != "userAuth"

    TopAppBar(
        title = {
            if (isSearchVisible) {
                SearchBar()
            } else {
                Text(stringResource(R.string.app_name))
            }
        },
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(
                        Icons.Filled.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (isSearchVisible) {
                IconButton(onClick = { /* Handle settings click */ }) {
                    Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
                }
            }
        }
    )
}

@Composable
fun SearchBar() {
    // Placeholder per la SearchBar
    Text(text = "🔍 Search...", modifier = Modifier.padding(8.dp))
}


@Composable
fun BottomBar(selectedIndex: MutableState<Int>, navHostController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex.value == 0,
            onClick = {
                selectedIndex.value = 0
                navHostController.navigate("home") {
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = stringResource(R.string.home)
                )
            }
        )
        NavigationBarItem(
            selected = selectedIndex.value == 1,
            onClick = {
                selectedIndex.value = 1
                navHostController.navigate("userAuth") {
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.user)
                )
            }
        )
        NavigationBarItem(
            selected = selectedIndex.value == 2,
            onClick = {
                selectedIndex.value = 2
                navHostController.navigate("cart") {
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(R.string.cart)
                )
            }
        )
        NavigationBarItem(
            selected = selectedIndex.value == 3,
            onClick = {
                selectedIndex.value = 3
                navHostController.navigate("favorite") {
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.favorite)
                )
            }
        )
    }
}



@Composable
fun AddToCartFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Create, contentDescription = stringResource(id = R.string.create))
    }
}


@Composable
fun CartScreen() {
    Text(text = "Cart Screen")
}

@Composable
fun FavoriteScreen() {
    Text(text = "Favorite Screen")
}

/*
sealed class Screen(val route: String, val icon: ImageVector) {
    object Home : Screen("home", Icons.Default.Home)
    object Cart : Screen("cart", Icons.Default.ShoppingCart)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EcommerceFrontEndTheme {
        MyApp()
    }
}*/