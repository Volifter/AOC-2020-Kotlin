package day05

import utils.*

fun getSeatIds(lines: List<String>): List<Int> =
    lines.map { line ->
        val row = line
            .substring(0, 7)
            .replace('F', '0')
            .replace('B', '1')
            .toInt(2)
        val col = line
            .substring(7)
            .replace('L', '0')
            .replace('R', '1')
            .toInt(2)

        row * 8 + col
    }

fun part1(input: List<String>): Int =
    getSeatIds(input).maxOf { it }

fun part2(input: List<String>): Int =
    getSeatIds(input)
        .sorted()
        .zipWithNext()
        .first { (a, b) -> a + 1 != b }.first + 1

fun main() {
    val testInput = readInput("Day05_test")
    expect(part1(testInput), 820)
    expect(part2(testInput), 120)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
