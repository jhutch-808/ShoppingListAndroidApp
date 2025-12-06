package hu.bme.ait.wanderer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.ait.wanderer.ui.navigation.AddRestarauntScreenRoute
import hu.bme.ait.wanderer.ui.navigation.ListPlacesScreenRoute
import hu.bme.ait.wanderer.ui.navigation.MainScreenRoute
import hu.bme.ait.wanderer.ui.navigation.WelcomeScreenRoute
import hu.bme.ait.wanderer.ui.screens.mainScreen.MainScreen
import hu.bme.ait.wanderer.ui.screens.welcomescreens.WelcomeScreen
import hu.bme.ait.wanderer.ui.theme.WandererTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WandererTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NavGraph(modifier: Modifier = Modifier) {

    val backStack= rememberNavBackStack(WelcomeScreenRoute)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider  = entryProvider {
            entry<WelcomeScreenRoute> {
                WelcomeScreen(
                    onContinueClicked = {
                        backStack.add(MainScreenRoute)
                    }
                )
            }
            entry<MainScreenRoute> {
                MainScreen(
                    onAddRestarauntClicked = {
                        backStack.add(AddRestarauntScreenRoute)
                    },
                    onListRestarauntsClicked = {
                        backStack.add(ListPlacesScreenRoute)
                    }
                )
            }

        }
    )
}
