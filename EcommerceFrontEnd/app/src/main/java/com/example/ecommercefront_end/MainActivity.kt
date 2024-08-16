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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.network.CartApiService
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.CartRepository
import com.example.ecommercefront_end.ui.cart.CartScreen
import com.example.ecommercefront_end.ui.theme.EcommerceFrontEndTheme
import com.example.ecommercefront_end.viewmodels.CartViewModel


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
    val selectedIndex = remember { mutableIntStateOf(0) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = { TopBar(navController) }, // Rimuovi la condizione
        bottomBar = { BottomBar(selectedIndex, navController) } // Rimuovi la condizione
    )
    { innerPadding ->
        NavHost(navController = navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { HomePage(navController) }
            composable("cart") {  val _cartApiService = RetrofitClient.cartApiService

                val repository = CartRepository(_cartApiService)

                CartScreen(viewModel = CartViewModel(repository), onCheckoutClick = { /* Add your action here */ })
            }
            composable("favorite") { FavoriteScreen() }
            composable("userAuth") { UserAuthScreen(navController) }
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
    val showBackIcon by remember(currentBackStackEntry) { derivedStateOf { navHostController.previousBackStackEntry != null } }
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
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
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
            }
        }
    )
}

@Composable
fun BottomBar(selectedIndex: MutableState<Int>, navHostController: NavHostController) {
    BottomAppBar {
        NavigationBar {
            NavigationBarItem(
                selected = selectedIndex.value == 0,
                onClick = {
                    selectedIndex.value = 0
                    navHostController.navigate("home")
                },
                icon = {
                    Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home))
                }
            )
            NavigationBarItem(
                selected = selectedIndex.value == 1,
                onClick = {
                    selectedIndex.value = 1
                    navHostController.navigate("userAuth")
                },
                icon = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.user))
                }
            )
            NavigationBarItem(
                selected = selectedIndex.value == 2,
                onClick = {
                    selectedIndex.value = 2
                    navHostController.navigate("cart")
                },
                icon = {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(R.string.cart))
                }
            )
            NavigationBarItem(
                selected = selectedIndex.value == 3,
                onClick = {
                    selectedIndex.value = 3
                    navHostController.navigate("favorite")
                },
                icon = {
                    Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.favorite))
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    val selectedIndex = remember { mutableIntStateOf(0) }

}


@Composable
fun AddToCartFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Create, contentDescription = stringResource(id = R.string.create))
    }
}

@Composable
fun HomeScreen() {
    Text(text = "Home Screen")
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