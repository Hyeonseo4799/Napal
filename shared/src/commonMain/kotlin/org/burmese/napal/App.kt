package org.burmese.napal

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.burmese.napal.domain.Card
import org.burmese.napal.navigation.Create
import org.burmese.napal.navigation.Home
import org.burmese.napal.navigation.Result
import org.burmese.napal.screen.CreateScreen
import org.burmese.napal.screen.HomeScreen
import org.burmese.napal.screen.ResultScreen
import org.burmese.napal.screen.SplashScreen

@Composable
@Preview
fun App() {
    var showSplash by remember { mutableStateOf(true) }

    var card: Card by remember { mutableStateOf(Card()) }

    val navController = rememberNavController()

    Scaffold { innerPadding ->
        if (showSplash) {
            SplashScreen(onFinished = { showSplash = false })
        } else {
            NavHost(
                navController = navController,
                startDestination = Home,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                composable<Home> {
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        goToCreate = { navController.navigate(Create) },
                        goToGallery = { }
                    )
                }
                composable<Create> {
                    CreateScreen(
                        modifier = Modifier,
                        onBack = { navController.popBackStack() },
                        card = card,
                        onUpdateCard = { card = it },
                        goToResult = { navController.navigate(Result) }
                    )
                }
                composable<Result> {
                    LaunchedEffect(card.byteArray) {
                        if (card.byteArray == null) {
                            navController.navigate(Home) {
                                popUpTo(Home) { inclusive = true }
                            }
                        }
                    }
                    ResultScreen(
                        modifier = Modifier.padding(innerPadding),
                        card = card,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

}