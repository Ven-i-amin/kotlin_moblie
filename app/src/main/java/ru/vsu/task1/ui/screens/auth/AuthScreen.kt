@file:OptIn(ExperimentalAnimationApi::class)

package ru.vsu.task1.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.navigation.AppBarViewModel
import ru.vsu.task1.ui.theme.AppTypography

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel(),
    appBarViewModel: AppBarViewModel = koinInject()
) {
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        appBarViewModel.setTopBar {  }
        appBarViewModel.hideBottomBar()
    }

    val authToken by viewModel.authToken.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val route by appBarViewModel.pressedButton.collectAsState()


    var isLogin by remember { mutableStateOf(true) }

    // Состояния для полей
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }
    var signupConfirmPassword by remember { mutableStateOf("") }
    var signupName by remember {mutableStateOf("")}


    LaunchedEffect(authToken) {
        if (authToken.isNotBlank()) {
            navController.navigate(route) {
                popUpTo("auth") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    if (isLoading) {
        return Loading()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = if (isLogin) "Welcome back" else "Create account",
                    style = AppTypography.displaySmall,
                    color = colors.onBackground
                )

                Row {
                    Text(
                        text = if (isLogin) "Don't have an account? " else "Already have an account? ",
                        style = AppTypography.bodySmall,
                        color = colors.onBackground.copy(alpha = 0.7f),
                    )

                    Text(
                        modifier = Modifier.clickable { isLogin = !isLogin },
                        text = if (isLogin) "Sign up" else "Log in",
                        style = AppTypography.bodySmall,
                        color = colors.primary
                    )

                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(8.dp))


                Spacer(Modifier.height(24.dp))

                AnimatedContent(
                    targetState = isLogin,
                    transitionSpec = {
                        val duration = 220
                        if (targetState) {
                            (slideInVertically(
                                animationSpec = tween(duration)
                            ) { it / 2 } + fadeIn(animationSpec = tween(duration))).togetherWith(
                                slideOutVertically(
                                    animationSpec = tween(duration)
                                ) { -it / 2 } + fadeOut(animationSpec = tween(duration)))
                        } else {
                            (slideInVertically(
                                animationSpec = tween(duration)
                            ) { -it / 2 } + fadeIn(animationSpec = tween(duration))).togetherWith(
                                slideOutVertically(
                                    animationSpec = tween(duration)
                                ) { it / 2 } + fadeOut(animationSpec = tween(duration)))
                        }
                    },
                    label = "auth-forms"
                ) { loginState ->
                    if (loginState) {
                        LoginForm(
                            email = loginEmail,
                            password = loginPassword,
                            onEmailChange = { loginEmail = it },
                            onPasswordChange = { loginPassword = it }
                        )
                    } else {
                        SignupForm(
                            email = signupEmail,
                            password = signupPassword,
                            confirmPassword = signupConfirmPassword,
                            name = signupName,
                            onEmailChange = { signupEmail = it },
                            onPasswordChange = { signupPassword = it },
                            onConfirmPasswordChange = { signupConfirmPassword = it },
                        )
                    }
                }

                if (error != null) {
                    Text(
                        text = error!!,
                        style = AppTypography.bodySmall,
                        color = colors.error
                    )
                }

                Spacer(Modifier.height(24.dp))


                AuthButton(
                    text = if (isLogin) "Login" else "Create account",
                    onClick = {
                        if (isLogin) {
                            viewModel.login(
                                username = loginEmail,
                                password = loginPassword
                            )
                        } else {
                            viewModel.register(
                                username = signupEmail,
                                password = signupPassword,
                                confirmPassword = signupConfirmPassword,
                                name = signupName
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )
    }
}

@Composable
private fun SignupForm(
    email: String,
    password: String,
    confirmPassword: String,
    name: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = name,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Name") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )
    }
}

@Composable
private fun AuthButton(
    text: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.primary),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues()
    ) {
        Text(
            text = text,
            style = AppTypography.bodyMedium
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun AuthScreenPreview() {
    val colors = MaterialTheme.colorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography
    ) {
        AuthScreen(navController = rememberNavController())
    }
}

