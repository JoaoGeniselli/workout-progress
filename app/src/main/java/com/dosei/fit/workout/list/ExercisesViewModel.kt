package com.dosei.fit.workout.list

import com.dosei.fit.workout.base.BaseViewModel
import com.dosei.fit.workout.data.model.Exercise
import com.dosei.fit.workout.data.repository.WorkoutRepository

class ExercisesViewModel(
    private val repository: WorkoutRepository,
) : BaseViewModel() {

    val items = repository.exercises

    fun onUpdateLoad(item: Exercise, newLoad: Int, newSets: Int, newReps: Int) {
        request {
            repository.updateWeightLoad(item.id, newLoad, newSets, newReps)
        }
    }
}