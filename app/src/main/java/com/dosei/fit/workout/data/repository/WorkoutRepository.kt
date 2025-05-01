package com.dosei.fit.workout.data.repository

import com.dosei.fit.workout.data.model.Exercise
import com.dosei.fit.workout.data.sample.sampleExercises
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WorkoutRepository(
    private val ioContext: CoroutineContext,
) {

    private val _exercises = MutableStateFlow(sampleExercises())
    val exercises: StateFlow<List<Exercise>> get() = _exercises

    suspend fun updateExercise(
        id: Int,
        newLoad: Int,
        sets: Int,
        reps: Int
    ) = withContext(ioContext) {
        delay(1000L)
        val updated = _exercises.value.toMutableList().apply {
            replaceAll { exercise ->
                if (exercise.id == id) {
                    exercise.copy(currentWeightLoad = newLoad, sets = sets, repetitions = reps)
                } else {
                    exercise
                }
            }
        }
        _exercises.emit(updated)
    }
}