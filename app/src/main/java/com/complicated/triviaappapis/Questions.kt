package com.complicated.triviaappapis

import kotlinx.serialization.Serializable

@Serializable
data class Questions(
    val response_code: Int,
    val results: List<Result>
)

@Serializable
data class Result(
    val type: String,
    val difficulty: String,
    val category: String,
    var question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

