package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.ui.home.testImgs
import com.example.ecommercefront_end.viewmodels.AdminViewModel
import com.example.ecommercefront_end.viewmodels.BookViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID


fun Modifier.userEntryModifier(navController: NavController, userId: UUID) = composed {
    this
        .fillMaxWidth()
        .height(200.dp)
        .clickable { navController.navigate("/admin/user_details/${userId}") }
}

@Composable
fun AdminUsersListScreen(viewModel: AdminViewModel, navHostController: NavHostController){

    val users by viewModel.filteredUsers.collectAsState()
    var searchValue by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    Column(modifier = Modifier.fillMaxSize()){

        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            TextField(
                value = searchValue,
                onValueChange = { searchValue = it;
                    viewModel.filterUser(searchValue)
                                },
                label = { Text("Search by Name, Surname, ID or Email") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.padding(2.dp))
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            if(users != null && users!!.isNotEmpty()) {
                for ((index,user) in users!!.withIndex())
                    item {
                        Row(){
                            Text(text = (index + 1).toString(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(4.dp))
                            Spacer(modifier = Modifier.padding(2.dp))
                            UserCard(user = user, navHostController)
                        }
                    }
            }
            else{
                item{
                    Text(text = "No users")
                }
            }
        }
    }
}

@Composable
fun UserCard(user: UserDetails, navHostController: NavHostController){
    
    Row(modifier = Modifier.userEntryModifier(navHostController,user.id)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)) {
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)){
                    Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon", modifier = Modifier.size(50.dp))

                    Spacer(modifier = Modifier.padding(4.dp))

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column {
                                Text("Name", fontWeight = FontWeight.Bold)
                                Text(
                                    text = user.firstName?:"Null",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Column {
                                Text("Surname", fontWeight = FontWeight.Bold)
                                Text(
                                    text = user.lastName?:"Null",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                    }
                }
                Row(modifier = Modifier.padding(8.dp)) {
                    Column {
                        Text("Email", fontWeight = FontWeight.Bold)
                        Text(text = user.email?:"Null",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    }
                }

                Row(modifier = Modifier.padding(8.dp)) {
                    Column {
                        Text("ID", fontWeight = FontWeight.Bold)
                        Text(
                            text = user.id.toString()?:"Null",
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}