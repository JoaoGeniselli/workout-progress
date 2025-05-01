package com.dosei.fit.workout.list.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dosei.fit.workout.R
import com.dosei.fit.workout.list.ExercisesActions

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchTopBar(query: String, actions: ExercisesActions) {
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
            .padding(16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.action_search)
            )
        },
        trailingIcon = {
            Row {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { actions.onSearch("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.action_clear)
                        )
                    }
                }
                IconButton(onClick = actions.onClickFilter) {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = stringResource(R.string.action_filter)
                    )
                }
            }
        },
        placeholder = { Text(text = stringResource(R.string.exercises_title)) },
        content = {},
        query = query,
        onQueryChange = actions.onSearch,
        onSearch = {},
        onActiveChange = {},
        active = false
    )
}

@Preview
@Composable
private fun Preview() {
    SearchTopBar(query = "", actions = ExercisesActions())
}