package com.dosei.fit.workout.data.model

data class Exercise(
    val id: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val equipment: Equipment,
    val currentWeightLoad: Int,
)

fun mockExercises(): List<Exercise> {
    val totalBody = MuscleGroup(1, "Total Body")
    val upperBody = MuscleGroup(2, "Upper Body")
    val core = MuscleGroup(3, "Core")
    val back = MuscleGroup(4, "Back")
    val lowerBody = MuscleGroup(5, "Lower Body")

    val dumbbell = Equipment(1, "Dumbbell")
    return listOf(
        Exercise(1, "Tricep Kickback", totalBody, dumbbell, 5),
        Exercise(2, "Shoulder Press", upperBody, dumbbell, 100),
        Exercise(3, "Side Bend", core, dumbbell, 5),
        Exercise(4, "Reverse fly", back, dumbbell, 1),
        Exercise(5, "Lunge", lowerBody, dumbbell, 1),
    )
}