package day24

import utils.*

val DIRECTIONS = listOf(
    Pair("e", Coords3(0, 1, 1)),
    Pair("ne", Coords3(1, 1, 0)),
    Pair("se", Coords3(-1, 0, 1)),
    Pair("w", Coords3(0, -1, -1)),
    Pair("nw", Coords3(1, 0, -1)),
    Pair("sw", Coords3(-1, -1, 0)),
)

fun parseDirections(fullLine: String) = sequence {
    var line = fullLine

    while (line.isNotEmpty()) {
        val (dir, offset) = DIRECTIONS.find { line.startsWith(it.first) }!!

        yield(offset)
        line = line.drop(dir.length)
    }
}

fun parseInput(input: List<String>): List<List<Coords3>> =
    input.map { line -> parseDirections(line).toList() }

fun getFlippedTiles(coordinates: List<Coords3>): Set<Coords3> {
    val flipped = mutableSetOf<Coords3>()

    coordinates.forEach { coords ->
        if (coords in flipped)
            flipped.remove(coords)
        else
            flipped.add(coords)
    }

    return flipped
}

fun part1(input: List<String>): Int {
    val offsets = parseInput(input)
    val coordinates = offsets.map { it.reduce(Coords3::plus) }

    return getFlippedTiles(coordinates).size
}

fun part2(input: List<String>, count: Int = 100): Int {
    val offsets = parseInput(input)
    val coordinates = offsets.map { it.reduce(Coords3::plus) }
    var black = getFlippedTiles(coordinates)
    val neighborsCounts = mutableMapOf<Coords3, Int>()

    repeat(count) {
        black.forEach { coords ->
            DIRECTIONS.map { coords + it.second }.forEach { neighbor ->
                neighborsCounts[neighbor] = (neighborsCounts[neighbor] ?: 0) + 1
            }
        }

        black = (neighborsCounts.keys.toSet() + black).filter {
            when (it in black) {
                true -> neighborsCounts[it] in (1..2)
                false -> neighborsCounts[it] == 2
            }
        }.toSet()

        neighborsCounts.clear()
    }

    return black.size
}

fun main() {
    expect(part1(readInput("Day24_test")), 10)
    expect(part2(readInput("Day24_test")), 2208)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
