package day12

import utils.*

val DIRECTIONS = listOf(
    Coords(1, 0),
    Coords(0, 1),
    Coords(-1, 0),
    Coords(0, -1)
)

fun getInstructions(input: List<String>) : List<Pair<Char, Int>> =
    input.map { Pair(it.first(), it.drop(1).toInt()) }

fun part1(input: List<String>): Int {
    var position = Coords(0, 0)
    var directionIdx = 0

    getInstructions(input).forEach { (instruction, value) ->
        when (instruction) {
            'N' -> position += Coords(0, -value)
            'S' -> position += Coords(0, value)
            'E' -> position += Coords(value, 0)
            'W' -> position += Coords(-value, 0)
            'F' -> position += DIRECTIONS[directionIdx] * value
            'R' -> directionIdx = (directionIdx + value / 90) % 4
            'L' -> directionIdx = Math.floorMod(directionIdx - value / 90, 4)
        }
    }

    return position.manhattanDistance
}

fun part2(input: List<String>): Int {
    var shipPos = Coords(0, 0)
    var waypointPos = Coords(10, -1)

    getInstructions(input).forEach { (instruction, value) ->
        when (instruction) {
            'N' -> waypointPos += Coords(0, -value)
            'S' -> waypointPos += Coords(0, value)
            'E' -> waypointPos += Coords(value, 0)
            'W' -> waypointPos += Coords(-value, 0)
            'F' -> shipPos += waypointPos * value
            'L' -> waypointPos = waypointPos.rotatedCCW(value / 90)
            'R' -> waypointPos = waypointPos.rotatedCW(value / 90)
        }
    }

    return shipPos.manhattanDistance
}

fun main() {
    val testInput = readInput("Day12_test")
    expect(part1(testInput), 25)
    expect(part2(testInput), 286)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
