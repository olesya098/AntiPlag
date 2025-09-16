package com.hfad.antiplag.presentation.homeScreen

import MessageBubble
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.model.PlagiatCheckState
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.bottonBar.BottomBar
import com.hfad.antiplag.presentation.components.message.MessageType
import com.hfad.antiplag.presentation.components.navigationDrawer.NavigationDrawer
import com.hfad.antiplag.viewModel.LoginSigninViewModel
import com.hfad.antiplag.viewModel.MessageViewModel
import com.hfad.antiplag.viewModel.PlagiarismCheckViewModel
import com.hfad.antiplag.viewModel.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    plagiarismCheckViewModel: PlagiarismCheckViewModel,
    viewModel: LoginSigninViewModel,
    themeViewModel: ThemeViewModel,
    messageViewModel: MessageViewModel
) {

    val email by viewModel.email.collectAsState()
    val state = plagiarismCheckViewModel.checkState.collectAsState()
    val messages by messageViewModel.messages.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current
    val isGoogleUser by viewModel.isGoogleUser.collectAsState()
    val lazyListState = rememberLazyListState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                if (isGranted) {
                    Log.d("Permissions", "Permission granted: ${it.key}")
                } else {
                    Toast.makeText(context, "Разрешения не предоставлены", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    // Инициализация сообщений при загрузке
    LaunchedEffect(Unit) {
        try {
            val userId = viewModel.getCurrentUserId()
            if (userId.isNotEmpty()) {
                messageViewModel.initMessages(userId)
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "Firestore initialization error: ${e.message}")
            // Продолжаем работу с локальными сообщениями
        }

        launcher.launch(
            arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )

        if (email.isEmpty()) {
            val currentEmail = viewModel.getCurrentUserEmail()
            if (currentEmail.isNotEmpty()) {
                viewModel.updateEmail(currentEmail)
            }
        }
    }

    // Сохранение состояний в базу данных
    LaunchedEffect(state.value) {
        try {
            val userId = viewModel.getCurrentUserId()
            if (userId.isNotEmpty()) {
                when (val currentState = state.value) {
                    PlagiatCheckState.SendText -> {
                        messageViewModel.addMessage(userId, "Текст отправлен", MessageType.SYSTEM)
                    }
                    is PlagiatCheckState.CheckingStatus -> {
                        messageViewModel.addMessage(userId, "Текст обрабатывается ${currentState.progress}", MessageType.CHECKING)
                    }
                    is PlagiatCheckState.Error -> {
                        messageViewModel.addMessage(userId, "Ошибка: ${currentState.message}", MessageType.ERROR)
                    }
                    is PlagiatCheckState.Success -> {
                        plagiarismCheckViewModel.updateReport(currentState.report)
                        messageViewModel.addMessage(userId, "Узнать результат", MessageType.SUCCESS)
                    }
                    is PlagiatCheckState.WaitingStatus -> {
                        messageViewModel.addMessage(userId, "...", MessageType.WAITING)
                    }
                    else -> {}
                }
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "Error saving message to Firestore: ${e.message}")
            // Fallback: сохраняем локально
            when (val currentState = state.value) {
                PlagiatCheckState.SendText -> {
                    messageViewModel.addLocalMessage("Текст отправлен", MessageType.SYSTEM)
                }
                is PlagiatCheckState.CheckingStatus -> {
                    messageViewModel.addLocalMessage("Текст обрабатывается ${currentState.progress}", MessageType.CHECKING)
                }
                is PlagiatCheckState.Error -> {
                    messageViewModel.addLocalMessage("Ошибка: ${currentState.message}", MessageType.ERROR)
                }
                is PlagiatCheckState.Success -> {
                    plagiarismCheckViewModel.updateReport(currentState.report)
                    messageViewModel.addLocalMessage("Узнать результат", MessageType.SUCCESS)
                }
                is PlagiatCheckState.WaitingStatus -> {
                    messageViewModel.addLocalMessage("...", MessageType.WAITING)
                }
                else -> {}
            }
        }
    }

    // Автопрокрутка к последнему сообщению
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    NavigationDrawer(
        drawerState = drawerState,
        onMenuClick = {
            scope.launch {
                if (drawerState.isClosed) drawerState.open()
                else drawerState.close()
            }
        },
        navController = navController,
        isDarkTheme = isDarkTheme,
        onThemeChange = {
            themeViewModel.toggleTheme()
        },
        onOut = {
            viewModel.signOut(context) {
                Toast.makeText(context, "Вы вышли из системы", Toast.LENGTH_SHORT).show()
            }
        },
        email = email,
        onDelete = {
            viewModel.signDelete(context) { success ->
                if (success) {
                    Toast.makeText(context, "Аккаунт удален", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ошибка при удалении аккаунта", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onNavigateToLogin = {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        },
        isGoogleUser = isGoogleUser,
    ) { innerPadding ->
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    androidx.compose.material3.TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.Text(
                                    "AntiPlag",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        },
                        navigationIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        scope.launch {
                                            if (drawerState.isClosed) drawerState.open()
                                            else drawerState.close()
                                        }
                                    }
                            )
                        },
                        actions = {
                            Image(
                                painter = painterResource(id = R.drawable.information),
                                contentDescription = "About",
                                modifier = Modifier
                                    .size(27.dp)
                                    .clickable {
                                        navController.navigate(Routes.ABOUT)
                                    }
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.scrim,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    BottomBar(
                        plagiarismCheckViewModel = plagiarismCheckViewModel,
                        messageViewModel = messageViewModel, // Передаем MessageViewModel
                        viewModel = viewModel // Передаем LoginSigninViewModel
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { scaffoldPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (messages.isEmpty() && state.value == PlagiatCheckState.Idle) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon),
                            contentDescription = null,
                            modifier = Modifier.size(86.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = lazyListState
                    ) {
                        items(messages) { message ->
                            MessageBubble(
                                message = message.text,
                                type = message.type, // Передаем тип сообщения
                                modifier = if (message.type == MessageType.SUCCESS) {
                                    Modifier.clickable {
                                        navController.navigate(Routes.RESULTS)
                                    }
                                } else {
                                    Modifier
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}