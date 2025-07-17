package com.hfad.antiplag.presentation.components.navigationDrawer

import android.R.attr.onClick
import android.util.Log.i
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.model.DrawerItem
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.dayNightAnimation.DayNight
import com.hfad.antiplag.ui.theme.blueLite
import kotlinx.coroutines.launch
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var isDarkTheme by remember { mutableStateOf(false) }
//    MaterialTheme(
//        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
//    ) {
        val scope = rememberCoroutineScope()


    val items = listOf(
        DrawerItem(
            icon = R.drawable.add,
            label = stringResource(R.string.new_chat),
            route = Routes.HOME
        ),
        DrawerItem(
            icon = R.drawable.schedule,
            label = stringResource(R.string.history),
            route = Routes.HISTORY
        ),
        DrawerItem(
            icon = R.drawable.error,
            label = "About",
            route = Routes.ABOUT
        ),
        DrawerItem(
            icon = R.drawable.login,
            label = stringResource(R.string.login),
            route = Routes.LOGIN // Изменил с HOME на LOGIN
        )
    )

    var selectedItem by remember { mutableStateOf(items[0]) }
    val drawerWidth = remember { 0.9f }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxWidth(drawerWidth)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "AntiPlag",
                            fontSize = 32.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    items.forEach { item ->
                        val isSelected = item == selectedItem
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .height(47.dp)
                                .border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = blueLite
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.outline else Color.Transparent,
                                    shape = MaterialTheme.shapes.medium
                                )
                        ) {
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        item.label,
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        selectedItem = item
                                        // Добавляем навигацию здесь
                                        navController.navigate(item.route) {
                                            // Очищаем back stack до стартового экрана
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            // Предотвращаем множественные копии экрана
                                            launchSingleTop = true
                                            // Восстанавливаем состояние
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = item.label,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = Color.Transparent,
                                    unselectedContainerColor = Color.Transparent
                                )
                            )
                        }
                    }


                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 20.dp),
                        color = blueLite,
                        thickness = 1.dp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.your_email),
                            fontSize = 17.sp,
                            modifier = Modifier.padding(start = 10.dp),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "example@gmail.com",
                            fontSize = 17.sp,
                            modifier = Modifier.padding(start = 10.dp),

                            color = blueLite,
                            style = MaterialTheme.typography.bodySmall,
                        )

                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(15.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        DayNight(
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f),
                            isDay = !isDarkTheme, // Параметр, запускающий анимацию
                            onClick = {
                                isDarkTheme = !isDarkTheme // Переключение состояния при клике
                            }
                        )
                    }
                }
            },
            content = {
                content(PaddingValues())
            }
        )
    //}
}