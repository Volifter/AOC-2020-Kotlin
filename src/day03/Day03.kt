package day03

import utils.*

fun slide(lines: List<String>, stepX: Int, stepY: Int): Int =
    lines
        .filterIndexed { i, _ -> i % stepY == 0 }
        .drop(1)
        .withIndex()
        .count { (i, line) -> line[(i + 1) * stepX % line.length] == '#' }

fun part1(input: List<String>): Int = slide(input, 3, 1)

fun part2(input: List<String>): Long =
    listOf(
        Pair(1, 1),
        Pair(3, 1),
        Pair(5, 1),
        Pair(7, 1),
        Pair(1, 2)
    )
        .map { (stepX, stepY) -> slide(input, stepX, stepY).toLong() }
        .reduce(Long::times)

fun main() {
    val testInput = readInput("Day03_test")
    expect(part1(testInput), 7)
    expect(part2(testInput), 336)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
