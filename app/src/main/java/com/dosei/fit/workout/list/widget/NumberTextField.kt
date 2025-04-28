package com.dosei.fit.workout.list.widget

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NumberTextField(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    val fieldValue by remember(value) {
        derivedStateOf {
            val valueString = value.toString()
            TextFieldValue(
                text = valueString,
                selection = TextRange(valueString.length)
            )
        }
    }

    OutlinedTextField(
        modifier = modifier,
        maxLines = 1,
        minLines = 1,
        label = label,
        shape = shape,
        prefix = prefix,
        suffix = suffix,
        colors = colors,
        enabled = enabled,
        singleLine = true,
        isError = isError,
        value = fieldValue,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        visualTransformation = visualTransformation,
        onValueChange = { newFieldValue ->
            val newValue = newFieldValue.text.filter { it.isDigit() }.toIntOrNull() ?: 0
            val boundedValue = maxOf(range.first, minOf(newValue, range.last))
            onValueChange(boundedValue)
        },
        leadingIcon = {
            IconButton(
                modifier = Modifier.focusable(false),
                onClick = { onValueChange(maxOf(value.dec(), range.first)) },
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "remover 1"
                )
            }
        },
        trailingIcon = {
            IconButton(
                modifier = Modifier.focusable(false),
                onClick = { onValueChange(minOf(value.inc(), range.last)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "adicionar 1"
                )
            }
        },
    )
}

@Preview
@Composable
private fun Preview() {
    Surface {
        NumberTextField(
            modifier = Modifier.padding(16.dp),
            label = { Text(text = "Label") },
            value = 15,
            onValueChange = {}
        )
    }
}