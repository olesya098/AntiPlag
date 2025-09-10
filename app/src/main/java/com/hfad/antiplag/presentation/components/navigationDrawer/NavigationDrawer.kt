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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.model.DrawerItem
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.dayNightAnimation.DayNight
import com.hfad.antiplag.ui.theme.blueLite
import com.hfad.antiplag.ui.theme.coolBlack
import com.hfad.antiplag.ui.theme.darkGray
import com.hfad.antiplag.ui.theme.grayDevider
import com.hfad.antiplag.ui.theme.grayDeviderLite
import com.hfad.antiplag.ui.theme.liteGray
import com.hfad.antiplag.ui.theme.white
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HeaderFooter.fontSize

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onOut: () -> Unit,
    onDelete: () -> Unit,
    email: String,
    onNavigateToLogin: () -> Unit,
    isGoogleUser: Boolean,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scope = rememberCoroutineScope()

    // Цвета берутся из текущей темы
    val drawerBackgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val dividerColor = if (isDarkTheme) grayDevider else grayDeviderLite

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
            label = stringResource(R.string.about),
            route = Routes.ABOUT
        ),
        DrawerItem(
            icon = R.drawable.login,
            label = stringResource(R.string.log_out),
            route = Routes.LOGOUT
        )
    ) + if (!isGoogleUser) listOf(
        DrawerItem(
            icon = R.drawable.rubish,
            label = stringResource(R.string.delete_an_account),
            route = Routes.DELETE
        )
    ) else emptyList()

    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(drawerBackgroundColor)
            ) {
                // Заголовок
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "AntiPlag",
                        fontSize = 32.sp,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                // Элементы меню
                items.forEach { item ->
                    val isSelected = item == selectedItem
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .height(50.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = dividerColor
                                ),
                                shape = MaterialTheme.shapes.medium
                            )
                            .background(
                                color = if (isSelected) blueLite.copy(alpha = 0.2f)
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    item.label,
                                    fontSize = 17.sp,
                                    color = textColor,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                scope.launch {
                                    drawerState.close()

                                    // Для обычных экранов обновляем selectedItem
                                    if (item.route != Routes.LOGOUT && item.route != Routes.DELETE) {
                                        selectedItem = item
                                    }

                                    when (item.route) {
                                        Routes.LOGOUT -> {
                                            onOut()
                                            onNavigateToLogin() // Только здесь вызываем навигацию
                                        }
                                        Routes.DELETE -> {
                                            onDelete()
                                            onNavigateToLogin() // Только здесь вызываем навигацию
                                        }
                                        else -> {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.label,
                                    tint = textColor,
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

                // Разделитель
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    color = dividerColor,
                    thickness = 1.dp
                )

                // Блок с email
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.your_email),
                        fontSize = 17.sp,
                        modifier = Modifier.padding(start = 10.dp),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = email,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 4.dp),
                        color = blueLite,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Переключатель темы
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    DayNight(
                        modifier = Modifier
                            .width(40.dp)
                            .aspectRatio(1f),
                        isDay = !isDarkTheme,
                        onClick = {
                            onThemeChange()
                        }
                    )
                }
            }
        },
        content = {
            content(PaddingValues())
        }
    )
}