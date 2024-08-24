package com.example.ecommercefront_end

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommercefront_end.network.CartApiService
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.repository.AuthRepository
import com.example.ecommercefront_end.repository.CartRepository
import com.example.ecommercefront_end.repository.HomeRepository
import com.example.ecommercefront_end.repository.WishlistRepository
import com.example.ecommercefront_end.ui.cart.CartScreen
import com.example.ecommercefront_end.ui.home.BookDetailsScreen
import com.example.ecommercefront_end.ui.home.HomeScreen
import com.example.ecommercefront_end.ui.theme.EcommerceFrontEndTheme
import com.example.ecommercefront_end.ui.user.AccountManagerScreen
import com.example.ecommercefront_end.ui.user.AddressesScreen
import com.example.ecommercefront_end.ui.user.MyAccountScreen
import com.example.ecommercefront_end.ui.user.UserAuthScreen
import com.example.ecommercefront_end.ui.wishlist.WishlistsScreen
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import com.example.ecommercefront_end.viewmodels.CartViewModel
import com.example.ecommercefront_end.viewmodels.HomeViewModel
import com.example.ecommercefront_end.viewmodels.LoginViewModel
import com.example.ecommercefront_end.viewmodels.RegistrationViewModel
import com.example.ecommercefront_end.viewmodels.WishlistViewModel


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
                    val navController = rememberNavController()
                    NavigationView(navController)
                    SessionManager.init(this)
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
    val accountViewModel = remember { AccountViewModel(repository = AccountRepository(RetrofitClient.userApiService)) }

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
            composable("wishlist") {
                selectedIndex.value = 3
                val _wishlistApiService = RetrofitClient.wishlistApiService
                val _wishlistItemApiService = RetrofitClient.wishlistItemApiService
                val repository = WishlistRepository(_wishlistApiService, _wishlistItemApiService)
                WishlistsScreen(viewModel = WishlistViewModel(repository), navController = navController)

            }
            composable("userAuth") {
                selectedIndex.value = 1
                val _authApiService = RetrofitClient.authApiService
                val repository = AuthRepository(_authApiService)
                UserAuthScreen(loginViewModel = LoginViewModel(repository), registrationViewModel = RegistrationViewModel(repository), navController)
            }

            composable("account-manager") {
                selectedIndex.value = 1
                val _userApiService = RetrofitClient.userApiService
                val repository = AccountRepository(_userApiService)
                AccountManagerScreen(viewModel = AccountViewModel(repository), navController)
            }
            composable("my-account") {
                val _userApiService = RetrofitClient.userApiService
                val repository = AccountRepository(_userApiService)
                MyAccountScreen(
                    viewModel = AccountViewModel(repository),
                    navHostController = navController)
            }

            composable("addresses") {
                val _addressApiService = RetrofitClient.addressApiService
                val repository = AddressRepository(_addressApiService)
                AddressesScreen(
                    viewModel = AddressViewModel(repository), navController)
            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navHostController: NavHostController) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBackIcon by remember(currentBackStackEntry) { derivedStateOf { navHostController.previousBackStackEntry != null } }
    val isSearchVisible = currentRoute != "userAuth" && currentRoute != "account-manager" && currentRoute != "my-account" && currentRoute != "addresses"
    val colorScheme = MaterialTheme.colorScheme

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
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.statusBars.only(WindowInsetsSides.Top)
        ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.primary, // Usa il colore primario del tema
            titleContentColor = colorScheme.onPrimary, // Usa il colore onPrimary del tema
            navigationIconContentColor = colorScheme.onPrimary, // Usa il colore onPrimarydel tema
            actionIconContentColor = colorScheme.onPrimary // Usa il colore onPrimary del tema
        )
    )
}

@Composable
fun SearchBar() {
    // Placeholder per la SearchBar
    Text(text = "🔍 Search...", modifier = Modifier.padding(8.dp))
}


@PreviewParameter(PreviewParameterProvider::class)
@Composable
fun BottomBar(selectedIndex: MutableState<Int>, navHostController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .height(67.dp).drawBehind {
                val borderSize = 1.dp.toPx()
                val borderColor = Color.White.toArgb()

                drawLine(
                    color = Color(borderColor),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = borderSize
                )
            }
            )
    {
        BottomNavigationItem(
            selected = selectedIndex.value == 0,
            onClick = {
                selectedIndex.value = 0
                navHostController.navigate("home") {
                    // ...
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,contentDescription = stringResource(R.string.home),
                    modifier = if (selectedIndex.value == 0) {
                        Modifier.shadow(
                            elevation = 4.dp, //Imposta l'elevazione dell'ombra
                            shape = CutCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                    } else Modifier
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurface
        )
        // Ripeti per gli altri elementi della Navigation Bar
        // ...
        BottomNavigationItem(
            selected = selectedIndex.value == 1,
            onClick = {
                selectedIndex.value = 1
                if (SessionManager.user == null) {
                    navHostController.navigate("userAuth") {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } else {
                    navHostController.navigate("account-manager") {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.user),
                modifier = if (selectedIndex.value == 1) {
                    Modifier.shadow(
                        elevation = 4.dp, //Imposta l'elevazione dell'ombra
                        shape = CutCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                } else Modifier
            )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurface
        )
        BottomNavigationItem(
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
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(R.string.cart),
                    modifier = if (selectedIndex.value == 2) {
                        Modifier.shadow(
                            elevation = 4.dp, //Imposta l'elevazione dell'ombra
                            shape = CutCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                    } else Modifier
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurface
        )
        BottomNavigationItem(
            selected = selectedIndex.value == 3,
            onClick = {
                selectedIndex.value = 3
                navHostController.navigate("wishlist") {
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.favorite),
                    modifier = if (selectedIndex.value == 3) {
                        Modifier.shadow(
                            elevation = 4.dp, //Imposta l'elevazione dell'ombra
                            shape = CutCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                    } else Modifier
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurface
        )
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
            NavigationView(navController = navHostController)
        }
    }
}
@Composable
fun AddToCartFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Create, contentDescription = stringResource(id = R.string.create))
    }
}
