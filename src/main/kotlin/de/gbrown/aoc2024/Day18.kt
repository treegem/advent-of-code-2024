@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve
import java.util.PriorityQueue

object Day18 {

    fun part1(input: List<String>): Int = part1Internal(input, 1024, 70)

    fun part1Test(input: List<String>): Int = part1Internal(input, 12, 6)

    fun part2(input: List<String>): String = part2Internal(input, 1024, 70)

    fun part2Test(input: List<String>): String = part2Internal(input, 12, 6)

    private fun part1Internal(input: List<String>, byteAmount: Int, maxIndex: Int): Int {
        val freePositions = findAllFreePositions(input, byteAmount, maxIndex)

        return calculateMinimumSteps(maxIndex, freePositions)!!
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
        val corruptedPosition = if (calculateMinimumSteps(maxIndex, freePositions - minAddedCorruptions) == null) {
            remainingCorruptedPositions[minCorruptionsToAdd - 1]
        } else {
            remainingCorruptedPositions[minCorruptionsToAdd]
        }
        return "${corruptedPosition.x},${corruptedPosition.y}"
    }

    private fun binarySearchMinimumCorruptionsToAdd(
        remainingCorruptedPositions: MutableList<Position>,
        maxIndex: Int,
        freePositions: MutableList<Position>,
    ): Int {
        var minCorruptionsToAdd = 0
        var maxCorruptionsToAdd = remainingCorruptedPositions.size
        var corruptionsToAdd = maxCorruptionsToAdd / 2
        while (minCorruptionsToAdd < maxCorruptionsToAdd) {
            val addedCorruptions = remainingCorruptedPositions.take(corruptionsToAdd).toSet()
            if (calculateMinimumSteps(maxIndex, freePositions - addedCorruptions) == null) {
                maxCorruptionsToAdd = corruptionsToAdd
            } else {
                minCorruptionsToAdd = corruptionsToAdd
            }
            if (maxCorruptionsToAdd - minCorruptionsToAdd <= 1) break
            corruptionsToAdd = minCorruptionsToAdd + (maxCorruptionsToAdd - minCorruptionsToAdd) / 2
        }
        return minCorruptionsToAdd
    }

    private fun calculateMinimumSteps(
        maxIndex: Int,
        freePositions: List<Position>,
    ): Int? {
        val startPosition = Position(0, 0)
        val endPosition = Position(maxIndex, maxIndex)

        val distances = mutableMapOf<Position, Int>().withDefault { Int.MAX_VALUE }
        distances[startPosition] = 0

        val priorityQueue = PriorityQueue<Pair<Position, Int>>(compareBy { it.second })
        priorityQueue.add(startPosition to 0)

        while (priorityQueue.isNotEmpty()) {
            val (currentPosition, currentDistance) = priorityQueue.poll()
            findAllNextNeighbors(currentPosition, freePositions).forEach { neighborPosition ->
                val totalDistance = currentDistance + 1
                if (totalDistance < distances.getValue(neighborPosition)) {
                    distances[neighborPosition] = totalDistance
                    priorityQueue.add(neighborPosition to totalDistance)
                }
            }
            if (currentPosition == endPosition) break
        }
        val result = distances[endPosition]
        return result
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

    private fun findAllNextNeighbors(position: Position, freePositions: List<Position>) =
        Direction.nonDiagonals.map { position.moved(it) }
            .filter { it in freePositions }
}

fun main() {

    val day = 18

    println("\nPart 1:")
    checkOnTestInput(day, 22, Day18::part1Test)
    solve(day, Day18::part1)

    println("\nPart2:")
    checkOnTestInput(day, "6,1", Day18::part2Test)
    solve(day, Day18::part2)
}
