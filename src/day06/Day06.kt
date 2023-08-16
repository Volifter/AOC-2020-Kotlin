package day06

import utils.*

fun getGroups(lines: List<String>): List<List<String>> =
    lines
        .joinToString("\n")
        .split("\n\n")
        .map { group ->
            group.split("\n")
        }

fun part1(input: List<String>): Int =
    getGroups(input)
        .sumOf {
            it.fold(setOf<Char>()) { acc, line -> acc + line.toSet() }.size
        }

fun part2(input: List<String>): Int =
    getGroups(input)
        .sumOf { it.map(String::toSet).reduce(Set<Char>::intersect).size }

fun main() {
    val testInput = readInput("Day06_test")
    expect(part1(testInput), 11)
    expect(part2(testInput), 6)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
