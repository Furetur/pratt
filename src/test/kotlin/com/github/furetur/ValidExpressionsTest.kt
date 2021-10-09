package com.github.furetur

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

internal class ValidExpressionsTest {
    companion object {
        private const val VALID_EXPRESSIONS_PATH = "src/test/resources/com/github/furetur/valid_expressions.yaml"

        @JvmStatic
        fun getInputsAndExpectedOutputs(): Stream<Arguments> {
            val text = File(VALID_EXPRESSIONS_PATH).readText()
            val testData = Yaml().decodeFromString<TestData>(text)
            return testData.data.stream().map {
                requireNotNull(it.expected) { "Expected value for expression ${it.input} was null but this test does not accept null" }
                Arguments.of(it.input, it.expected)
            }
        }
    }

    @ParameterizedTest
    @MethodSource("getInputsAndExpectedOutputs")
    fun `should be parsed correctly`(input: String, expected: String) {
        assertEquals(expected.filter { !it.isWhitespace() }, input.parse().stringify().filter { !it.isWhitespace() })
    }
}
