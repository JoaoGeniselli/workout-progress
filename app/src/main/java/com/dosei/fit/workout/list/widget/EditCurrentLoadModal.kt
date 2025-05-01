package com.dosei.fit.workout.list.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dosei.fit.workout.R
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditCurrentLoadModal(
    state: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    initialValue: Int,
    initialSets: Int,
    initialRepetitions: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var value by remember { mutableIntStateOf(initialValue) }
    var sets by remember { mutableIntStateOf(initialSets) }
    var reps by remember { mutableIntStateOf(initialRepetitions) }

    fun onDone() {
        scope.launch { state.hide() }.invokeOnCompletion { onConfirm(value, sets, reps) }
    }

    val doneEnabled by remember(value) { derivedStateOf { value > 0 } }

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = { scope.launch { state.hide() }.invokeOnCompletion { onDismiss() } },
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.edit_exercise_modal_title),
            style = MaterialTheme.typography.titleMedium
        )

        WeightField(
            value = value,
            onUpdate = { value = it },
        )

        SetsField(
            value = sets,
            onUpdate = { sets = it },
        )

        RepsField(
            value = reps,
            onUpdate = { reps = it },
            onDone = { if (doneEnabled) onDone() },
        )

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp, bottom = 24.dp),
            onClick = { onDone() },
            enabled = doneEnabled
        ) {
            Text(text = stringResource(R.string.action_update))
        }
    }
}

@Composable
private fun WeightField(
    onUpdate: (Int) -> Unit,
    value: Int,
) {
    NumberTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        label = { Text(text = stringResource(R.string.edit_exercise_load_label)) },
        value = value,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        onValueChange = { newValue -> onUpdate(newValue) },
        isError = value == 0,
    )
}

@Composable
private fun SetsField(
    onUpdate: (Int) -> Unit,
    value: Int,
) {
    NumberTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        label = { Text(text = stringResource(R.string.edit_exercise_set_label)) },
        value = value,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        onValueChange = { newValue -> onUpdate(newValue) },
        isError = value == 0,
    )
}

@Composable
private fun RepsField(
    onUpdate: (Int) -> Unit,
    onDone: () -> Unit,
    value: Int,
) {
    NumberTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        label = { Text(text = stringResource(R.string.edit_exercise_repetitions_label)) },
        value = value,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                defaultKeyboardAction(ImeAction.Done)
                onDone()
            }
        ),
        onValueChange = { newValue -> onUpdate(newValue) },
        isError = value == 0,
    )
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Preview() {
    EditCurrentLoadModal(
        state = rememberStandardBottomSheetState(SheetValue.Expanded),
        initialValue = 10,
        initialRepetitions = 15,
        initialSets = 3,
        onDismiss = { },
        onConfirm = { _, _, _ -> }
    )
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PreviewError() {
    EditCurrentLoadModal(
        state = rememberStandardBottomSheetState(SheetValue.Expanded),
        initialValue = 0,
        initialRepetitions = 0,
        initialSets = 0,
        onDismiss = { },
        onConfirm = { _, _, _ -> }
    )
}