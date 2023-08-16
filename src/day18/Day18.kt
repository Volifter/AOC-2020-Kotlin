package day18

import utils.*
import java.util.Stack

val TOKEN_REGEX = """(\d+|[^ ])""".toRegex()

fun parseInput(lines: List<String>): List<List<String>> =
    lines.map { line -> TOKEN_REGEX.findAll(line).map { it.value }.toList() }

fun applyOperation(operation: Char, a: Long, b: Long) = when (operation) {
    '+' -> a + b
    '-' -> a - b
    '*' -> a * b
    '/' -> a / b
    else -> throw RuntimeException("Invalid operation $operation")
}

fun solve(tokens: List<String>, priorities: Map<Char, Int>): Long {
    val valueStack = Stack<Long>()
    val operatorStack = Stack<Char>()
    val getPriority = { c: Char? ->
        when (c) {
            null -> -1
            '(' -> -1
            ')' -> 0
            else -> priorities.getOrDefault(c, 0)
        }
    }

    tokens.forEach { token ->
        val n = token.toLongOrNull()

        when {
            token == "(" -> {
                operatorStack.push(token.single())
            }
            n == null -> {
                val priority = getPriority(token.single())

                while (getPriority(operatorStack.lastOrNull()) >= priority) {
                    valueStack.push(applyOperation(
                        operatorStack.pop(),
                        valueStack.pop(),
                        valueStack.pop()
                    ))
                }

                if (token == ")") {
                    assert(operatorStack.peek() == '(')
                    operatorStack.pop()
                } else {
                    operatorStack.push(token.single())
                }
            }
            else -> {
                valueStack.push(n)
            }
        }
    }

    while (operatorStack.isNotEmpty()) {
        valueStack.push(applyOperation(
            operatorStack.pop(),
            valueStack.pop(),
            valueStack.pop()
        ))
    }

    assert(valueStack.isEmpty())

    return valueStack.single()
}

fun part1(input: List<String>): Long {
    val priorities = mapOf<Char, Int>()

    return parseInput(input).sumOf { solve(it, priorities) }
}

fun part2(input: List<String>): Long {
    val priorities = mapOf('+' to 1)

    return parseInput(input).sumOf { solve(it, priorities) }
}

fun main() {
    expect(part1(readInput("Day18_test")), 26386)
    expect(part2(readInput("Day18_test")), 693942)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
