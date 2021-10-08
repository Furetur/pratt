package com.github.furetur

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestData(val data: List<TestExpression>)

@Serializable
data class TestExpression(
    @SerialName("q") val input: String,
    @SerialName("a") val expected: String? = null
)
