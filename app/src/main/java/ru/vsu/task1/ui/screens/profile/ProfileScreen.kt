package ru.vsu.task1.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingView
import ru.vsu.task1.ui.composables.generic.topbar.DefaultTopBar
import ru.vsu.task1.ui.navigation.AppBarViewModel
import ru.vsu.task1.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    appBarViewModel: AppBarViewModel = koinInject(),
    viewModel: ProfileViewModel = koinInject(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        appBarViewModel.setTopBar {
            DefaultTopBar(
                modifier = Modifier,
                title = "Profile",
                onClickOnHamburger = {}
            )
        }
        appBarViewModel.showBottomBar()
        viewModel.fetchUserInfo()
    }

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val colors = MaterialTheme.colorScheme

    var editingField by remember { mutableStateOf<ProfileField?>(null) }
    var inputValue by remember { mutableStateOf("") }

    fun openEditor(field: ProfileField) {
        editingField = field
        inputValue = when (field) {
            ProfileField.Name -> user?.fullName.orEmpty()
            ProfileField.Email -> user?.email.orEmpty()
            ProfileField.Password -> user?.password.orEmpty()
        }
    }

    LoadingView(
        isLoading = isLoading,
        isError = error != null,
        onLoading = { Loading() },
        onError = { ErrorMessage { viewModel.fetchUserInfo() } }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileEditableRow(
                label = "Name",
                value = user?.fullName ?: "Not available",
                onEdit = { openEditor(ProfileField.Name) }
            )

            ProfileEditableRow(
                label = "Email",
                value = user?.email ?: "Not available",
                onEdit = { openEditor(ProfileField.Email) }
            )

            ProfileEditableRow(
                label = "Password",
                value = user?.password ?: "Not available",
                onEdit = { openEditor(ProfileField.Password) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("auth") {
                        popUpTo("auth") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.error,
                    contentColor = colors.onError
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sign out",
                    style = AppTypography.bodyMedium
                )
            }
        }
    }

    if (editingField != null) {
        val field = editingField ?: return
        ProfileEditSheet(
            field = field,
            value = inputValue,
            onValueChange = { inputValue = it },
            onDismiss = { editingField = null },
            onSave = {
                viewModel.updateUserField(field, inputValue)
                editingField = null
            }
        )
    }
}

@Composable
private fun ProfileEditableRow(
    label: String,
    value: String,
    onEdit: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                style = AppTypography.labelSmall,
                color = colors.onBackground
            )

            Text(
                text = value,
                style = AppTypography.bodyMedium,
                color = colors.onBackground
            )
        }
        TextButton(onClick = onEdit) {
            Text(
                text = "Edit",
                style = AppTypography.bodySmall,
                color = colors.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileEditSheet(
    field: ProfileField,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val title = when (field) {
        ProfileField.Name -> "Edit name"
        ProfileField.Email -> "Edit email"
        ProfileField.Password -> "Edit password"
    }
    val keyboardType = when (field) {
        ProfileField.Email -> KeyboardType.Email
        ProfileField.Password -> KeyboardType.Password
        else -> KeyboardType.Text
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                label = { Text(title) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Save",
                    style = AppTypography.bodyMedium
                )
            }
        }
    }
}
