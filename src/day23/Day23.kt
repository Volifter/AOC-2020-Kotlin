package day23

import utils.*

data class Cup(val n: Int) {
    var next: Cup = this
    var prev: Cup = this

    fun getNext(n: Int): Sequence<Cup> = generateSequence(this.next) { it.next }
        .take(n)
}

fun parseInput(input: List<Int>): Map<Int, Cup> {
    var prevCup: Cup? = null
    val cups = mutableMapOf<Int, Cup>()

    input.forEach { n ->
        val cup = Cup(n)

        cups[n] = cup

        prevCup?.let {
            cup.prev = it
            cup.next = it.next
            it.next.prev = cup
            it.next = cup
        }

        prevCup = cup
    }

    return cups
}

fun shuffleCups(cups: Map<Int, Cup>, count: Int = 100) {
    var head = cups.values.first()

    repeat(count) {
        val batch = head.getNext(3).toList()
        val nextHead = batch.last().next

        head.next = nextHead
        nextHead.prev = head

        val targetN = generateSequence(Math.floorMod(head.n - 2, cups.size)) {
            Math.floorMod(it - 1, cups.size)
        }.first { batch.all { cup -> cup.n != it + 1 } } + 1
        val target = cups[targetN]!!
        val targetNext = target.next

        target.next = batch.first()
        batch.first().prev = target
        batch.last().next = targetNext
        targetNext.prev = batch.last()

        head = nextHead
    }
}

fun part1(input: String, count: Int = 100): String {
    val cups = parseInput(input.map(Char::digitToInt))

    shuffleCups(cups, count)

    return cups[1]!!.getNext(cups.size - 1)
        .joinToString("") { it.n.toString() }
}

fun part2(input: String, count: Int = 10000000): Long {
    val ints = input.map(Char::digitToInt)
    val cups = parseInput(ints + (ints.max() + 1..1000000))

    shuffleCups(cups, count)

    return cups[1]!!.getNext(2).map { it.n }.fold(1L, Long::times)
}

fun main() {
    expect(part1(readInput("Day23_test")[0], 10), "92658374")
    expect(part1(readInput("Day23_test")[0]), "67384529")
    expect(part2(readInput("Day23_test")[0], 10000000), 149245887792L)

    val input = readInput("Day23")[0]
    println(part1(input))
    println(part2(input))
}
