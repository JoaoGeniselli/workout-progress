package com.dosei.fit.workout.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dosei.fit.workout.AppRoutes
import com.dosei.fit.workout.list.ExercisesScreen

@Composable
fun AppNavigation(controller: NavHostController) {
    NavHost(
        startDestination = AppRoutes.exercises,
        navController = controller
    ) {
        composable(route = AppRoutes.exercises) {
            ExercisesScreen(controller = controller)
        }
    }
}