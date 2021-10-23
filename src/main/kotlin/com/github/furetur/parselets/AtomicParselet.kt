package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.Parser
import com.github.furetur.TokenNode

/**
 * Parselet for parsing terminals such as identifiers, numbers and etc
 */
class AtomicParselet<Tok, TokType> : BeginningParselet<Tok, TokType> {
    override fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok> {
        return TokenNode(beginningToken)
    }
}
