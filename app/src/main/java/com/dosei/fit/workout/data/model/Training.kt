package com.dosei.fit.workout.data.model

data class Training(
    val id: Long,
    val name: String,
    val notes: String,
    val exercises: List<Exercise>,
)
