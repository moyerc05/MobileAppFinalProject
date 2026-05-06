@file:Suppress("ktlint:standard:no-wildcard-imports")

package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.moravian.csci395.flashfocus.data.AppDatabase
import org.jetbrains.compose.resources.stringResource
import studyblobs.composeapp.generated.resources.Res
import studyblobs.composeapp.generated.resources.app_name
import studyblobs.composeapp.generated.resources.back

@Suppress("ktlint:standard:function-naming", "ktlint:standard:kdoc")
/**
 * Root composable for the Flash Focus application.
 * - Sets up navigation
 * - Creates and provides the AppViewModel
 * - Displays the top app bar
 * - Routes between all screens
 * @param database The app's database used to create the DAO and ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    database: AppDatabase,
) {
    val navController = rememberNavController()

    val dao = database.getDao()
    val viewModel: AppViewModel = viewModel { AppViewModel(dao) }

    FlashFocusTheme {
        Scaffold(
            topBar = {
                val curBackStackEntry by navController.currentBackStackEntryAsState()
                val curDestination = curBackStackEntry?.destination

                val onWelcomeScreen =
                    curDestination?.hasRoute<WelcomeScreen>() == true

                TopAppBar(
                    title = { Text(stringResource(Res.string.app_name)) },
                    navigationIcon = {
                        if (!onWelcomeScreen) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Text(stringResource(Res.string.back))
                            }
                        }
                    },
                )
            },
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = WelcomeScreen,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable<WelcomeScreen> {
                    WelcomeScreen(
                        viewModel = viewModel,
                        onStart = { navController.navigate(TimerSetupScreen) },
                        onViewStats = { navController.navigate(StatsScreen) },
                        onViewCollection = { navController.navigate(CollectionScreen) },
                    )
                }

                composable<TimerSetupScreen> {
                    TimerSetupScreen(
                        viewModel = viewModel,
                        onStartTimer = {
                            navController.navigate(TimerScreen)
                        },
                    )
                }

                composable<TimerScreen> {
                    TimerScreen(
                        viewModel = viewModel,
                        onTimerFinished = {
                            navController.navigate(EndScreen)
                        },
                    )
                }

                composable<EndScreen> {
                    EndScreen(
                        viewModel = viewModel,
                        onDone = {
                            navController.popBackStack(WelcomeScreen, inclusive = false)
                        },
                    )
                }

                composable<StatsScreen> {
                    StatsScreen(
                        viewModel = viewModel,
                        onReset = {
                            viewModel.resetAllData()
                        },
                    )
                }

                composable<CollectionScreen> {
                    CollectionScreen(
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}
