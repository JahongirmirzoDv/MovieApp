package com.example.movieapp.ui.view.bottombar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.movieapp.ui.view.model.bottombar.BottomBarItem
import com.google.accompanist.insets.ui.BottomNavigation

@Composable
fun BottomBar(
    list: List<BottomBarItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: (BottomBarItem) -> Unit,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier.clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
        backgroundColor = Color.White,
        elevation = 5.dp,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        list.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onClick(it)
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray,
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = it.icon, contentDescription = it.name)
                        if (selected) {
                            Text(
                                text = it.name

                            )
                        }
                    }
                }
            )
        }
    }
}