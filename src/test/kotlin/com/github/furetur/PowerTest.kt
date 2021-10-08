package com.github.furetur

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PowerTest {
    @Test
    fun `power should be right associative 1^2^3^---^100`() {
        val expression = (1..100).joinToString("^")
        var expected = "100"
        for (i in 99 downTo 1) {
            expected = "($i^$expected)"
        }
        assertEquals(expected, expression.parse().stringify())
    }
}
