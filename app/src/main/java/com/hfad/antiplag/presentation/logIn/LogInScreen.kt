package com.hfad.antiplag.presentation.logIn

import android.R.attr.visible
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.customButton.CustomButton
import com.hfad.antiplag.presentation.components.customTextField.CustomTextField
import com.hfad.antiplag.presentation.components.customTextFieldPassword.CustomTextFieldPassword
import com.hfad.antiplag.presentation.components.dialog.Dialog
import com.hfad.antiplag.ui.theme.blueLite
import com.hfad.antiplag.ui.theme.red
import com.hfad.antiplag.viewModel.LoginSigninViewModel

@Composable
fun LogInScreen(navController: NavController, viewModel: LoginSigninViewModel) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogMessage by viewModel.dialogMessage.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    val errorMessage by viewModel.errorMessage.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.clearFields()
    }

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
            .padding(start = 26.dp, end = 26.dp, top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 15.dp)
        ) {
            Text(
                text = stringResource(R.string.log_in),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                text = stringResource(R.string.please_log_in_to_your_account),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.padding(7.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = red,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
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


            CustomTextFieldPassword(
                label = stringResource(R.string.password),
                value = password,
                onvalChange = { viewModel.updatePassword(it) },
                visible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = it }

            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        CustomButton(
            title = stringResource(R.string.log_in),
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
                text = stringResource(R.string.is_this_your_first_time),
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 32.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.padding(1.dp))

            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 15.sp,
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