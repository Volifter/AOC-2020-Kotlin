package day11

import utils.*

class Seat(private val coords: Coords) {
    private lateinit var neighbors: List<Seat>

    var isOccupied = false
    var newIsOccupied = false

    val occupiedNeighborsCount get() = neighbors.count { it.isOccupied }

    fun setNearNeighbors(seats: Map<Coords, Seat>) {
        neighbors = (-1..1).flatMap { x ->
            (-1..1 step if (x == 0) 2 else 1).mapNotNull { y ->
                seats[coords + Coords(x, y)]
            }
        }
    }

    fun setVisibleNeighbors(seats: Map<Coords, Seat>, size: Coords) {
        neighbors = (-1..1).flatMap { x ->
            (-1..1 step if (x == 0) 2 else 1).mapNotNull { y ->
                val dir = Coords(x, y)
                var pos = coords + dir

                while (pos in size) {
                    seats[pos]?.let { return@mapNotNull it }
                    pos += dir
                }
                null
            }
        }
    }
}

fun getSeats(input: List<String>): Map<Coords, Seat> {
    val result = mutableMapOf<Coords, Seat>()

    input.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == 'L')
                result[Coords(x, y)] = Seat(Coords(x, y))
        }
    }

    return result
}

fun solve(seats: List<Seat>, minNeighbors: Int): Int {
    var hasMoved = true

    while (hasMoved) {
        hasMoved = false

        seats
            .onEach { seat ->
                val occupiedNeighborsCount = seat.occupiedNeighborsCount
                val willSwitch = !seat.isOccupied && occupiedNeighborsCount == 0
                    || seat.isOccupied && occupiedNeighborsCount >= minNeighbors

                hasMoved = hasMoved || willSwitch

                seat.newIsOccupied = seat.isOccupied xor willSwitch
            }
            .forEach { seat -> seat.isOccupied = seat.newIsOccupied }
    }

    return seats.count { it.isOccupied }
}

fun part1(input: List<String>): Int =
    solve(
        getSeats(input).let { seatsMap ->
            seatsMap.values.onEach { it.setNearNeighbors(seatsMap) }.toList()
        },
        4
    )

fun part2(input: List<String>): Int =
    solve(
        getSeats(input).let { seatsMap ->
            seatsMap.values
                .onEach {
                    it.setVisibleNeighbors(
                        seatsMap,
                        Coords(input.first().length, input.size)
                    )
                }
                .toList()
        },
        5
    )

fun main() {
    val testInput = readInput("Day11_test")
    expect(part1(testInput), 37)
    expect(part2(testInput), 26)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
