package day19

import utils.*

interface Instruction {
    fun match(line: String, offset: Int = 0): Sequence<Int>
}

data class ValueInstruction(val value: Char) : Instruction {
    override fun match(line: String, offset: Int) = sequence {
        if (line.getOrNull(offset) == value)
            yield(offset + 1)
    }
}

data class ReferenceInstruction(
    var groups: List<List<Instruction>>? = null
) : Instruction {
    override fun match(line: String, offset: Int) = sequence {
        groups!!.forEach { group ->
            var seq = listOf(offset)

            group.forEach { child ->
                seq = seq.flatMap { start -> child.match(line, start) }
            }

            yieldAll(seq)
        }
    }
}

fun parseInput(
    lines: List<String>,
    extraLines: List<String> = listOf()
): Pair<Instruction, List<String>> {
    val instructionGroups = mutableMapOf<Int, List<List<Int>>>()
    val instructions = (lines.takeWhile { it.isNotEmpty() } + extraLines)
        .associate { line ->
            val (id, data) = line.split(": ").let { Pair(it[0].toInt(), it[1]) }

            id to when {
                data.first() == '"' && data.last() == '"' -> {
                    ValueInstruction(data[1])
                }

                else -> {
                    instructionGroups[id] = data.split(" | ").map { part ->
                        part.split(" ").map { it.toInt() }
                    }

                    ReferenceInstruction()
                }
            }
        }

    instructionGroups.forEach { (id, groups) ->
        (instructions[id] as ReferenceInstruction).groups =
            groups.map { group -> group.map { instructions[it]!! } }
    }

    return Pair(instructions[0]!!, lines.drop(instructions.size + 1))
}

fun solve(root: Instruction, lines: List<String>) =
    lines.count { line -> root.match(line).any { it == line.length } }

fun part1(input: List<String>): Int {
    val (root, lines) = parseInput(input)

    return solve(root, lines)
}

fun part2(input: List<String>): Int {
    val (root, lines) = parseInput(
        input,
        listOf("8: 42 | 42 8", "11: 42 31 | 42 11 31")
    )

    return solve(root, lines)
}

fun main() {
    expect(part1(readInput("Day19_test_a")), 2)
    expect(part1(readInput("Day19_test_b")), 3)
    expect(part2(readInput("Day19_test_b")), 12)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
