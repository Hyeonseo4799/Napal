package org.burmese.pomi

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
import org.burmese.pomi.domain.Card
import org.burmese.pomi.navigation.Create
import org.burmese.pomi.navigation.Home
import org.burmese.pomi.navigation.Result
import org.burmese.pomi.screen.CreateScreen
import org.burmese.pomi.screen.HomeScreen
import org.burmese.pomi.screen.ResultScreen
import org.burmese.pomi.screen.SplashScreen

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