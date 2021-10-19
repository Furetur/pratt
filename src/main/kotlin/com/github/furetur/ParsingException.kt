package com.github.furetur

open class ParsingException(msg: String) : IllegalArgumentException(msg)

class UnexpectedTokenException(val token: Token) : ParsingException("Unexpected token $token")

class UnexpectedEndException : ParsingException("Stream of tokens ended unexpectedly while parsing expression")
