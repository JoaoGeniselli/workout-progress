package com.dosei.fit.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dosei.fit.workout.data.model.Training
import com.dosei.fit.workout.data.sample.sampleTraining
import com.dosei.fit.workout.list.widget.ExerciseRow
import com.dosei.fit.workout.list.widget.TableHeader
import kotlin.math.exp

@Composable
fun TrainingsScreen(
    controller: NavHostController
) {
//    TrainingsContent(
//        actions = TrainingsActions(
//            onBack = { controller.popBackStack() }
//        )
//    )
}

private data class TrainingsActions(
    val onBack: () -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrainingsContent(
    expandedIds: Set<Long>,
    items: List<Training>,
    actions: TrainingsActions,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(R.string.trainings_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_add_training)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(items) { item ->
                TrainingRow(item = item, expanded = item.id in expandedIds)
            }
        }
    }
}

@Composable
private fun TrainingRow(item: Training, expanded: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = { Text(text = item.name) },
            supportingContent = if (item.notes.isNotEmpty()) {
                { Text(text = item.notes) }
            } else null,
            trailingContent = {
                if (expanded) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = stringResource(R.string.action_collapse)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(R.string.action_expand)
                    )
                }
            }
        )
        AnimatedVisibility(visible = expanded) {
            Column {
                TableHeader()
                item.exercises.forEach {
                    ExerciseRow(modifier = Modifier, item = it)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.End),
                ) {
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.action_exclude)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.action_exclude))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.action_edit)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.action_edit))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true, locale = "pt-rBR")
@Composable
private fun Preview() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        TrainingsContent(
            expandedIds = setOf(2L),
            items = listOf(
                sampleTraining(id = 1L, name = "Treino A"),
                sampleTraining(id = 2L, name = "Treino B")
            ),
            actions = TrainingsActions()
        )
    }
}
