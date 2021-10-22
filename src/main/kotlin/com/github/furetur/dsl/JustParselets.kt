package com.github.furetur.dsl

import com.github.furetur.parselets.BeginningParselet
import com.github.furetur.parselets.FollowingParselet

class JustBeginningParselet<Tok, TokType>(private val beginningParselet: BeginningParselet<Tok, TokType>) :
    BeginningParseletBuilder<Tok, TokType> {
    override fun build(): BeginningParselet<Tok, TokType> = beginningParselet
}

class JustFollowingParselet<Tok, TokType>(private val followingParselet: FollowingParselet<Tok, TokType>) :
    FollowingParseletBuilder<Tok, TokType> {
    override fun build(): FollowingParselet<Tok, TokType> = followingParselet
}
