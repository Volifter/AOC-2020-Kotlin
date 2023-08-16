package day17

import utils.*

fun parseInput(lines: List<String>): Set<Coords> =
    lines.indices
        .flatMap { y -> lines.first().indices.map { x -> Coords(x, y) } }
        .filter { (x, y) -> lines[y][x] == '#' }
        .toSet()

fun solve(squares: Set<Coords>, dimension: Int, steps: Int = 6): Int {
    var cubes = squares
        .map { (x, y) -> CoordsN(x, y, *IntArray(dimension - 2) { 0 }) }
        .toMutableSet()

    repeat(steps) {
        val newCubes = mutableSetOf<CoordsN>()
        val candidates = mutableMapOf<CoordsN, Int>()

        cubes.forEach { cube ->
            var neighborsCount = 0

            cube.getNeighbors().filter { it != cube }.forEach { neighbor ->
                candidates[neighbor] = candidates.getOrDefault(neighbor, 0) + 1

                if (neighbor in cubes)
                    neighborsCount++
            }

            if (neighborsCount in 2..3)
                newCubes.add(cube)
        }

        newCubes.addAll(
            candidates.entries.mapNotNull { (coords, count) ->
                coords.takeIf { count == 3 }
            }
        )

        cubes = newCubes
    }

    return cubes.size
}

fun part1(input: List<String>): Int {
    return solve(parseInput(input), 3)
}

fun part2(input: List<String>): Int {
    return solve(parseInput(input), 4)
}

fun main() {
    expect(part1(readInput("Day17_test")), 112)
    expect(part2(readInput("Day17_test")), 848)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
