package day09

import utils.*

fun part1(input: List<String>, size: Int): Long {
    val numbers = input.map(String::toLong)
    val preceding = numbers.slice(0 until size).toMutableSet()

    numbers.slice(size..numbers.lastIndex).forEachIndexed { i, n ->
        if (preceding.none { n - it in preceding })
            return n

        preceding.remove(numbers[i])
        preceding.add(n)
    }

    return 0
}

fun part2(input: List<String>, size: Int): Long {
    val target = part1(input, size)
    val numbers = input.map(String::toLong)
    var sum = numbers.first()
    var start = 0
    var end = 0

    while (sum != target) {
        if (sum > target) {
            sum -= numbers[start]
            start++
        } else {
            end++
            sum += numbers[end]
        }
    }

    val interval = numbers.slice(start..end)

    return interval.min() + interval.max()
}

fun main() {
    val testInput = readInput("Day09_test")
    expect(part1(testInput, 5), 127)
    expect(part2(testInput, 5), 62)

    val input = readInput("Day09")
    println(part1(input, 25))
    println(part2(input, 25))
}
