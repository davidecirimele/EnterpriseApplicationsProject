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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.ui.cart.CartScreen
import com.example.ecommercefront_end.ui.theme.EcommerceFrontEndTheme
import com.example.ecommercefront_end.viewmodels.CartViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceFrontEndTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage()
                }
            }
        }
    }
}

@Composable
fun NavigationView(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "home") {
        composable("home") {
            HomeProducts()
        }
        composable("user") {
            UserScreen()
        }
        composable("cart") {
            CartScreen(viewModel = CartViewModel(), onCheckoutClick = { /* Add your action here */ })

            }
        }
        composable("favorite") {
            CartScreen()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navHostController: NavHostController) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val showBackIcon by remember(currentBackStackEntry) { derivedStateOf { navHostController.previousBackStackEntry != null } }
    TopAppBar(title = { Text(stringResource(R.string.app_name))},
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
            NavigationBarItem(selected = selectedIndex.value == 0, onClick = {
                selectedIndex.value = 0
                navHostController.navigate("home")
            }, icon = {
                Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home))
            })
            NavigationBarItem(selected = selectedIndex.value == 1, onClick = {
                selectedIndex.value = 1
                navHostController.navigate("user")
            }, icon = {
                Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.user))
            })
            NavigationBarItem(selected = selectedIndex.value == 2, onClick = {
                selectedIndex.value = 2
                navHostController.navigate("cart")
            }, icon = {
                Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(R.string.cart))
            })
            NavigationBarItem(selected = selectedIndex.value == 3, onClick = {
                selectedIndex.value = 3
                navHostController.navigate("favorite")
            }, icon = {
                Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.favorite))
            })
        }
    }
}

@Composable
fun HomePage() {
    val navHostController = rememberNavController()
    val selectedIndex = remember { mutableIntStateOf(0) }
    Scaffold(topBar = { TopBar(navHostController) },
        bottomBar = { BottomBar(selectedIndex, navHostController) },
        floatingActionButton = { AddToCartFloatingButton { /* Add your action here */ } },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigationView(navHostController = navHostController)
        }
    }
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
fun UserScreen() {
    Text(text = "User Screen")
}

/*@Composable
fun CartScreen() {
    Text(text = "Cart Screen")
}*/
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