package com.github.furetur

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

internal class InvalidExpressionsTest {
    companion object {
        private const val VALID_EXPRESSIONS_PATH = "src/test/resources/com/github/furetur/invalid_expressions.yaml"

        @JvmStatic
        fun getInputs(): Stream<String> {
            val text = File(VALID_EXPRESSIONS_PATH).readText()
            val testData = Yaml().decodeFromString<TestData>(text)
            return testData.data.stream().map {
                require(it.expected == null) { "Expected value for expression ${it.input} was not null but this test accepts only nulls" }
                it.input
            }
        }
    }

    @ParameterizedTest
    @MethodSource("getInputs")
    fun `should be parsed correctly`(input: String) {
        assertThrows<Exception> {
            input.parse()
        }
    }
}
