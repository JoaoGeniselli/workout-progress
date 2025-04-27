package com.dosei.fit.workout.data.model

data class UiError(
    val message: String,
    val cause: Throwable? = null,
)
