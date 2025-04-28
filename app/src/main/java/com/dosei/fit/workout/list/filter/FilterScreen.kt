package com.dosei.fit.workout.list.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dosei.fit.workout.data.model.MuscleGroup
import com.dosei.fit.workout.data.model.mockExercises
import com.dosei.fit.workout.list.widget.NumberTextField

@Composable
fun FilterScreen(
    initial: FilterState,
    applyFilter: (FilterState) -> Unit,
    onClearFilter: () -> Unit,
    onClose: () -> Unit,
) {
    var filter by remember { mutableStateOf(initial) }
    FilterContent(
        state = filter,
        actions = FilterActions(
            onBack = onClose,
            onUpdateBookmarks = { filter = filter.copy(bookmarks = it) },
            onClearFilter = onClearFilter,
            onApplyFilter = { applyFilter(filter) },
            onChangeGroup = { group ->
                filter = if (group in filter.selectedMuscleGroups) {
                    filter.copy(selectedMuscleGroups = filter.selectedMuscleGroups - group)
                } else {
                    filter.copy(selectedMuscleGroups = filter.selectedMuscleGroups + group)
                }
            },
            onChangeMaxWeight = { newValue ->
                filter = filter.copy(maxWeight = newValue)
            },
            onChangeMinWeight = { newValue ->
                filter = filter.copy(minWeight = newValue)
            },
            onClose = onClose
        )
    )
}

data class FilterState(
    val availableMuscleGroups: Set<MuscleGroup>,
    val selectedMuscleGroups: Set<MuscleGroup>,
    val bookmarks: Boolean = false,
    val weightRange: IntRange,
    val minWeight: Int,
    val maxWeight: Int,
)

private data class FilterActions(
    val onBack: () -> Unit = {},
    val onUpdateBookmarks: (Boolean) -> Unit = {},
    val onClearFilter: () -> Unit = {},
    val onApplyFilter: () -> Unit = {},
    val onChangeGroup: (MuscleGroup) -> Unit = {},
    val onChangeMinWeight: (Int) -> Unit = {},
    val onChangeMaxWeight: (Int) -> Unit = {},
    val onClose: () -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterContent(
    state: FilterState,
    actions: FilterActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Filtrar exercícios") },
                actions = {
                    IconButton(onClick = actions.onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(Modifier.padding(16.dp)) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = actions.onClearFilter
                ) {
                    Text(text = "Limpar Filtros")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = actions.onApplyFilter
                ) {
                    Text(text = "Aplicar Filtros")
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                modifier = Modifier.padding(start = 13.dp, top = 24.dp, bottom = 8.dp),
                text = "Grupos musculares",
                style = MaterialTheme.typography.labelLarge
            )
            state.availableMuscleGroups.forEach { muscleGroup ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = muscleGroup in state.selectedMuscleGroups,
                        onCheckedChange = { actions.onChangeGroup(muscleGroup) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier
                            .clickable { actions.onChangeGroup(muscleGroup) }
                            .fillMaxWidth(),
                        text = muscleGroup.name
                    )
                }
            }

            Text(
                modifier = Modifier.padding(start = 13.dp, top = 24.dp, bottom = 8.dp),
                text = "Favoritos",
                style = MaterialTheme.typography.labelLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.bookmarks, onCheckedChange = actions.onUpdateBookmarks)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Exibir apenas favoritos")
            }

            Text(
                modifier = Modifier.padding(start = 13.dp, top = 24.dp, bottom = 8.dp),
                text = "Peso",
                style = MaterialTheme.typography.labelLarge
            )
            NumberTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = state.minWeight,
                onValueChange = actions.onChangeMinWeight,
                label = { Text(text = "Mínimo (Kg)") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            NumberTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = state.maxWeight,
                onValueChange = actions.onChangeMaxWeight,
                label = { Text(text = "Máximo (Kg)") },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        FilterContent(
            state = FilterState(
                availableMuscleGroups = mockExercises().map { it.muscleGroup }.toSet(),
                selectedMuscleGroups = emptySet(),
                bookmarks = true,
                weightRange = mockExercises()
                    .map { it.currentWeightLoad }
                    .let { it.min()..it.max() },
                minWeight = 2,
                maxWeight = 50
            ),
            actions = FilterActions()
        )
    }
}
