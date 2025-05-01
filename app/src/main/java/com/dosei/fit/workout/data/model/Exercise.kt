package com.dosei.fit.workout.data.model

data class Exercise(
    val id: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val equipment: Equipment,
    val currentWeightLoad: Int,
    val repetitions: Int,
    val sets: Int,
    val notes: String = "",
)
