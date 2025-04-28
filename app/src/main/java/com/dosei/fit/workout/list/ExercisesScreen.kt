package com.dosei.fit.workout.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dosei.fit.workout.R
import com.dosei.fit.workout.data.model.Exercise
import com.dosei.fit.workout.data.model.mockExercises
import com.dosei.fit.workout.list.filter.FilterScreen
import com.dosei.fit.workout.list.filter.FilterState
import com.dosei.fit.workout.list.widget.EditCurrentLoadModal
import com.dosei.fit.workout.ui.theme.GrayAlt
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    controller: NavHostController,
    viewModel: ExercisesViewModel = koinViewModel(),
) {
    val items by viewModel.items.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var filterState by remember(items) {
        mutableStateOf(createFilter(items))
    }

    val filteredItems by remember(filterState, items) {
        derivedStateOf {
            filterItems(items, filterState)
        }
    }

    var editingExercise by remember { mutableStateOf<Exercise?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                FilterScreen(
                    initial = filterState,
                    applyFilter = {
                        filterState = it
                        scope.launch { drawerState.close() }
                    },
                    onClearFilter = {
                        filterState = createFilter(items)
                        scope.launch { drawerState.close() }
                    },
                    onClose = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        ExercisesContent(
            items = filteredItems,
            actions = ExercisesActions(
                onBack = { controller.popBackStack() },
                onClickFilter = { scope.launch { drawerState.open() } },
                onClickEdit = { editingExercise = it }
            )
        )
    }

    editingExercise?.let { exercise ->
        EditCurrentLoadModal(
            initialValue = exercise.currentWeightLoad,
            onDismiss = { editingExercise = null },
            onConfirm = { newLoad ->
                viewModel.onUpdateLoad(exercise, newLoad)
                editingExercise = null
            },
            initialSets = exercise.sets,
            initialRepetitions = exercise.repetitions
        )
    }
}

private data class ExercisesActions(
    val onBack: () -> Unit = {},
    val onClickFilter: () -> Unit = {},
    val onClickEdit: (Exercise) -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ExercisesContent(
    items: List<Exercise>,
    actions: ExercisesActions,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Exercícios") },
                actions = {
                    IconButton(onClick = actions.onClickFilter) {
                        Icon(imageVector = Icons.Default.FilterAlt, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
//            verticalArrangement = spacedBy(8.dp)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillParentMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        modifier = Modifier.weight(1.5f),
                        text = "Nome",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = "Séries",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.End
                    )
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = "Reps",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.End
                    )
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = "Peso",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }

            items(items) { item ->
                Row(
                    modifier = Modifier.fillParentMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(Modifier.weight(1.5f)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = buildAnnotatedString {
                                append(item.muscleGroup.name)
                                append(" • ")
                                append(item.equipment.name)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = item.sets.toString(),
                        textAlign = TextAlign.End
                    )
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = item.repetitions.toString(),
                        textAlign = TextAlign.End
                    )
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = buildAnnotatedString {
                            append(item.currentWeightLoad.toString())
                            withStyle(
                                SpanStyle(
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append("Kg")
                            }
                        },
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

private fun createFilter(items: List<Exercise>): FilterState {
    val groups = items.map { it.muscleGroup }.toSet()
    val range = items
        .map { it.currentWeightLoad }
        .let { it.min()..it.max() }

    return FilterState(
        availableMuscleGroups = groups,
        selectedMuscleGroups = groups,
        bookmarks = false,
        weightRange = range,
        minWeight = range.first,
        maxWeight = range.last
    )
}

private fun filterItems(items: List<Exercise>, filter: FilterState): List<Exercise> {
    val weightRange = filter.minWeight..filter.maxWeight
    return items.filter { exercise ->
        exercise.muscleGroup in filter.selectedMuscleGroups
                && exercise.currentWeightLoad in weightRange
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        ExercisesContent(
            items = mockExercises(),
            actions = ExercisesActions()
        )
    }
}
