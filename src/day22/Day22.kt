package day22

import utils.*

fun parseInput(input: List<String>): List<ArrayDeque<Int>> {
    val cards = mutableListOf<Int>()
    val result = mutableListOf<ArrayDeque<Int>>()

    (input + "").forEach { line ->
        when {
            line.startsWith("Player ") -> {}
            line.isNotEmpty() -> cards += listOf(line.toInt())
            else -> {
                result += ArrayDeque(cards)
                cards.clear()
            }
        }
    }

    return result
}

// A wins -> false, B wins -> true
fun play(deckA: ArrayDeque<Int>, deckB: ArrayDeque<Int>): Boolean {
    while (deckA.isNotEmpty() && deckB.isNotEmpty()) {
        val cardA = deckA.removeFirst()
        val cardB = deckB.removeFirst()

        if (cardA > cardB)
            deckA += listOf(cardA, cardB)
        else
            deckB += listOf(cardB, cardA)
    }

    return deckA.isEmpty()
}

fun getDeckScore(deck: List<Int>) = deck
    .reversed()
    .withIndex()
    .sumOf { (i, v) -> v * (i + 1) }

// A wins -> false, B wins -> true
fun playRecursive(deckA: ArrayDeque<Int>, deckB: ArrayDeque<Int>): Boolean {
    val playedGames = mutableSetOf<Pair<List<Int>, List<Int>>>()

    while (deckA.isNotEmpty() && deckB.isNotEmpty()) {
        if (Pair(deckA, deckB) in playedGames)
            return false

        playedGames.add(Pair(deckA, deckB))

        val cardA = deckA.removeFirst()
        val cardB = deckB.removeFirst()
        val winner = when {
            cardA <= deckA.size && cardB <= deckB.size ->
                playRecursive(
                    ArrayDeque(deckA.take(cardA)),
                    ArrayDeque(deckB.take(cardB))
                )
            else -> cardB > cardA
        }

        if (winner)
            deckB += listOf(cardB, cardA)
        else
            deckA += listOf(cardA, cardB)
    }

    return deckA.isEmpty()
}

fun part1(input: List<String>): Int {
    val (deckA, deckB) = parseInput(input)

    return getDeckScore(if (play(deckA, deckB)) deckB else deckA)
}

fun part2(input: List<String>): Int {
    val (deckA, deckB) = parseInput(input)

    return getDeckScore(if (playRecursive(deckA, deckB)) deckB else deckA)
}

fun main() {
    expect(part1(readInput("Day22_test")), 306)
    expect(part2(readInput("Day22_test")), 291)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
