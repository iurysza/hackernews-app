package com.iurysouza.hackernews.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

@ExperimentalAnimationApi
fun AnimatedContentScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination
    val targetNavGraph = target.destination
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
fun AnimatedContentScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination
    val targetNavGraph = target.destination
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}
