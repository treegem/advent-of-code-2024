@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.findValueInDirection
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day10 {

    private val relevantDirections = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)

    fun part1(input: List<String>): Int {
        val intMatrix = input.to2dIntMatrix()

        val zeroPositions = intMatrix.findAllPositionsByPredicate { it == 0 }

        return zeroPositions
            .map { findAllTrailEnds(it, intMatrix).distinct() }
            .sumOf { it.count() }
    }

    fun part2(input: List<String>): Int {
        val intMatrix = input.to2dIntMatrix()

        val zeroPositions = intMatrix.findAllPositionsByPredicate { it == 0 }

        return zeroPositions
            .map { findAllTrailEnds(it, intMatrix) }
            .sumOf { it.count() }
    }

    private fun List<String>.to2dIntMatrix(): List<List<Int>> {
        val intMatrix = to2dCharsListMatrix()
            .map { line -> line.map { it.toString().toInt() } }
        return intMatrix
    }

    private fun findAllTrailEnds(
        zeroPosition: Position,
        intMatrix: List<List<Int>>,
    ): MutableList<Position> {
        var currentHeightPositions = mutableListOf(zeroPosition)
        (1..9).forEach { height ->
            val newHeightPositions = findNeighboringHeightPositions(currentHeightPositions, intMatrix, height)
            currentHeightPositions = newHeightPositions.toMutableList()
        }
        return currentHeightPositions
    }

    private fun findNeighboringHeightPositions(
        currentHeightPositions: MutableList<Position>,
        intMatrix: List<List<Int>>,
        height: Int,
    ): MutableList<Position> {
        val newHeightPositions = mutableListOf<Position>()
        currentHeightPositions.forEach { position ->
            relevantDirections.forEach { direction ->
                if (intMatrix.findValueInDirection(position, direction) == height) {
                    newHeightPositions.add(position.moved(direction))
                }
            }
        }
        return newHeightPositions
    }
}

fun main() {

    val day = 10

    println("\nPart 1:")
    checkOnTestInput(day, 36, Day10::part1)
    solve(day, Day10::part1)

    println("\nPart2:")
    checkOnTestInput(day, 81, Day10::part2)
    solve(day, Day10::part2)
}
