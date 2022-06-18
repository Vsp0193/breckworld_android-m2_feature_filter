package com.breckworld.extensions

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

fun View.navigateTo(id: Int, bundle: Bundle? = null) = Navigation.findNavController(this).navigate(id, bundle)
fun View.navigateBack() = Navigation.findNavController(this).popBackStack()
fun View.navigateUp() = Navigation.findNavController(this).navigateUp()

fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}

fun androidx.fragment.app.Fragment.navigateTo(direction: Int, extras: FragmentNavigator.Extras) {
    findNavController().navigate(direction, null, null, extras)
}

fun NavController.navigateTo(
    direction: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = null,
    extras: FragmentNavigator.Extras? = null
) {
    navigate(direction, bundle, navOptions, extras)
}

/**
 * This need to disable navigate to fragment when selected menu item was clicked again.
 */
fun BottomNavigationView.setupWithNavController(navController: NavController) {
    this.setOnNavigationItemSelectedListener { item ->
        if (item.isChecked.not()) {
            NavigationUI.onNavDestinationSelected(item, navController)
        }
        item.isChecked.not()
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        val menu = this.menu
        menu.forEach {
            if (matchDestination(destination, it.itemId)) {
                it.isChecked = true
            }
        }
    }
}

fun Fragment.findNavController(@IdRes id: Int) = this.activity?.let { it.findNavController(id) }

fun matchDestination(destination: NavDestination, @IdRes destId: Int): Boolean {
    var currentDestination: NavDestination? = destination
    while (currentDestination?.id != destId && currentDestination?.parent != null) {
        currentDestination = currentDestination.parent
    }
    return currentDestination?.id == destId
}