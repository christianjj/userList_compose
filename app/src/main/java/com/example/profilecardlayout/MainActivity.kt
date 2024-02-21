package com.example.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.transform.CircleCropTransformation
import com.example.profilecardlayout.ui.theme.ProfileCardLayoutTheme
import com.google.accompanist.coil.rememberCoilPainter
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileCardLayoutTheme() {
                        UserApplication()
                    }

                }
            }
        }
    }
}

@Composable
fun UserApplication(userProfiles: List<UserProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list") {
        composable("users_list") {
            UserListScreen(userProfiles, navController)
        }
        composable("users_details/{userId}", arguments = listOf(navArgument("userId") {
            type = NavType.IntType
        })) { navBackStacEntry ->
            UserProfileDetailsScreen(navBackStacEntry.arguments!!.getInt("userId"), navController)
        }
    }
}


@Composable
fun UserListScreen(userProfiles: List<UserProfile>, navController: NavController?) {
    Scaffold(topBar = { AppBar("Users List", Icons.Default.Home) {} }) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {

            LazyColumn {
                items(userProfiles) { userProfile ->
                    ProfileCard(userProfile = userProfile) {
                        navController?.navigate("users_details/${userProfile.id}")
                    }
                }
            }
//            Column {
//                for (userProfile in userProfiles) {
//                    ProfileCard(userProfile)
//                }
//            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, icon: ImageVector, iconClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                icon,
                contentDescription = "dsad",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { iconClick.invoke() }
            )
        },
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Magenta)
    )


}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
    Column() {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .clickable { clickAction.invoke() },

            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = CutCornerShape(topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.purple_200)),
        )

        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ProfilePicture(userProfile.drawableId, userProfile.status, 72.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
            }
        }


    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment

    ) {
        Text(text = userName, style = MaterialTheme.typography.headlineLarge)
        Text(
            text = if (onlineStatus) {
                "Active Now"
            } else {
                "Offline"
            }, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProfilePicture(drawableId: String, onlineStatus: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        modifier = Modifier.padding(10.dp),
        border = BorderStroke(
            width = 2.dp, color = if (onlineStatus) {
                Color.Green
            } else {
                Color.LightGray
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )

    ) {
//        Image(
//            painter = painterResource(id = R.drawable.maki),
//            modifier = Modifier.size(72.dp),
//            contentDescription = "content description",
//            contentScale = ContentScale.Crop
//
//        )

        Image(
            painter = rememberCoilPainter(request = drawableId, requestBuilder = {
                transformations(
                    CircleCropTransformation()
                )
            }),
            modifier = Modifier.size(imageSize),
            contentDescription = "content description",
        )

    }

}

@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavHostController?) {
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }
    Scaffold(topBar = {
        AppBar("Users Profile Details", Icons.Default.ArrowBack)
        {
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
//            ProfileCard(userProfile = userProfiles)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                ProfilePicture(userProfile.drawableId, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
//            Column {
//                for (userProfile in userProfiles) {
//                    ProfileCard(userProfile)
//                }
//            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserProfileDetailsPreview() {
    ProfileCardLayoutTheme {
        UserProfileDetailsScreen(0, null)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileCardLayoutTheme {
        UserListScreen(userProfiles = userProfileList, null)
    }
}