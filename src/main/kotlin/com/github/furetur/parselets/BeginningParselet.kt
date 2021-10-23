package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.Parser

/**
 * Parselet that parses expressions that *begin* with some special token. For example:
 * literal expressions (identifiers `a` and numbers `12`) -- consist of 1 token, therefore begin with ID or NUMBER token
 * prefix operators like `-` and `+` -- start with the operator token
 * if expressions like `if (a) 1 else 2` -- start with the IF token
 */
interface BeginningParselet<Tok, TokType> {
    fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok>
}
