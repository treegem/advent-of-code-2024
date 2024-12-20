@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Dijkstra
import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day18 {

    private val dijkstra = Day18Dijkstra()

    fun part1(input: List<String>): Long = part1Internal(input, 1024, 70)

    fun part1Test(input: List<String>): Long = part1Internal(input, 12, 6)

    fun part2(input: List<String>): String = part2Internal(input, 1024, 70)

    fun part2Test(input: List<String>): String = part2Internal(input, 12, 6)

    private fun part1Internal(input: List<String>, byteAmount: Int, maxIndex: Int): Long {
        val freePositions = findAllFreePositions(input, byteAmount, maxIndex)

        return dijkstra.calculateShortestDistance(freePositions, Position(0, 0), Position(maxIndex, maxIndex))!!
    }

    private fun part2Internal(input: List<String>, safeByteAmount: Int, maxIndex: Int): String {
        val freePositions = findAllFreePositions(input, safeByteAmount, maxIndex).toMutableList()
        val remainingCorruptedPositions = input
            .map { it.split(",") }
            .map { coordinateStrings -> coordinateStrings.map { it.toInt() } }
            .map { (x, y) -> Position(x, y) }
            .takeLast(input.size - safeByteAmount)
            .toMutableList()

        val minCorruptionsToAdd =
            binarySearchMinimumCorruptionsToAdd(remainingCorruptedPositions, maxIndex, freePositions)
        val minAddedCorruptions = remainingCorruptedPositions.take(minCorruptionsToAdd).toSet()
        val start = Position(0, 0)
        val end = Position(maxIndex, maxIndex)
        val breakingCorruptedPosition =
            if (dijkstra.calculateShortestDistance(freePositions - minAddedCorruptions, start, end) == null) {
                remainingCorruptedPositions[minCorruptionsToAdd - 1]
            } else {
                remainingCorruptedPositions[minCorruptionsToAdd]
            }
        return "${breakingCorruptedPosition.x},${breakingCorruptedPosition.y}"
    }

    private fun binarySearchMinimumCorruptionsToAdd(
        remainingCorruptedPositions: MutableList<Position>,
        maxIndex: Int,
        freePositions: MutableList<Position>,
    ): Int {
        var minCorruptionsToAdd = 0
        var maxCorruptionsToAdd = remainingCorruptedPositions.size
        var corruptionsToAdd = maxCorruptionsToAdd / 2
        val start = Position(0, 0)
        val end = Position(maxIndex, maxIndex)
        while (minCorruptionsToAdd < maxCorruptionsToAdd) {
            val addedCorruptions = remainingCorruptedPositions.take(corruptionsToAdd).toSet()
            if (dijkstra.calculateShortestDistance(freePositions - addedCorruptions, start, end) == null) {
                maxCorruptionsToAdd = corruptionsToAdd
            } else {
                minCorruptionsToAdd = corruptionsToAdd
            }
            if (maxCorruptionsToAdd - minCorruptionsToAdd <= 1) break
            corruptionsToAdd = minCorruptionsToAdd + (maxCorruptionsToAdd - minCorruptionsToAdd) / 2
        }
        return minCorruptionsToAdd
    }

    private fun findAllFreePositions(
        input: List<String>,
        byteAmount: Int,
        maxIndex: Int,
    ): List<Position> {
        val corruptedPositions = input.asSequence()
            .map { it.split(",") }
            .map { coordinateStrings -> coordinateStrings.map { it.toInt() } }
            .map { (x, y) -> Position(x, y) }
            .take(byteAmount)
            .toSet()
        val allGridPositions = (0..maxIndex).map { x ->
            (0..maxIndex).map { y ->
                Position(x, y)
            }
        }.flatten()
        val freePositions = allGridPositions - corruptedPositions
        return freePositions
    }
}

private class Day18Dijkstra : Dijkstra<Position>() {

    override fun distance(from: Position, to: Position): Long = 1

    override fun findNextNodes(currentNode: Position, nodesBetween: List<Position>): List<Position> =
        Direction.nonDiagonals.map { currentNode.moved(it) }
            .filter { it in nodesBetween }
}

fun main() {

    val day = 18

    println("\nPart 1:")
    checkOnTestInput(day, 22L, Day18::part1Test)
    solve(day, Day18::part1)

    println("\nPart2:")
    checkOnTestInput(day, "6,1", Day18::part2Test)
    solve(day, Day18::part2)
}
