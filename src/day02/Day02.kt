package day02

import utils.*

fun part1(input: List<String>): Int =
    input.count { line ->
        val (policy, password) = line.split(": ")
        val (range, c) = policy.split(" ")
        val (from, to) = range.split("-").map(String::toInt)

        password.count { it == c.single() } in (from..to)
    }

fun part2(input: List<String>): Int =
    input.count { line ->
        val (policy, password) = line.split(": ")
        val (range, c) = policy.split(" ")

        range.split("-")
            .map { if (password[it.toInt() - 1] == c.single()) 1 else 0 }
            .reduce(Int::xor) == 1
    }

fun main() {
    val testInput = readInput("Day02_test")
    expect(part1(testInput), 2)
    expect(part2(testInput), 1)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
