package com.hfad.antiplag.presentation.logIn

import android.R.attr.password
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.customButton.CustomButton
import com.hfad.antiplag.presentation.components.customTextField.CustomTextField
import com.hfad.antiplag.presentation.components.dialog.Dialog
import com.hfad.antiplag.ui.theme.blueLite
import com.hfad.antiplag.viewModel.LoginSigninViewModel

@Composable
fun LogInScreen(navController: NavController, viewModel: LoginSigninViewModel) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogMessage by viewModel.dialogMessage.collectAsState()


    if (showDialog) {
        Dialog(
            message = dialogMessage,
            onDismiss = {
                viewModel.dismissDialog()
                navController.navigate(Routes.HOME)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 32.dp, top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text(
                text = "Log In",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.padding(24.dp))
            Text(
                text = "Please log in to your account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 17.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                label = "Email",
                value = email,
                onvalChange = { viewModel.updateEmail(it) }
            )

            CustomTextField(
                label = "Password",
                value = password,
                onvalChange = { viewModel.updatePassword(it) }
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        CustomButton(
            title = "LogIn",
            onClick = {
                viewModel.logIn { success ->
                    if (success){
                        viewModel.showStatusDialog("Пользователь зашёл в систему")
                    }

                }

            },
            modifier = Modifier.padding()

        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = blueLite,
                thickness = 1.dp
            )
            Text(
                text = "Or",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = blueLite,
                thickness = 1.dp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "Sign in with Google",
            modifier = Modifier
                .size(48.dp)
                .clickable { /* Обработка входа через Google */ }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Is this your first time?",
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 32.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = " Signup",
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .clickable {
                        navController.navigate(Routes.SIGNUP)

                    },
                color = blueLite,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}