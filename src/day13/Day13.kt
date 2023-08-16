package day13

import utils.*
import java.math.BigInteger
import kotlin.math.ceil

fun part1(input: List<String>): Int {
    val target = input.first().toInt()
    val timestamps = input.last().split(",").mapNotNull(String::toIntOrNull)
    val (bestId, bestTime) = timestamps.map { n ->
        Pair(n, ceil(target * 1.0 / n).toInt() * n)
    }.minBy { it.second }

    return bestId * (bestTime - target)
}

fun getCRM(values: List<Pair<Int, Int>>): BigInteger {
    val prod = values.fold(1.toBigInteger()) { acc, (_, mod) ->
        acc * mod.toBigInteger()
    }

    return values.sumOf { (rem, mod) ->
        val otherProd = prod / mod.toBigInteger()

        rem.toBigInteger() *
            otherProd *
            otherProd.modInverse(mod.toBigInteger())
    } % prod
}

fun part2(input: List<String>): Long {
    val schedule = input.last().split(",").mapIndexedNotNull { i, value ->
        value.toIntOrNull()?.let { Pair(it - i, it) }
    }

    return getCRM(schedule).toLong()
}

fun main() {
    val testInput = readInput("Day13_test")
    expect(part1(testInput), 295)
    expect(part2(testInput), 1068781)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
