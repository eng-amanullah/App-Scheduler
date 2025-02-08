package com.amanullah.appscheduler.base.customview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String? = null,
    defaultValue: String,
    height: Dp = 48.dp,
    inputCallback: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        enabled = true,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .fillMaxWidth()
            .height(height = height),
        value = defaultValue,
        onValueChange = inputCallback,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = if (isSystemInDarkTheme()) Color.White else Color.Black, fontSize = 14.sp
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()

                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                ) {
                    if (defaultValue.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = hint ?: "",
                            style = LocalTextStyle.current.copy(
                                color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5F) else Color.Black.copy(
                                    alpha = 0.5F
                                ),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }

                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        SearchBar(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            hint = "Search App",
            defaultValue = "",
            inputCallback = { text ->

            }
        )
    }
}