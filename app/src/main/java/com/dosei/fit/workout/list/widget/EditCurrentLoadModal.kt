package com.dosei.fit.workout.list.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditCurrentLoadModal(
    state: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    initialValue: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var value by remember { mutableIntStateOf(initialValue) }

    fun onDone() {
        scope.launch { state.hide() }.invokeOnCompletion { onConfirm(value) }
    }

    val doneEnabled by remember(value) { derivedStateOf { value > 0 } }

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = { scope.launch { state.hide() }.invokeOnCompletion { onDismiss() } },
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Digite a nova carga (Kg):",
            style = MaterialTheme.typography.titleMedium
        )

        val fieldValue by remember(value) {
            derivedStateOf {
                val valueString = value.toString()
                TextFieldValue(
                    text = valueString,
                    selection = TextRange(valueString.length)
                )
            }
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            value = fieldValue,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                    if (doneEnabled) onDone()
                }
            ),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            onValueChange = { newFieldValue ->
                value = newFieldValue.text.filter { it.isDigit() }.toIntOrNull() ?: 0
            },
            leadingIcon = {
                IconButton(onClick = { value = maxOf(0, value.dec()) }) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "remover 1kg"
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { value = value.inc() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "adicionar 1kg"
                    )
                }
            },
            isError = doneEnabled.not(),
            supportingText = if (doneEnabled.not()) {
                { Text(text = "O novo peso deve ser maior que zero.") }
            } else null
        )

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp, bottom = 24.dp),
            onClick = { onDone() },
            enabled = doneEnabled
        ) {
            Text(text = "Atualizar")
        }
    }
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Preview() {
    EditCurrentLoadModal(
        state = rememberStandardBottomSheetState(SheetValue.Expanded),
        initialValue = 10,
        onDismiss = { },
        onConfirm = { }
    )
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PreviewError() {
    EditCurrentLoadModal(
        state = rememberStandardBottomSheetState(SheetValue.Expanded),
        initialValue = 0,
        onDismiss = { },
        onConfirm = { }
    )
}