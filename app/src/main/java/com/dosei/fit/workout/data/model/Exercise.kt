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

fun mockExercises(): List<Exercise> {
    val totalBody = MuscleGroup(1, "Total")
    val upperBody = MuscleGroup(2, "Superior")
    val core = MuscleGroup(3, "Core")
    val back = MuscleGroup(4, "Costas")
    val lowerBody = MuscleGroup(5, "Inferior")

    val dumbbell = Equipment(1, "Halteres")
    return listOf(
        Exercise(1, "Tríceps Francês", totalBody, dumbbell, 5, 15, 3),
        Exercise(2, "Dv. Ombros", upperBody, dumbbell, 8, 15, 3),
        Exercise(3, "Ext. Tríceps em Pé", core, dumbbell, 5, 15, 3),
        Exercise(4, "Remada curvada", back, dumbbell, 1, 15, 3),
        Exercise(5, "Agachamento", lowerBody, dumbbell, 1, 15, 3),
    )
}