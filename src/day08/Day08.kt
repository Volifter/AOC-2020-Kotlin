package day08

import utils.*

enum class Instruction { ACC, JMP, NOOP }

fun getInstructions(lines: List<String>): List<Pair<Instruction, Int>> =
    lines.map { line ->
        val (name, arg) = line.split(" ")
        val instruction = when (name) {
            "acc" -> Instruction.ACC
            "jmp" -> Instruction.JMP
            else -> Instruction.NOOP
        }

        Pair(instruction, arg.toInt())
    }

fun runInstructions(
    instructions: List<Pair<Instruction, Int>>
): Pair<Int, Boolean> {
    val visited = mutableSetOf<Int>()
    var i = 0
    var acc = 0

    while (i < instructions.size && i !in visited) {
        val instruction = instructions[i]

        visited.add(i)

        if (instruction.first == Instruction.ACC)
            acc += instruction.second

        i += if (instruction.first == Instruction.JMP) instruction.second else 1
    }

    return Pair(acc, i > instructions.lastIndex)
}

fun part1(input: List<String>): Int =
    runInstructions(getInstructions(input)).first

fun generateInstructionsPermutations(
    instructions: List<Pair<Instruction, Int>>
) = sequence {
    instructions.forEachIndexed { i, instruction ->
        val newType = when (instruction.first) {
            Instruction.JMP -> Instruction.NOOP
            Instruction.NOOP -> Instruction.JMP
            else -> return@forEachIndexed
        }

        val permutation = instructions.toMutableList()

        permutation[i] = Pair(newType, permutation[i].second)

        yield(permutation)
    }
}

fun part2(input: List<String>): Int? =
    generateInstructionsPermutations(getInstructions(input))
        .mapNotNull {
            runInstructions(it).let { (acc, hasFinished) ->
                acc.takeIf { hasFinished }
            }
        }
        .firstOrNull()

fun main() {
    val testInput = readInput("Day08_test")
    expect(part1(testInput), 5)
    expect(part2(testInput), 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
