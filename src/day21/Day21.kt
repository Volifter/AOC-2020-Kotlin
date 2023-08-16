package day21

import utils.*

val LINE_REGEX = """^([a-zA-Z ]+) \(contains ([a-zA-Z, ]+)\)$""".toRegex()

class Allergen(val name: String) {
    var possibilities: Set<String> = setOf()

    override fun toString(): String = "Allergen($name: $possibilities)"
}

fun parseInput(input: List<String>): List<Pair<Set<String>, List<Allergen>>> {
    val allergens = mutableMapOf<String, Allergen>()

    return input.map { line ->
        val (_, ingredientNames, allergenNames) =
            LINE_REGEX.find(line)?.groupValues
                ?: throw RuntimeException("invalid line format: $line")

        Pair(
            ingredientNames.split(" ").toSet(),
            allergenNames.split(", ").map { name ->
                allergens.getOrPut(name) { Allergen(name) }
            }
        )
    }
}

fun solve(
    constraints: List<Pair<Set<String>, List<Allergen>>>
): Map<String, Allergen> {
    val allIngredients = constraints.flatMap { it.first }.toSet()
    val allAllergens = constraints.flatMap { it.second }.toSet()

    allAllergens.forEach { allergen ->
        allergen.possibilities = allIngredients
    }

    constraints.forEach { (ingredients, allergens) ->
        allergens.forEach { allergen ->
            allergen.possibilities = allergen.possibilities.intersect(
                ingredients
            )
        }
    }

    val multipleAllergens = allAllergens.toMutableSet()

    generateSequence {
        val single = multipleAllergens.find { it.possibilities.size == 1 }
            ?: return@generateSequence null

        multipleAllergens -= single

        val ingredient = single.possibilities.single()

        multipleAllergens.forEach { it.possibilities -= ingredient }

        true
    }.count()

    return allAllergens
        .filter { it.possibilities.size == 1 }
        .associateBy { allergen -> allergen.possibilities.single() }
}

fun part1(input: List<String>): Int {
    val constraints = parseInput(input)
    val result = solve(constraints)

    return constraints.sumOf { (ingredients, _) ->
        ingredients.count { it !in result }
    }
}

fun part2(input: List<String>): String {
    val constraints = parseInput(input)
    val result = solve(constraints)

    return result.entries
        .sortedBy { it.value.name }
        .joinToString(",") { it.key }
}

fun main() {
    expect(part1(readInput("Day21_test")), 5)
    expect(part2(readInput("Day21_test")), "mxmxvkd,sqjhc,fvjkl")

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
