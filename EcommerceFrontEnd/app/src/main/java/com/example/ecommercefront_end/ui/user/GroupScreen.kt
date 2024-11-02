package com.example.ecommercefront_end.ui.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.Group
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.viewmodels.GroupViewModel

@Composable
fun GroupScreen(groupViewModel: GroupViewModel, navController: NavController) {
    val groups by groupViewModel.groups.collectAsState()
    val groupMembers by groupViewModel.groupMembers.collectAsState()
    val isGroupLoading by groupViewModel.isLoading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbar by groupViewModel.showSnackbar.collectAsState()
    val snackbarMessage by groupViewModel.snackbarMessage.collectAsState()

    val selectedGroup = remember(groups) {
        mutableStateOf<Group?>(null)
    }

    LaunchedEffect(groups) {
        if (groups.isNotEmpty() && selectedGroup.value == null) {
            selectedGroup.value = groups.first()
            groupViewModel.loadGroupMembers(selectedGroup.value!!.id) // Carica i membri del gruppo selezionato.?id) // Carica i membri del gruppo selezionato
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        paddingValues ->

        if (isGroupLoading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Ricarica gli elementi della wishlist selezionata

            if (groups.isEmpty()){
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Crea una lista desiideri per visionare i gruppi associati",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
                {
                    item {
                        GroupList(
                            groups = groups,
                            viewModel = groupViewModel,
                            onGroupSelected = { group ->
                                selectedGroup.value = group
                                groupViewModel.loadGroupMembers(group.id)
                            }
                        )
                    }

                    // Mostra i dettagli solo se c'è una wishlist selezionata e ha degli elementi
                    selectedGroup.value?.let { group ->
                        item {
                            GroupDetails(
                                group = group,
                                groupMembers = groupMembers,
                                groupViewModel = groupViewModel,
                                navController = navController
                            )
                        }
                    }

                }
            }
        }
        LaunchedEffect(showSnackbar) {
            if (showSnackbar) {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    duration = SnackbarDuration.Short
                )
                groupViewModel.setShowSnackbar(false) // Resetta lo stato della Snackbar
            }
        }

    }
}

@Composable
fun GroupDetails(
    group: Group,
    groupMembers: List<User>,
    groupViewModel: GroupViewModel,
    navController: NavController,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = group.groupName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (groupMembers.isEmpty()) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ancora nessun membro nel gruppo \n " +
                            "Condividi la tua lista desideri con i tuoi amici",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            Text(
                text = "Membri del gruppo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) {
                items(groupMembers) { member ->
                    GroupMemberCard(
                        member = member,
                        onExpelMember = { groupViewModel.unshareWishlist(group.id) }, // Funzione per espellere il membro
                        groupViewModel = groupViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}


@Composable
fun GroupMemberCard(
    member: User,
    onExpelMember: () -> Unit,
    groupViewModel: GroupViewModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(130.dp) // Aumentato per migliorare la visualizzazione
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "${member.firstName} ${member.lastName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Mostra dettagli aggiuntivi se disponibili
            Text(
                text = ("Phonenumber " + member.phoneNumber)
                    ?: "Numero di telefono non disponibile",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onExpelMember,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Espelli", color = Color.White)
            }
        }
    }
}


@Composable
fun GroupList(groups : List<Group>, viewModel: GroupViewModel, onGroupSelected: (Group) -> Unit) {
    var showGroupMain by remember { mutableStateOf(false) }
    val isAdmin = user?.role == "ROLE_ADMIN"
    //val idUserSelectedByAdmin by viewModel.userSelectedByAdmin.collectAsState()


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "I tuoi gruppi delle liste desideri",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(
            items = groups,
            key = { item -> item.id.toString() ?: "" } // Chiave a livello di items

        ) { group ->
            GroupsThumbnail(
                group = group,
                onClick = { onGroupSelected(group) }
            )
        }
    }
}


@Composable
fun GroupsThumbnail(group : Group, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(70.dp) // Imposta l'altezza fissa per tutte le card
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ) // Colore di riempimento condizionale
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = group.groupName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                //.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Aggiungi eventuali altri dettagli della wishlist qui
        }
    }


}