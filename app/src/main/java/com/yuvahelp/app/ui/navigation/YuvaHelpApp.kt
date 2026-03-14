package com.yuvahelp.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuvahelp.app.ui.screens.AboutScreen
import com.yuvahelp.app.ui.screens.ArticleScreen
import com.yuvahelp.app.ui.screens.CategoriesScreen
import com.yuvahelp.app.ui.screens.HomeScreen
import com.yuvahelp.app.ui.screens.LatestUpdatesScreen
import com.yuvahelp.app.ui.screens.MainViewModel
import com.yuvahelp.app.ui.screens.SearchScreen

private data class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun YuvaHelpApp() {
    val navController = rememberNavController()
    val vm: MainViewModel = viewModel()
    val backStack by navController.currentBackStackEntryAsState()
    var searchQuery by remember { mutableStateOf("") }

    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("categories", "Categories", Icons.Default.List),
        BottomNavItem("latest", "Latest", Icons.Default.Update),
        BottomNavItem("search", "Search", Icons.Default.Search),
        BottomNavItem("about", "About", Icons.Default.Info)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = backStack?.destination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = "home", modifier = androidx.compose.ui.Modifier.padding(padding)) {
            composable("home") {
                HomeScreen(vm.allPosts.value) { id ->
                    vm.selectPost(id)
                    navController.navigate("article")
                }
            }
            composable("categories") {
                CategoriesScreen(vm.allPosts.value) { id ->
                    vm.selectPost(id)
                    navController.navigate("article")
                }
            }
            composable("latest") {
                LatestUpdatesScreen(vm.allPosts.value) { id ->
                    vm.selectPost(id)
                    navController.navigate("article")
                }
            }
            composable("search") {
                SearchScreen(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        vm.setSearchQuery(it)
                    },
                    results = vm.searchResults.value,
                    onPostClick = { id ->
                        vm.selectPost(id)
                        navController.navigate("article")
                    }
                )
            }
            composable("about") { AboutScreen() }
            composable("article") { ArticleScreen(vm.selectedPost.value) }
        }
    }
}
