package com.dosei.fit.workout.di

import com.dosei.fit.workout.data.repository.WorkoutRepository
import com.dosei.fit.workout.list.ExercisesViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {
    single {
        WorkoutRepository(
            ioContext = get(named("io"))
        )
    }

    factory<CoroutineContext>(qualifier = named("main")) { Dispatchers.Main }
    factory<CoroutineContext>(qualifier = named("io")) { Dispatchers.IO }
    factory<CoroutineContext>(qualifier = named("default")) { Dispatchers.Default }
    factory<CoroutineContext>(qualifier = named("unconfined")) { Dispatchers.Unconfined }

    viewModel { ExercisesViewModel(get()) }
}