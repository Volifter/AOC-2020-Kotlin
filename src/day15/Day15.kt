package day15

import utils.*

fun part1(input: List<String>, count: Int): Int {
    var last: Int
    val occurrences = input
        .single()
        .split(',')
        .map(String::toInt)
        .also { last = it.last() }
        .dropLast(1)
        .withIndex()
        .associate { (i, n) -> n to i + 1 }
        .toMutableMap()

    (occurrences.size + 2..count).map { i ->
        if (last !in occurrences) {
            occurrences[last] = i - 1
            last = 0
        } else {
            val lastOccurrence = occurrences[last]!!

            occurrences[last] = i - 1
            last = i - 1 - lastOccurrence
        }
    }

    return last
}

fun main() {
    expect(part1(readInput("Day15_test"), 2020), 436)
    expect(part1(readInput("Day15_test"), 30000000), 175594)

    val input = readInput("Day15")
    println(part1(input, 2020))
    println(part1(input, 30000000))
}
