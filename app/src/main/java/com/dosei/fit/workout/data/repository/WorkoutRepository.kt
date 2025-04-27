package com.dosei.fit.workout.data.repository

import com.dosei.fit.workout.data.model.Exercise
import com.dosei.fit.workout.data.model.mockExercises
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WorkoutRepository(
    private val ioContext: CoroutineContext,
) {

    private val _exercises = MutableStateFlow(mockExercises())
    val exercises: StateFlow<List<Exercise>> get() = _exercises

    suspend fun updateWeightLoad(id: Int, newLoad: Int) = withContext(ioContext) {
        val updated = _exercises.value.toMutableList().apply {
            replaceAll { exercise ->
                if (exercise.id == id) {
                    exercise.copy(currentWeightLoad = newLoad)
                } else {
                    exercise
                }
            }
        }
        _exercises.emit(updated)
    }
}