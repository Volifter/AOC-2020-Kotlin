package day04

import utils.*

val MANDATORY_KEYS = mapOf<String, (String) -> Boolean>(
    "byr" to { line ->
        line.toIntOrNull()?.let { it in 1920..2002 } == true
    },
    "iyr" to { line ->
        line.toIntOrNull()?.let { it in 2010..2020 } == true
    },
    "eyr" to { line ->
        line.toIntOrNull()?.let { it in 2020..2030 } == true
    },
    "hgt" to { line ->
        line
            .dropLast(2)
            .toIntOrNull()
            ?.takeIf { line.takeLast(2) in listOf("cm", "in") }
            ?.let {
                it in if (line.takeLast(2) == "cm") 150..193 else 59..76
            } == true
    },
    "hcl" to { line ->
        line
            .takeIf { it.first() == '#' }
            ?.drop(1)
            ?.takeIf { it.length == 6 }
            ?.toIntOrNull(16) != null
    },
    "ecl" to { line ->
        line in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    },
    "pid" to { line ->
        line
            .takeIf { it.length == 9 }
            ?.toIntOrNull() != null
    },
)

fun getPassports(lines: List<String>): List<Map<String, String>> =
    lines.fold(listOf("")) { acc, line ->
        if (line.isEmpty())
            acc + listOf("")
        else
            acc.dropLast(1) + (acc.last() + " " + line)
    }.map { line ->
        line
            .trim()
            .split(" ")
            .associate { it.split(":").zipWithNext().single() }
    }

fun part1(input: List<String>): Int =
    getPassports(input).count { passport ->
        MANDATORY_KEYS.all { it.key in passport.keys }
    }

fun part2(input: List<String>): Int =
    getPassports(input).count { passport ->
        MANDATORY_KEYS.all { (name, validator) ->
            passport[name]?.let { validator(it) } == true
        }
    }


fun main() {
    val testInput = readInput("Day04_test")
    expect(part1(testInput), 2)
    expect(part2(testInput), 2)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
