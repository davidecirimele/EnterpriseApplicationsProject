package com.example.ecommercefront_end

import CheckoutViewModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
//import androidx.compose.material3.BottomNavigation
//import androidx.compose.material3.BottomNavigationItem


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.repository.AdminRepository
import com.example.ecommercefront_end.repository.AuthRepository
import com.example.ecommercefront_end.repository.BookRepository
import com.example.ecommercefront_end.repository.CartRepository
import com.example.ecommercefront_end.repository.CheckoutRepository
import com.example.ecommercefront_end.repository.GroupRepository
import com.example.ecommercefront_end.repository.TransactionRepository

import com.example.ecommercefront_end.repository.WishlistRepository
import com.example.ecommercefront_end.ui.admin.AdminCatalogueScreen
import com.example.ecommercefront_end.ui.user.Address.EditAddressScreen
import com.example.ecommercefront_end.ui.cart.CartScreen
import com.example.ecommercefront_end.ui.home.BookDetailsScreen
import com.example.ecommercefront_end.ui.home.HomeScreen
import com.example.ecommercefront_end.ui.theme.EcommerceFrontEndTheme
import com.example.ecommercefront_end.ui.user.AccountManagerScreen
import com.example.ecommercefront_end.ui.user.Address.AddressesScreen
import com.example.ecommercefront_end.ui.admin.AdminHomeScreen
import com.example.ecommercefront_end.ui.admin.AdminSingleBookScreen
import com.example.ecommercefront_end.ui.admin.AdminUserDetailsScreen
import com.example.ecommercefront_end.ui.admin.AdminUsersListScreen
import com.example.ecommercefront_end.ui.admin.InsertProductScreen
import com.example.ecommercefront_end.ui.checkout.CheckoutAddressScreen
import com.example.ecommercefront_end.ui.checkout.CheckoutPaymentScreen
import com.example.ecommercefront_end.ui.checkout.CheckoutScreen
import com.example.ecommercefront_end.ui.admin.AdminOrdersScreen
import com.example.ecommercefront_end.ui.checkout.OrderConfirmationScreen
import com.example.ecommercefront_end.ui.user.ChangePasswordScreen
import com.example.ecommercefront_end.ui.user.GroupScreen
import com.example.ecommercefront_end.ui.user.Address.InsertAddressScreen
import com.example.ecommercefront_end.ui.user.Payments.InsertPaymentMethodScreen
import com.example.ecommercefront_end.ui.user.MyAccountScreen
import com.example.ecommercefront_end.ui.user.Payments.PaymentMethodsScreen
import com.example.ecommercefront_end.ui.user.SignInUpScreen
import com.example.ecommercefront_end.ui.user.Payments.TransactionsScreen
import com.example.ecommercefront_end.ui.user.UserOrdersScreen
import com.example.ecommercefront_end.ui.wishlist.WishlistsScreen
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import com.example.ecommercefront_end.viewmodels.AdminViewModel
import com.example.ecommercefront_end.viewmodels.BookViewModel
import com.example.ecommercefront_end.viewmodels.CartViewModel
import com.example.ecommercefront_end.viewmodels.GroupViewModel
import com.example.ecommercefront_end.viewmodels.LoginViewModel
import com.example.ecommercefront_end.viewmodels.RegistrationViewModel
import com.example.ecommercefront_end.viewmodels.TransactionViewModel
import com.example.ecommercefront_end.viewmodels.WishlistViewModel
import kotlinx.coroutines.async
import java.util.UUID


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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

    override fun onResume() {
        super.onResume()
        SessionManager.init(this)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavigationView(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val selectedIndex = remember { mutableIntStateOf(0) }

    // Usa remember per mantenere i ViewModel
    val cartRepository = CartRepository(RetrofitClient.cartApiService)
    val cartViewModel = remember { CartViewModel(repository = cartRepository) }
    val accountViewModel = remember { AccountViewModel(repository = AccountRepository(RetrofitClient.userApiService)) }
    val addressViewModel = remember { AddressViewModel(repository = AddressRepository(RetrofitClient.addressApiService)) }
    val bookViewModel = remember { BookViewModel(repository = BookRepository(RetrofitClient.booksApiService)) }

    val adminViewModel = remember { AdminViewModel(repository = AdminRepository(RetrofitClient.adminApiService)) }
    val checkoutViewModel = remember { CheckoutViewModel(checkoutRepository = CheckoutRepository(RetrofitClient.checkoutApiService), cartViewModel = cartViewModel, navController = navController) }
    val transactionViewModel = remember { TransactionViewModel(transactionRepository = TransactionRepository(RetrofitClient.transactionApiService)) }

    val wRepository = WishlistRepository( RetrofitClient.wishlistApiService, RetrofitClient.wishlistItemApiService)
    val groupRepository = GroupRepository(RetrofitClient.groupApiService)
    val groupViewModel = GroupViewModel(groupRepository)

    val wishlistViewModel = remember { WishlistViewModel(wRepository, groupRepository) }

    val startDestination = if (SessionManager.user?.role == "ROLE_ADMIN") {
        "admin-home"
    } else {
        "home"
    }

    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(selectedIndex, navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                selectedIndex.value = 0
                HomeScreen(bookViewModel = bookViewModel,navController)
            }

            composable("admin-home") {
                selectedIndex.value = 0
                AdminHomeScreen(navHostController = navController)
            }

            composable("admin-catalogue") {
                AdminCatalogueScreen(bookViewModel = bookViewModel, navController = navController)
            }

            composable("admin-users-list") {
                AdminUsersListScreen(viewModel = adminViewModel, navHostController = navController)
            }

            composable("/books_details/{idBook}", arguments = listOf(navArgument("idBook") { type = NavType.LongType })) { backStackEntry ->
                val idBook = backStackEntry.arguments?.getLong("idBook") ?: 0L

                // Carica il libro corrispondente all'id
                LaunchedEffect(idBook) {
                    bookViewModel.loadBook(idBook)
                    wishlistViewModel.fetchWishlists(null)
                }

                // Osserva i cambiamenti del libro
                val book by bookViewModel.bookFlow.collectAsState()

                book?.let {
                    BookDetailsScreen(book = it, bookViewModel = bookViewModel, cartViewModel = cartViewModel, wishlistViewModel,navController)
                } ?: Text("Book not found")
            }

            composable("/admin/book_details/{idBook}", arguments = listOf(navArgument("idBook") { type = NavType.LongType })) { backStackEntry ->
                val idBook = backStackEntry.arguments?.getLong("idBook") ?: 0L

                // Carica il libro corrispondente all'id
                LaunchedEffect(idBook) {
                    bookViewModel.loadBook(idBook)
                }

                val book by bookViewModel.bookFlow.collectAsState()

                book?.let {
                    AdminSingleBookScreen(bookViewModel = bookViewModel, navController = navController)
                } ?: Text("Book not found")
            }

            composable("/admin/user_details/{userId}", arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null  // oppure gestisci l'errore in un altro modo
                    }
                }

                LaunchedEffect(userId) {
                    if(userId != null)
                        adminViewModel.loadUser(userId) // carica
                }

                val user by adminViewModel.userFlow.collectAsState()

                user?.let {
                    AdminUserDetailsScreen(user = it, navController)
                } ?: Text("User not found")
            }

            composable("insert-product") {
                InsertProductScreen(viewModel = bookViewModel, navController)
            }

            composable("change-password") {
                ChangePasswordScreen(viewModel = accountViewModel, navController)
            }

            composable("insert-payment-method") {
                InsertPaymentMethodScreen(viewModel = checkoutViewModel, navController)
            }

            composable("cart") {
                selectedIndex.value = 2
                CartScreen(viewModel = cartViewModel, navController = navController, onCheckoutClick = {
                    println("isCheckoutEnabled: ${cartViewModel.isCheckoutEnabled.value}")

                    if (cartViewModel.isCheckoutEnabled.value) navController.navigate("checkout") })

            }
            composable("/admin/wishlist/{userId}", arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null  // oppure gestisci l'errore in un altro modo
                    }
                }
                LaunchedEffect(userId) {
                    if(userId != null)
                        wishlistViewModel.fetchWishlists(idUser = userId)
                        //wishlistViewModel.fetchWishlistItems()

                }
                //val user by adminViewModel.userFlow.collectAsState()


                WishlistsScreen(wishlistViewModel = wishlistViewModel, bookViewModel= bookViewModel, cartViewModel = cartViewModel, navController = navController)

            }
            composable("wishlist") {
                selectedIndex.value = 3
                LaunchedEffect(Unit) {
                    wishlistViewModel.fetchWishlists(null)
                }
                WishlistsScreen(wishlistViewModel = wishlistViewModel, bookViewModel= bookViewModel, cartViewModel = cartViewModel, navController = navController)
            }

            composable("userAuth") {
                selectedIndex.value = 1
                val _authApiService = RetrofitClient.authApiService
                val repository = AuthRepository(_authApiService)
                SignInUpScreen(loginViewModel = LoginViewModel(repository), registrationViewModel = RegistrationViewModel(repository), navController)
            }

            composable("account-manager") {
                selectedIndex.value = 1

                LaunchedEffect(Unit) {
                    accountViewModel.loadUserDetails(forceReload = true)
                }

                val _userApiService = RetrofitClient.userApiService
                val repository = AccountRepository(_userApiService)
                AccountManagerScreen(viewModel = accountViewModel, bookViewModel, navController)
            }
            composable("my-account") {
                LaunchedEffect(Unit) {
                    Log.d("MyAccountScreen", "SessionManager.user: ${SessionManager.user}")
                    val userDetailsJob = async {accountViewModel.loadUserDetails(forceReload = true)}
                    val defaultAddress = async {addressViewModel.fetchDefaultAddress()}

                    userDetailsJob.await()
                    defaultAddress.await()
                }
                MyAccountScreen(
                    accountViewModel = accountViewModel, addressViewModel = addressViewModel,
                    navHostController = navController)
            }
            composable("groups") {
                LaunchedEffect(Unit) {
                    val groupoJob = async {groupViewModel.fetchGroups()}
                    //groupViewModel.fetchGroupMembers()

                    groupoJob.await()
                }
                GroupScreen(groupViewModel, navController)
            }

            composable("addresses/{userId}", arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null  // oppure gestisci l'errore in un altro modo
                    }
                }

                LaunchedEffect(userId) {
                    addressViewModel.fetchUserAddresses(userId)
                }

                AddressesScreen(userId, viewModel = addressViewModel, navController)
            }

            composable("insert-address/{userId}", arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null  // oppure gestisci l'errore in un altro modo
                    }
                }

                InsertAddressScreen(viewModel = addressViewModel, navController, userId)
            }

            composable("edit-address/{userId}/{addressId}", arguments = listOf(navArgument("userId") { type = NavType.StringType },navArgument("addressId") { type = NavType.LongType })) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.let {
                    try {
                        UUID.fromString(it)
                    } catch (e: IllegalArgumentException) {
                        null  // oppure gestisci l'errore in un altro modo
                    }
                }
                val addressId = backStackEntry.arguments?.getLong("addressId")
                if (addressId != null) {
                    EditAddressScreen(viewModel = addressViewModel, navController = navController, userId, addressId)
                }
            }

            composable("checkout") {
                CheckoutScreen(
                    viewModel = checkoutViewModel,
                    navController = navController)
            }

            composable("checkout-addresses") {
              CheckoutAddressScreen(viewModel = checkoutViewModel, navController = navController)
            }

            composable("checkout-payment") {
                CheckoutPaymentScreen(viewModel = checkoutViewModel, navController = navController)
            }

            composable("order-confirmation"){
                OrderConfirmationScreen( navController = navController, checkoutViewModel = checkoutViewModel)
            }

            composable("admin-orders") {
                AdminOrdersScreen(viewModel = adminViewModel, onOrderClick = {
                })
            }

            composable("orders") {
                UserOrdersScreen(viewModel = accountViewModel, onOrderClick = {
                })
            }
            composable("payment-methods") {
                PaymentMethodsScreen(viewModel = checkoutViewModel, navController = navController)
            }

            composable("transactions"){
                TransactionsScreen( viewModel = transactionViewModel)
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
    val isSearchVisible = currentRoute == "home" || currentRoute == "filtered-books"
    var filterOptions by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Allinea il contenuto al centro
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center
                )
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
fun BottomBar(selectedIndex: MutableState<Int>, navHostController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex.value == 0,
            onClick = {
                selectedIndex.value = 0
                if(SessionManager.user == null || (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN")) {
                    navHostController.navigate("home") {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                else if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN"){
                    navHostController.navigate("admin-home") {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
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
                if(SessionManager.user == null)
                    navHostController.navigate("userAuth") {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                else
                    navHostController.navigate("account-manager") {
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
        if(SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN")
            NavigationBarItem(
                selected = selectedIndex.value == 2,
                onClick = {
                    selectedIndex.value = 2
                    navHostController.navigate("cart") {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                icon = {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = stringResource(R.string.cart)
                    )
                }
            )

        if(SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN")
            NavigationBarItem(
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
