package day16

import utils.*

fun parseInput(
    input: List<String>
): Pair<List<Pair<String, Pair<IntRange, IntRange>>>, List<List<Int>>> {
    val separatingIdx = input.indexOf("")
    val ranges = input
        .slice(0 until separatingIdx)
        .map { line ->
            val (name, values) = line.split(": ")

            values.split(" or ").map { str ->
                val (from, to) = str.split('-').map(String::toInt)

                (from..to)
            }.let { Pair(name, Pair(it[0], it[1])) }
        }
    val tickets = input
        .slice(separatingIdx + 1..input.lastIndex)
        .mapNotNull { line ->
            if (',' !in line)
                null
            else
                line.split(',').map(String::toInt)
        }

    return Pair(ranges, tickets)
}

fun part1(input: List<String>): Int {
    val (ranges, tickets) = parseInput(input)

    return tickets.drop(1).sumOf { values ->
        values.sumOf { value ->
            value.takeUnless {
                ranges.any { (_, ranges) ->
                    val (rangeA, rangeB) = ranges

                    value in rangeA || value in rangeB
                }
            } ?: 0
        }
    }
}

fun reducePossibilities(
    possibilities: List<Set<String>>,
    used: Set<String> = setOf(),
    i: Int = 0
): List<Set<String>>? {
    if (i >= possibilities.size)
        return possibilities

    var newPossibilities = possibilities
    val newUsed = used.toMutableSet()
    var single = newPossibilities.find {
        it.size == 1 && it.single() !in newUsed
    }

    while (single != null) {
        newUsed += single
        newPossibilities = newPossibilities.map {
            if (it == single)
                it
            else
                (it - single!!).also { new ->
                    if (new.isEmpty())
                        return null
                }
        }
        single = newPossibilities.find {
            it.size == 1 && it.single() !in newUsed
        }
    }

    val possible = newPossibilities[i] - used

    if (possible.isEmpty())
        return null

    possible.forEach { name ->
        reducePossibilities(
            List(newPossibilities.size) { j ->
                if (i == j) setOf(name) else newPossibilities[j]
            },
            used + setOf(name),
            i + 1
        )?.let { return it }
    }

    return null
}

fun part2(input: List<String>, fieldPrefix: String): Long {
    val (ranges, tickets) = parseInput(input)
    val validTickets = tickets
        .filter { values ->
            values.all { value ->
                ranges.any { (_, ranges) ->
                    val (rangeA, rangeB) = ranges

                    value in rangeA || value in rangeB
                }
            }
        }
    val possibilities = validTickets.first().indices.map { i ->
        ranges.mapNotNull { (name, ranges) ->
            val (rangeA, rangeB) = ranges

            name.takeIf {
                validTickets.all { ticket ->
                    ticket[i].let { it in rangeA || it in rangeB }
                }
            }
        }.toSet()
    }

    val names = reducePossibilities(possibilities)!!.map(Set<String>::single)

    return names.zip(tickets.first()).mapNotNull { (name, value) ->
        value
            .takeIf { name.startsWith(fieldPrefix) }
            ?.toInt()
    }.fold(1L, Long::times)
}

fun main() {
    expect(part1(readInput("Day16_test_a")), 71)
    expect(part2(readInput("Day16_test_b"), "seat"), 13)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input, "departure"))
}
