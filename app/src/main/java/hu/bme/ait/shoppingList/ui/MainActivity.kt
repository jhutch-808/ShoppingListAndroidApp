package hu.bme.ait.shoppingList.ui

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
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.ait.shoppingList.ui.navigation.ShoppingItemsScreenRoute
import hu.bme.ait.shoppingList.ui.navigation.WelcomeScreenRoute
import hu.bme.ait.shoppingList.ui.screens.ShoppingItemScreen
import hu.bme.ait.shoppingList.ui.screens.WelcomeScreen
import hu.bme.ait.shoppingList.ui.theme.ShoppingItemAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingItemAppTheme {
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
    val backStack = rememberNavBackStack(WelcomeScreenRoute)

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
                        backStack.add(ShoppingItemsScreenRoute)
                    }
                )
            }
            entry<ShoppingItemsScreenRoute> {
                ShoppingItemScreen()
            }

        }
    )
}
