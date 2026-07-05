package com.mk.androidshowcase.domain.useCase.base

abstract class UseCase<in Params, out Result> {

    protected abstract suspend fun run(params: Params): Result

    // Main-safety is the data layer's job (Room/Retrofit/DataStore are already main-safe); use cases
    // stay dispatcher-agnostic and delegate. Keeps use cases pure and VM tests deterministic.
    suspend operator fun invoke(params: Params): Result = run(params)
}

suspend operator fun <T> UseCase<None, T>.invoke(): T = invoke(None)

object None
