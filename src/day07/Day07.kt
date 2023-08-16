package day07

import utils.*

class Bag {
    private val nestedBags: MutableMap<String, Boolean> = mutableMapOf()

    var children: MutableMap<String, Pair<Bag, Int>> = mutableMapOf()

    fun containsType(type: String): Boolean =
        nestedBags.getOrPut(type) {
            type in children ||
                children.values.any { (bag, _) -> bag.containsType(type) }
        }

    fun countAllNestedBags(): Int =
        children.values.sumOf { (bag, count) ->
            (bag.countAllNestedBags() + 1) * count
        }
}

fun getBags(input: List<String>): Map<String, Bag> {
    val bags: MutableMap<String, Bag> = mutableMapOf()

    input.forEach { line ->
        val (type, contents) = line.split(" bags contain ")
        val bag = bags.getOrPut(type) { Bag() }

        if (contents == "no other bags.")
            return@forEach

        bag.children = contents.split(", ").associate { description ->
            val parts = description.split(" ")
            val childCount = parts.first().toInt()
            val childType = parts
                .slice(1 until parts.lastIndex)
                .joinToString(" ")

            childType to Pair(
                bags.getOrPut(childType) { Bag() },
                childCount
            )
        }.toMutableMap()
    }

    return bags
}

fun part1(input: List<String>): Int =
    getBags(input).values.count { it.containsType("shiny gold") }

fun part2(input: List<String>): Int =
    getBags(input)["shiny gold"]!!.countAllNestedBags()

fun main() {
    expect(part1(readInput("Day07_test_a")), 4)
    expect(part2(readInput("Day07_test_b")), 126)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
