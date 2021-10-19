package com.github.furetur

import com.charleskorn.kaml.Yaml
import com.github.furetur.cursor.ListTokenCursor
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
        val cursor = ListTokenCursor(input.tokenize())
        assertThrows<ParsingException> {
            parser.parse(cursor)
            // TODO: this is a quick dirty fix.
            // TODO: Idk if we should distinguish between ParsingErrors and situations when not the full string was parsed
            if (!cursor.isDone) {
                throw ParsingException("Cursor is not done")
            }
        }
    }
}
