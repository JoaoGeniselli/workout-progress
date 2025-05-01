package com.dosei.fit.workout.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.dosei.fit.workout.data.model.Exercise
import com.dosei.fit.workout.data.sample.sampleExercises
import com.dosei.fit.workout.list.filter.FilterScreen
import com.dosei.fit.workout.list.filter.FilterState
import com.dosei.fit.workout.list.widget.EditCurrentLoadModal
import com.dosei.fit.workout.list.widget.ExerciseRow
import com.dosei.fit.workout.list.widget.SearchTopBar
import com.dosei.fit.workout.list.widget.TableHeader
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
    var query by remember { mutableStateOf("") }

    var filterState by remember(items) {
        mutableStateOf(createFilter(items))
    }

    val filteredItems by remember(query, filterState, items) {
        derivedStateOf {
            filterItems(query, items, filterState)
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
            query = query,
            actions = ExercisesActions(
                onBack = { controller.popBackStack() },
                onClickFilter = { scope.launch { drawerState.open() } },
                onClickEdit = { editingExercise = it },
                onSearch = { query = it }
            )
        )
    }

    editingExercise?.let { exercise ->
        EditCurrentLoadModal(
            initialValue = exercise.currentWeightLoad,
            onDismiss = { editingExercise = null },
            onConfirm = { newLoad, newSets, newReps ->
                viewModel.onUpdateLoad(exercise, newLoad, newSets, newReps)
                editingExercise = null
            },
            initialSets = exercise.sets,
            initialRepetitions = exercise.repetitions
        )
    }
}

data class ExercisesActions(
    val onBack: () -> Unit = {},
    val onClickFilter: () -> Unit = {},
    val onClickEdit: (Exercise) -> Unit = {},
    val onSearch: (String) -> Unit = {},
)

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ExercisesContent(
    query: String,
    items: List<Exercise>,
    actions: ExercisesActions,
) {
    Scaffold(
        topBar = {
            SearchTopBar(query = query, actions = actions)
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            stickyHeader { TableHeader(Modifier.fillParentMaxWidth()) }

            items(items) { item ->
                ExerciseRow(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .clickable { actions.onClickEdit(item) },
                    item = item
                )
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

private fun filterItems(query: String, items: List<Exercise>, filter: FilterState): List<Exercise> {
    val weightRange = filter.minWeight..filter.maxWeight
    val lowerQuery = query.lowercase()
    return if (query.isNotEmpty()) {
        items.filter { exercise ->
            exercise.muscleGroup in filter.selectedMuscleGroups
                    && exercise.currentWeightLoad in weightRange
                    && (lowerQuery in exercise.name.lowercase()
                    || lowerQuery in exercise.muscleGroup.name.lowercase()
                    || lowerQuery in exercise.equipment.name.lowercase())
        }
    } else {
        items.filter { exercise ->
            exercise.muscleGroup in filter.selectedMuscleGroups
                    && exercise.currentWeightLoad in weightRange
        }
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
private fun Preview() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        ExercisesContent(
            items = sampleExercises(),
            actions = ExercisesActions(),
            query = ""
        )
    }
}
