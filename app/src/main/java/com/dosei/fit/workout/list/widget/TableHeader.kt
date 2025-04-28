package com.dosei.fit.workout.list.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LazyItemScope.TableHeader() {
    Row(
        modifier = Modifier.fillParentMaxWidth()
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

@Preview
@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun Preview() {
    Surface {
        LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
            stickyHeader { TableHeader() }
        }
    }
}