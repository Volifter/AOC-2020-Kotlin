package day14

import utils.*

open class Instruction(private val args: LongArray) {
    operator fun component1(): Long = args[0]

    operator fun component2(): Long = args[1]
}

class MaskInstruction(vararg args: Long) : Instruction(args)

class MemoryInstruction(vararg args: Long) : Instruction(args)

fun getInstruction(line: String): Instruction {
    val parts = line.split(" = ")

    if (parts.first() == "mask") {
        val maskStr = parts.last()
        val maskAnd = maskStr.fold(0L) { acc, c ->
            acc shl 1 or if (c == 'X') 1L else 0L
        }
        val maskOr = maskStr.fold(0L) { acc, c ->
            acc shl 1 or if (c == 'X') 0L else c.digitToInt().toLong()
        }

        return MaskInstruction(maskAnd, maskOr)
    }

    return MemoryInstruction(
        parts.first().let { it.slice(4 until it.lastIndex) }.toLong(),
        parts.last().toLong()
    )
}

fun solve(
    input: List<String>,
    memorySetter: (
        MutableMap<Long, Long>,
        MaskInstruction,
        MemoryInstruction
    ) -> Unit
): Long {
    var maskInstruction = MaskInstruction(0xfffffffff, 0)
    val memory = mutableMapOf<Long, Long>()

    input
        .asSequence()
        .map { getInstruction(it) }
        .forEach { instruction ->
            when (instruction) {
                is MaskInstruction -> maskInstruction = instruction
                is MemoryInstruction -> memorySetter(
                    memory,
                    maskInstruction,
                    instruction
                )
            }
        }

    return memory.values.sum()
}

fun part1(input: List<String>): Long =
    solve(input) { memory, (maskAnd, maskOr), (address, value) ->
        memory[address] = value and maskAnd or maskOr
    }

fun getAddressesByMask(
    address: Long,
    maskInstruction: MaskInstruction
): List<Long> {
    val (maskAnd, maskOr) = maskInstruction
    val floatingPositions = (0 until 36).filter {
        (maskAnd shr it) and 1L != 0L
    }

    return (0 until (1 shl floatingPositions.size)).map { n ->
        var value = address and (maskAnd xor 0xfffffffffL) or maskOr

        floatingPositions.forEachIndexed { i, pos ->
            if ((n shr i) and 1 != 0)
                value = value or (1L shl pos)
        }

        value
    }
}

fun part2(input: List<String>): Long =
    solve(input) { memory, maskInstruction, (rootAddress, value) ->
        getAddressesByMask(rootAddress, maskInstruction).forEach { address ->
            memory[address] = value
        }
    }

fun main() {
    expect(part1(readInput("Day14_test_a")), 165)
    expect(part2(readInput("Day14_test_b")), 208)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
