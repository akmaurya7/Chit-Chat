package com.example.chitchat.View.Screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.chitchat.Model.EmailAuthState
import com.example.chitchat.View.Navigation.Routes
import com.example.chitchat.View.ViewModel.EmailAuthViewModel
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun HomeScreen(
    navController: NavController,
    emailAuthViewModel: EmailAuthViewModel,
    db:FirebaseFirestore
) {
    val emailAuthState by emailAuthViewModel.emailAuthState.observeAsState()
    val userDetail by emailAuthViewModel.emailUserState.collectAsState()
    //val currentUserDetails = emailAuthViewModel.getCurrentUser(db)


    // Get image from the database and parse it into the uri
    var userImageUri by remember { mutableStateOf<Uri?>(null) }
    userImageUri = Uri.parse(userDetail.imageUri)

    LaunchedEffect(emailAuthState) {
        when(emailAuthState){
            is EmailAuthState.Unauthenticated -> {navController.navigate(Routes.EmailSignInScreen)}
            else -> Unit
        }
    }
    Scaffold(
        topBar = { AppTopBar(userImageUri = userImageUri) },
        bottomBar = { AppBottomBar() },
        floatingActionButton = {

            FloatingActionButton(
                onClick = { /* TODO: Handle click */ }
                ,containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "New Chat")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                if (userImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userImageUri),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Default User Icon",
                        modifier = Modifier.size(120.dp),
                        tint = Color.Gray
                    )
                }
                Button(onClick = { emailAuthViewModel.signOut() }) {
                    Text(text = "Sign Out")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(userImageUri:Uri?) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        title = {
            Text(
                text = "Chit Chat",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 8.dp))
                },
        actions = {
            IconButton(onClick = { /* TODO: Handle account icon click */ }) {

                //Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
                if (userImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userImageUri),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Default User Icon",
                        modifier = Modifier.size(120.dp),
                        tint = Color.Gray
                    )
                }
            }
            IconButton(onClick = { /* TODO: Handle 3-dot menu click */ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More Options")
            }
        },
        scrollBehavior = scrollBehavior,
        )
}

@Composable
fun AppBottomBar() {
    var selectedItem by remember { mutableStateOf("Chats") }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Chat, contentDescription = "Chats") },
            label = { Text(text = "Chats") },
            selected = selectedItem == "Chats",
            onClick = { selectedItem = "Chats"},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Group, contentDescription = "Groups") },
            label = { Text(text = "Groups") },
            selected = selectedItem == "Groups",
            onClick = { selectedItem = "Groups" },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Call, contentDescription = "Call") },
            label = { Text(text = "Calls", fontWeight = FontWeight.Bold) },
            selected = selectedItem == "Calls",
            onClick = { selectedItem = "Calls" },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
    }
}
