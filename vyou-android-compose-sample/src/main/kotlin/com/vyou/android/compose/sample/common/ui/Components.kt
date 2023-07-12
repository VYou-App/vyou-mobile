package com.vyou.android.compose.sample.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextHeader(text: String) {
    Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Medium)
}

@Composable
fun TextSubtitle(text: String) {
    Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
}

@Composable
fun BasicTextField(
    value: String,
    label: String,
    error: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onValueChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            isError = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
            trailingIcon = {
                if(error.isNotEmpty()) {
                    Icon(imageVector = Icons.Filled.Error, null, tint = MaterialTheme.colors.error)
                }
            }
        )
        if(error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colors.error)
        }
    }
}

@Composable
fun EmailTextField(email: String, label: String = "Email", error: String = "", onValueChange: (String) -> Unit) {
    val isError = error.isNotEmpty()
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Email, null)
            },
            trailingIcon = {
                if(isError) {
                    Icon(imageVector = Icons.Filled.Error, null, tint = MaterialTheme.colors.error)
                }
            }
        )
        if(isError) {
            Text(error, color = MaterialTheme.colors.error)
        }
    }

}

@Composable
fun PasswordTextField(password: String, passwordVisibility: Boolean, label: String = "Password", error: String = "", onPasswordVisibilityChange: () -> Unit, onValueChange: (String) -> Unit) {
    val isError = error.isNotEmpty()
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = password,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,  imeAction = ImeAction.Done),
            isError = isError,
            trailingIcon = {
                if(isError) {
                    Icon(imageVector = Icons.Filled.Error, null, tint = MaterialTheme.colors.error)
                } else {
                    val image = if(passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = onPasswordVisibilityChange) {
                        Icon(imageVector = image, null)
                    }
                }

            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Password, null)
            }
        )
        if(isError) {
            Text(error, color = MaterialTheme.colors.error)
        }
    }
}

@Composable
fun NumberTextField(number: Double, label: String, error: String = "", imeAction: ImeAction = ImeAction.Done, onValueChange: (Int) -> Unit = {}) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = number.toString(),
            onValueChange = { onValueChange.invoke(it.toIntOrNull() ?: 0) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            isError = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
            trailingIcon = {
                if(error.isNotEmpty()) {
                    Icon(imageVector = Icons.Filled.Error, null, tint = MaterialTheme.colors.error)
                }
            }
        )
        if(error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colors.error)
        }
    }
}

@Composable
fun TextLink(func: () -> Unit) {
    ClickableText(text = AnnotatedString("Did you forget your password?"), onClick = { _ ->
        func.invoke()
    }, style = TextStyle(color = MaterialTheme.colors.primary, fontSize = 16.sp, fontWeight = FontWeight.Medium))
}

@Composable
fun CheckboxText(checked: Boolean, text: String, onCheckedChange: (Boolean) -> Unit) {
    Row {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(text = text, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun LoadingButton(label: String, isLoading: Boolean, isEnabled: Boolean, modifier: Modifier = Modifier, func: () -> Unit) {
    Button(onClick = {
        if(!isLoading) {
            func.invoke()
        }
    }, enabled = isEnabled, modifier = modifier.fillMaxWidth()) {
        if(isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
        } else {
            Text(text = label.uppercase())
        }
    }
}

@Composable
fun LoadingOutlinedButton(label: String, isLoading: Boolean, isEnabled: Boolean, func: () -> Unit) {
    OutlinedButton(onClick = {
        if(!isLoading) {
            func.invoke()
        }
    }, enabled = isEnabled, modifier = Modifier.fillMaxWidth()) {
        if(isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colors.primary)
        } else {
            Text(text = label.uppercase())
        }
    }
}