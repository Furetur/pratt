package com.github.furetur

interface Visitor<Tok, R> {
    fun visit(node: Node<Tok>): R
}
