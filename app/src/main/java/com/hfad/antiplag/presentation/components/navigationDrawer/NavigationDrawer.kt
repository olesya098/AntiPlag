package com.hfad.antiplag.presentation.components.navigationDrawer

import android.util.Log.i
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.hfad.antiplag.R
import com.hfad.antiplag.model.DrawerItem
import com.hfad.antiplag.ui.theme.blueLite
import kotlinx.coroutines.launch
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    val items = listOf(
        DrawerItem(icon = R.drawable.add, label = stringResource(R.string.new_chat)),
        DrawerItem(icon = R.drawable.schedule, label = stringResource(R.string.history)),
        DrawerItem(icon = R.drawable.error, label = "About"),
        DrawerItem(icon = R.drawable.login, label = stringResource(R.string.login))
    )

    var selectedItem by remember { mutableStateOf(items[0]) }
    val drawerWidth = remember { 0.9f }

    ModalNavigationDrawer(
        drawerState = drawerState,
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
                                scope.launch { drawerState.close() }
                                selectedItem = item
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
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = "example@gmail.com",
                        fontSize = 17.sp,
                        color = blueLite,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
        content = {
            content(PaddingValues())
        }
    )
}