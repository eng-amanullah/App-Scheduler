package com.amanullah.appscheduler.base.navgraph

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amanullah.appscheduler.R
import com.amanullah.appscheduler.presentation.apps.AppsScreen
import com.amanullah.appscheduler.presentation.completed.CompletedScreen
import com.amanullah.appscheduler.presentation.scheduled.ScheduledScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var backPressedOnce by remember { mutableStateOf(value = false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(weight = 1F)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Apps.route
            ) {
                composable(route = Screen.Apps.route) {
                    AppsScreen()
                    // Handle back press logic for Apps screen
                    HandleBackPress(
                        navController = navController,
                        context = context,
                        backPressedOnce = backPressedOnce
                    ) {
                        backPressedOnce = true
                        coroutineScope.launch {
                            delay(timeMillis = 2000) // Reset the flag after 2 seconds
                            backPressedOnce = false
                        }
                    }
                }
                composable(route = Screen.Scheduled.route) {
                    ScheduledScreen()
                    HandleBackPress(
                        navController = navController,
                        context = context,
                        goToApps = true
                    )
                }
                composable(route = Screen.Completed.route) {
                    CompletedScreen()
                    HandleBackPress(
                        navController = navController,
                        context = context,
                        goToApps = true
                    )
                }
            }
        }

        BottomNavBar(navController = navController)
    }
}

@Composable
fun HandleBackPress(
    navController: NavHostController,
    context: android.content.Context,
    goToApps: Boolean = false,
    backPressedOnce: Boolean = false,
    onBackPressedOnce: (() -> Unit)? = null
) {
    BackHandler {
        val currentRoute = navController.currentDestination?.route
        when {
            // Navigate to Apps screen if coming from Scheduled or Completed
            goToApps && currentRoute != Screen.Apps.route -> {
                navController.navigate(route = Screen.Apps.route) {
                    popUpTo(Screen.Apps.route) { inclusive = false }
                }
            }

            // Show confirmation on back press in Apps screen
            currentRoute == Screen.Apps.route -> {
                if (backPressedOnce) {
                    (context as Activity).finish()
                } else {
                    onBackPressedOnce?.invoke()
                    Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val currentRoute = navBackStackEntry.value?.destination?.route

        // Contacts Tab
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.apps),
                    contentDescription = null,
                    tint = if (currentRoute == Screen.Apps.route) {
                        MaterialTheme.colorScheme.primary // Selected icon color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default icon color
                    }
                )
            },
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Apps",
                    color = if (currentRoute == Screen.Apps.route) {
                        MaterialTheme.colorScheme.primary // Selected label color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default label color
                    }
                )
            },
            selected = currentRoute == Screen.Apps.route,
            onClick = {
                if (currentRoute != Screen.Apps.route) {
                    navController.navigate(route = Screen.Apps.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(route = Screen.Apps.route) { inclusive = false }
                    }
                }
            },
            modifier = Modifier
                .background(
                    color = if (currentRoute == Screen.Apps.route) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) // Background when selected
                    } else {
                        Color.Transparent
                    }
                )
                .scale(
                    if (currentRoute == Screen.Apps.route) 1.1f else 1f // Slight scale when selected
                ),
            alwaysShowLabel = true
        )

        // Scheduled Tab
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.scheduled),
                    contentDescription = null,
                    tint = if (currentRoute == Screen.Scheduled.route) {
                        MaterialTheme.colorScheme.primary // Selected icon color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default icon color
                    }
                )
            },
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Scheduled",
                    color = if (currentRoute == Screen.Scheduled.route) {
                        MaterialTheme.colorScheme.primary // Selected label color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default label color
                    }
                )
            },
            selected = currentRoute == Screen.Scheduled.route,
            onClick = {
                if (currentRoute != Screen.Scheduled.route) {
                    navController.navigate(route = Screen.Scheduled.route) {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(route = Screen.Scheduled.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .background(
                    color = if (currentRoute == Screen.Scheduled.route) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) // Background when selected
                    } else {
                        Color.Transparent
                    }
                )
                .scale(
                    if (currentRoute == Screen.Scheduled.route) 1.1f else 1f // Slight scale when selected
                ),
            alwaysShowLabel = true
        )

        // Completed Tab
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.completed),
                    contentDescription = null,
                    tint = if (currentRoute == Screen.Completed.route) {
                        MaterialTheme.colorScheme.primary // Selected icon color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default icon color
                    }
                )
            },
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Completed",
                    color = if (currentRoute == Screen.Completed.route) {
                        MaterialTheme.colorScheme.primary // Selected label color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default label color
                    }
                )
            },
            selected = currentRoute == Screen.Completed.route,
            onClick = {
                if (currentRoute != Screen.Completed.route) {
                    navController.navigate(route = Screen.Completed.route) {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(route = Screen.Completed.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .background(
                    color = if (currentRoute == Screen.Completed.route) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) // Background when selected
                    } else {
                        Color.Transparent
                    }
                )
                .scale(
                    if (currentRoute == Screen.Completed.route) 1.1f else 1f // Slight scale when selected
                ),
            alwaysShowLabel = true
        )
    }
}