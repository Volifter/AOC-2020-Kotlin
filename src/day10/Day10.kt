package day10

import utils.*

val OFFSETS = (1..3)

fun part1(input: List<String>): Int {
    val numbers = input.map(String::toInt).toSet()
    val diffs = OFFSETS.associateWith { 0 }.toMutableMap()

    generateSequence(0) { i ->
        val step = OFFSETS.firstOrNull { (i + it) in numbers }
            ?: return@generateSequence null

        diffs[step] = diffs[step]!! + 1

        i + step
    }.count()

    return diffs[1]!! * (diffs[3]!! + 1)
}

fun countWays(
    numbers: Set<Int>,
    i: Int = 0,
    cache: MutableMap<Int, Long> = mutableMapOf()
): Long =
    cache.getOrPut(i) {
        OFFSETS
            .filter { i + it in numbers }
            .sumOf { countWays(numbers, i + it, cache) }
            .takeIf { it != 0L }
            ?: 1
    }

fun part2(input: List<String>): Long =
    countWays(input.map(String::toInt).toSet())

fun main() {
    val testInput = readInput("Day10_test")
    expect(part1(testInput), 220)
    expect(part2(testInput), 19208)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
