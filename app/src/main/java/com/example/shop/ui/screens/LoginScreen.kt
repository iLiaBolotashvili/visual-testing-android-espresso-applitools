package com.example.shop.ui.screens

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    validEmail: String = "demo@shop.com",
    onSsoClick: (() -> Unit)? = null
) {
    val focus = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf(validEmail) }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var showError by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val canSubmit = email.isNotBlank() && password.isNotBlank() && !loading

    fun submit() {
        if (!canSubmit) return
        scope.launch {
            attemptLogin(
                email, password, validEmail,
                setLoading = { loading = it },
                setError = { showError = it },
                onSuccess = onLoginSuccess
            )
        }
    }

    val bg = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
        ),
        tileMode = TileMode.Clamp
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .widthIn(max = 520.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {}

                Text(
                    "Welcome back",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    "Sign in to continue to Shop Demo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; showError = false },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPreviewKeyEvent { event ->
                            if (event.type == KeyEventType.KeyUp &&
                                event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER
                            ) {
                                focus.moveFocus(FocusDirection.Next)
                                true
                            } else false
                        }
                        .testTag("email_field")
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; showError = false },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPreviewKeyEvent { event ->
                            if (event.type == KeyEventType.KeyUp &&
                                event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER
                            ) {
                                focus.clearFocus()
                                submit()
                                true
                            } else false
                        }
                        .testTag("password_field")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                        Text("Remember me")
                    }
                    TextButton(onClick = { /* demo no-op */ }) {
                        Text("Forgot password?")
                    }
                }

                if (showError) {
                    Text(
                        "Invalid email or password.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("error_text")
                    )
                }

                Button(
                    onClick = { focus.clearFocus(); submit() },
                    enabled = canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("sign_in_button")
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp).alpha(0.9f)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Signing in…")
                    } else {
                        Text("Sign in")
                    }
                }

                if (onSsoClick != null) {
                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(Modifier.weight(1f))
                        Text("or", modifier = Modifier.padding(horizontal = 12.dp))
                        HorizontalDivider(Modifier.weight(1f))
                    }
                    OutlinedButton(
                        onClick = onSsoClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("sso_button")
                    ) {
                        Text("Continue with SSO")
                    }
                }

                Text(
                    "By signing in you agree to our Terms • Privacy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

private suspend fun attemptLogin(
    email: String,
    password: String,
    validEmail: String,
    setLoading: (Boolean) -> Unit,
    setError: (Boolean) -> Unit,
    onSuccess: () -> Unit
) {
    setLoading(true)
    setError(false)
    delay(350)
    val ok = email.trim().equals(validEmail, ignoreCase = true) && password.isNotBlank()
    if (ok) onSuccess() else setError(true)
    setLoading(false)
}
