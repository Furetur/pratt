package com.github.furetur.dsl

import com.github.furetur.Parser
import com.github.furetur.parselets.AtomicParselet
import com.github.furetur.parselets.BeginningParselet
import com.github.furetur.parselets.FollowingParselet
import com.github.furetur.parselets.GroupingParselet

interface BeginningParseletBuilder<Tok, TokType> {
    fun build(): BeginningParselet<Tok, TokType>
}

interface FollowingParseletBuilder<Tok, TokType> {
    fun build(): FollowingParselet<Tok, TokType>
}

class ParserBuilder<Tok, TokType> {
    var getTokenType: ((Tok) -> TokType)? = null
    private val beginningParselets: MutableMap<TokType, BeginningParseletBuilder<Tok, TokType>> = mutableMapOf()
    private val followingParselets: MutableMap<TokType, FollowingParseletBuilder<Tok, TokType>> = mutableMapOf()

    fun addBeginningParseletBuilder(
        tokenType: TokType,
        beginningParseletBuilder: BeginningParseletBuilder<Tok, TokType>
    ) {
        if (tokenType !in beginningParselets) {
            beginningParselets[tokenType] = beginningParseletBuilder
        }
    }

    fun addFollowingParseletBuilder(
        tokenType: TokType,
        followingParseletBuilder: FollowingParseletBuilder<Tok, TokType>
    ) {
        if (tokenType !in followingParselets) {
            followingParselets[tokenType] = followingParseletBuilder
        }
    }

    infix fun TokType.isBeginningTokenOf(beginningParselet: BeginningParselet<Tok, TokType>) {
        addBeginningParseletBuilder(this, JustBeginningParselet(beginningParselet))
    }

    infix fun TokType.isFollowingTokenOf(followingParselet: FollowingParselet<Tok, TokType>) {
        addFollowingParseletBuilder(this, JustFollowingParselet(followingParselet))
    }

    fun atomic(vararg tokenTypes: TokType) {
        for (tokenType in tokenTypes) {
            tokenType isBeginningTokenOf AtomicParselet()
        }
    }

    fun prefix(tokenType: TokType): PrefixParseletBuilder<Tok, TokType> = PrefixParseletBuilder<Tok, TokType>().also {
        addBeginningParseletBuilder(tokenType, it)
    }

    fun infix(tokenType: TokType): InfixParseletBuilder<Tok, TokType> = InfixParseletBuilder<Tok, TokType>().also {
        addFollowingParseletBuilder(tokenType, it)
    }

    fun postfix(tokenType: TokType): PostfixParseletBuilder<Tok, TokType> =
        PostfixParseletBuilder<Tok, TokType>().also {
            addFollowingParseletBuilder(tokenType, it)
        }

    fun grouping(vararg bracketPairs: Pair<TokType, TokType>) {
        for (bracketPair in bracketPairs) {
            bracketPair.first isBeginningTokenOf GroupingParselet(bracketPair.second)
        }
    }

    fun build(): Parser<Tok, TokType> {
        val getTokenType = getTokenType
        requireNotNull(getTokenType) { "getTokenType is not set" }
        return Parser(
            getTokenType = getTokenType,
            beginningParselets = beginningParselets.mapValues { it.value.build() },
            followingParselets = followingParselets.mapValues { it.value.build() }
        )
    }
}

fun <Tok, TokType> pratt(init: ParserBuilder<Tok, TokType>.() -> Unit): Parser<Tok, TokType> =
    ParserBuilder<Tok, TokType>().apply(init).build()
