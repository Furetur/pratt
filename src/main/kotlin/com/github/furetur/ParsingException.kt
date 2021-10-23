package com.github.furetur

open class ParsingException(msg: String) : IllegalArgumentException(msg)

class UnexpectedTokenException(val position: Int, val token: String, val expectedTokens: List<String>? = null) :
    ParsingException(
        run {
            val msg = "At $position: Unexpected token '$token'"
            if (expectedTokens != null) {
                msg + " expected ${expectedTokens.joinToString(", ")}"
            } else {
                msg
            }
        }
    )

class UnexpectedEofException(val position: Int) :
    ParsingException("At $position: Stream of tokens ended unexpectedly while parsing expression")
