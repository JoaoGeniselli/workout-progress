package com.dosei.fit.workout.list.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosei.fit.workout.data.model.Exercise
import com.dosei.fit.workout.data.model.mockExercises

@Composable
fun ExerciseRow(modifier: Modifier, item: Exercise) {
    Row(
        modifier = modifier,
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

@Preview
@Composable
private fun Preview() {
    Surface {
        ExerciseRow(modifier = Modifier.fillMaxWidth(), item = mockExercises().first())
    }
}