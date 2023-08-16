package day01

import utils.*

fun getNumbersSum(numbers: Set<Int>, sum: Int, n: Int): List<Int>? = when {
    n < 1 -> throw Error("n must be at least 1")
    n == 1 -> listOf(sum).takeIf { sum in numbers }
    else -> numbers.firstNotNullOfOrNull { number ->
        getNumbersSum(
            numbers - number,
            sum - number,
            n - 1
        )?.let { it + number }
    }
}

fun part1(input: List<String>): Int =
    getNumbersSum(
        input.map(String::toInt).toSet(),
        2020,
        2
    )!!.reduce(Int::times)

fun part2(input: List<String>): Int =
    getNumbersSum(
        input.map(String::toInt).toSet(),
        2020,
        3
    )!!.reduce(Int::times)

fun main() {
    val testInput = readInput("Day01_test")
    expect(part1(testInput), 514579)
    expect(part2(testInput), 241861950)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
