# Pratt

> A generic pratt parser library for Kotlin

![Dawn and trees](img/dawn_and_trees.jpg)

*Photographer: [EmirVildanov](https://github.com/EmirVildanov)*

## Example

We will parse simple mathematical expressions that contain

* Operands: lowercase latin letters `'a'..'z'`
* Prefix unary operators: `+` and `-`
* Infix binary operators: `+`, `-`, '*' and right associative power operators `^`
* Postfix unary operator: factorial `!`

```kotlin
enum class TokenType {
    ID, PLUS, MULTIPLY, MINUS, POWER, BANG
}

fun getTokenType(char: Char) = when (char) {
    in 'a'..'z' -> ID
    '+' -> PLUS
    '*' -> MULTIPLY
    '-' -> MINUS
    '^' -> POWER
    '!' -> BANG
    else -> error("Unknown token")
}

val parser = pratt<Char, TokenType> {
    getTokenType = ::getTokenType

    atomic(ID)

    // prefix
    prefix(PLUS) precedence 3
    prefix(MINUS) precedence 3
    // infix
    infix(PLUS) precedence 1
    infix(MINUS) precedence 1
    infix(MULTIPLY) precedence 2
    infix(POWER) precedence 4 associativity RIGHT
    // postfix
    postfix(BANG) precedence 5
}

fun main() {
    // builds AST
    val tree = parser.parse("-a+b*c!".asIterable())
    println(tree)
}
```

## Use as a dependency

* Go to the _Releases page_ and find out the latest version
* Then add _pratt_ as a Jitpack dependency

```kotlin
repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.Furetur:pratt:<latest version>")
}
```
