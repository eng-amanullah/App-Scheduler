package com.amanullah.appscheduler.base.navgraph

sealed class Screen(val route: String) {
    data object Apps : Screen(route = "apps")
    data object Scheduled : Screen(route = "scheduled")
    data object Completed : Screen(route = "completed")
}