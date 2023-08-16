package day20

import utils.*
import kotlin.math.sqrt

val TITLE_REGEX = """^Tile (\d+):$""".toRegex()

enum class Side(val i: Int) {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3)
}

class Tile(val id: Int, lines: List<String>) {
    val size: Int = lines.first().length
    val transpositionImages: List<List<List<Char>>> =
        getTranspositions(lines.map(String::toList)).toList()
    val transpositionSides: List<List<Int>> = transpositionImages.map { rows ->
        listOf(
            rows.first().toList(),
            rows.map { it.last() },
            rows.last().toList(),
            rows.map { it.first() }
        ).map(::parseTileSide)
    }

    override fun toString(): String = "Tile($id)"
}

class PlacedTile(val tile: Tile, val transpositionIdx: Int) {
    val transpositionSide get() = tile.transpositionSides[transpositionIdx]
    val transpositionImage get() = tile.transpositionImages[transpositionIdx]

    override fun toString(): String =
        "PlacedTile(${tile.id}, $transpositionIdx)"

    override fun equals(other: Any?): Boolean = (
        other is PlacedTile
            && other.tile.id == tile.id
            && other.transpositionIdx == transpositionIdx
    )

    override fun hashCode(): Int = (tile.id shl 5) or transpositionIdx
}

fun <T> getTranspositions(matrix: List<List<T>>) = sequence {
    var transposition = matrix.map(List<T>::toList)

    repeat(4) {
        yield(transposition)

        // Flipped
        yield(transposition.map(List<T>::reversed))

        // Clockwise rotation
        transposition = matrix.indices.map { y ->
            matrix.indices.map { x ->
                transposition[transposition.lastIndex - x][y]
            }
        }
    }
}

fun parseTileSide(chars: List<Char>) = chars.fold(0) { acc, c ->
    acc shl 1 or ".#".indexOf(c)
}

fun parseInput(lines: List<String>): Set<Tile> {
    var currentTileLines = mutableListOf<String>()
    var currentTileId = 0
    val tiles = mutableMapOf<Int, Tile>()

    (lines + "").forEach { line ->
        if (line.isEmpty()) {
            tiles[currentTileId] = Tile(currentTileId, currentTileLines)

            return@forEach
        }

        TITLE_REGEX.find(line)?.groups?.get(1)?.value?.let {
            currentTileId = it.toInt()
            currentTileLines = mutableListOf()
        } ?: run {
            currentTileLines.add(line)
        }
    }

    return tiles.values.toSet()
}

fun getImageSize(tilesSize: Int) = sqrt(tilesSize * 1.0).let {
    if (it % 1 != 0.0)
        throw RuntimeException("number of tiles must be a square")

    it.toInt()
}

fun buildIndex(tiles: Set<Tile>): List<MutableMap<Int, Set<PlacedTile>>> {
    val index = List(4) { mutableMapOf<Int, Set<PlacedTile>>() }

    tiles.forEach { tile ->
        tile.transpositionSides.forEachIndexed { i, transposition ->
            transposition.forEachIndexed { j, n ->
                index[j][n] = (
                    index[j].getOrDefault(n, setOf())
                        + setOf(PlacedTile(tile, i))
                )
            }
        }
    }

    return index
}

fun solve(
    remaining: Set<Tile>,
    index: List<MutableMap<Int, Set<PlacedTile>>>,
    size: Int,
    placed: Map<Coords, PlacedTile> = mapOf(),
    coords: Coords = Coords(0, 0)
): Sequence<Map<Coords, PlacedTile>> = sequence {
    val nextCoords = when {
        coords.y == size - 1 -> Coords(coords.y, coords.x + 1)
        coords.x == 0 -> Coords(coords.y + 1, 0)
        else -> coords + Coords(-1, 1)
    }

    if (nextCoords.y > size)
        return@sequence yield(placed)

    val constraints: MutableList<Set<PlacedTile>> = mutableListOf()

    index[Side.LEFT.i][placed[coords - Coords(1, 0)]
        ?.transpositionSide?.get(Side.RIGHT.i)]
        ?.let { constraints.add(it) }
    index[Side.TOP.i][placed[coords - Coords(0, 1)]
        ?.transpositionSide?.get(Side.BOTTOM.i)]
        ?.let { constraints.add(it) }

    (
        constraints
            .takeIf { it.isNotEmpty() }
            ?: listOf(
                remaining.flatMap { tile ->
                    (0..<8).map { i ->
                        PlacedTile(
                            tile,
                            i
                        )
                    }
                }.toSet()
            )
    )
        .reduce(Set<PlacedTile>::intersect)
        .filter { it.tile in remaining }
        .forEach { placedTile ->
            yieldAll(
                solve(
                    remaining - placedTile.tile,
                    index,
                    size,
                    placed + mapOf(coords to placedTile),
                    nextCoords
                )
            )
        }
}

val seaMonsterCoords: List<Coords> = sequence {
    listOf(
        "                  # ",
        "#    ##    ##    ###",
        " #  #  #  #  #  #   "
    ).mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == '#')
                yield(Coords(x, y))
        }
    }
}.toList()

val seaMonsterSize = Coords(
    seaMonsterCoords.maxOf { it.x } + 1,
    seaMonsterCoords.maxOf { it.y } + 1
)

fun findSeaMonsters(image: List<List<Char>>): Int {
    val count = (0..image.size - seaMonsterSize.y).sumOf { y ->
        (0..image.size - seaMonsterSize.x).count { x ->
            seaMonsterCoords.all { (monsterX, monsterY) ->
                image[y + monsterY][x + monsterX] == '#'
            }
        }
    }

    return when (count) {
        0 -> 0
        else -> (
            image.sumOf { line -> line.count { it == '#' } }
                - seaMonsterCoords.size * count
        )
    }
}

fun part1(input: List<String>): Long {
    val tiles = parseInput(input)
    val size = getImageSize(tiles.size)
    val positions: Map<Coords, PlacedTile> = solve(
        tiles, buildIndex(tiles), size
    ).first()

    return (
        1L
        * positions[Coords(0, 0)]!!.tile.id
        * positions[Coords(size - 1, 0)]!!.tile.id
        * positions[Coords(0, size - 1)]!!.tile.id
        * positions[Coords(size - 1, size - 1)]!!.tile.id
    )
}

fun part2(input: List<String>): Int {
    val tiles = parseInput(input)
    val size = getImageSize(tiles.size)
    val imgSize = tiles.first().transpositionImages.first().size
    val positions: Map<Coords, PlacedTile> = solve(
        tiles, buildIndex(tiles), size
    ).first()
    val image = sequence {
        (0..<size).map { y ->
            (1..<imgSize - 1).map { i ->
                yield(
                    (0..<size).flatMap { x ->
                        positions[Coords(x, y)]!!.transpositionImage[i]
                            .slice(1..<imgSize - 1)
                    }
                )
            }
        }
    }.toList()

    return getTranspositions(image)
        .map { findSeaMonsters(it) }
        .find { it != 0 }
        ?: 0
}

fun main() {
    expect(part1(readInput("Day20_test")), 20899048083289)
    expect(part2(readInput("Day20_test")), 273)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
