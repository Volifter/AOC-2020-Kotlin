package day25

import utils.*

const val MUL = 7

const val MOD = 20201227

fun parseInput(input: List<String>): Pair<Int, Int> =
    Pair(input[0].toInt(), input[1].toInt())

fun getLoopSize(n: Int): Int = generateSequence(1) { it * MUL % MOD }.indexOf(n)

fun part1(input: List<String>): Int {
    val (publicA, publicB) = parseInput(input)

    val privateA = getLoopSize(publicA)

    return powMod(
        publicB.toLong(),
        privateA,
        MOD.toLong()
    ).toInt()
}

fun main() {
    expect(part1(readInput("Day25_test")), 14897079)

    val input = readInput("Day25")
    println(part1(input))
}
