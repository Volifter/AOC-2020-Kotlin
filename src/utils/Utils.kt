package utils

import java.io.File
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("inputs", "$name.txt").readLines()

/**
 * A verbose encapsulation of check()
 */
fun <T> expect(got: T, expected: T) {
    try {
        check(got == expected)
    } catch (exception: IllegalStateException) {
        System.err.println("Assertion failed: expected $expected, got $got")

        throw exception
    }

    println("Assertion passed: $got == $got")
}

/**
 * Calculates base ** exponent % mod
 */
fun powMod(base: Long, exponent: Int, mod: Long): Long {
    var res = 1L
    var b = base
    var exp = exponent

    while (exp > 0) {
        if (exp and 1 != 0)
            res = res * b % mod

        exp = exp shr 1
        b = b * b % mod
    }

    return res % mod
}

data class Coords(val x: Int, val y: Int) {
    val manhattanDistance get() = x.absoluteValue + y.absoluteValue

    operator fun plus(other: Coords) = Coords(x + other.x, y + other.y)

    operator fun minus(other: Coords) = Coords(x - other.x, y - other.y)

    operator fun times(factor: Int) = Coords(x * factor, y * factor)

    operator fun contains(other: Coords) =
        other.x in (0 until x)
            && other.y in (0 until y)

    fun rotatedCW(n: Int): Coords =
        when (Math.floorMod(n, 4)) {
            1 -> Coords(-y, x)
            2 -> Coords(-x, -y)
            3 -> Coords(y, -x)
            else -> this
        }

    fun rotatedCCW(n: Int): Coords = rotatedCW(4 - n)
}

data class Coords3(val x: Int, val y: Int, val z: Int) {
    val manhattanDistance get() =
        x.absoluteValue + y.absoluteValue + z.absoluteValue

    operator fun plus(other: Coords3) =
        Coords3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Coords3) =
        Coords3(x - other.x, y - other.y, z - other.z)

    operator fun times(factor: Int) =
        Coords3(x * factor, y * factor, z * factor)

    operator fun contains(other: Coords3) =
        other.x in (0 until x)
            && other.y in (0 until y)
            && other.z in (0 until z)
}

data class CoordsN(val coords: List<Int>) {
    constructor(vararg coords: Int) : this(coords.toList())

    fun getNeighbors(delta: Int = 1): Sequence<CoordsN> = sequence {
        when (coords.size) {
            1 -> coords.single().let { it - delta..it + delta }.forEach {
                yield(CoordsN(it))
            }
            else -> coords.first().let { it - delta..it + delta }.forEach { d ->
                CoordsN(coords.drop(1)).getNeighbors(delta).forEach {
                    yield(CoordsN(d, *it.coords.toIntArray()))
                }
            }
        }
    }
}
