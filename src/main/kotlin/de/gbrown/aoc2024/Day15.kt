@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.findValueAt
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day15 {

    fun part1(input: List<String>): Long {
        val tileMap: List<MutableList<NarrowTileType>> = extractTileMap(input)

        var robotPosition = getRobotStartingPosition(input)

        val directionCommands = extractDirectionCommands(input)

        directionCommands.forEach { direction ->
            val didMoveSuccessFully = pushWithNarrowBoxes(robotPosition, direction, tileMap)
            if (didMoveSuccessFully) {
                robotPosition = robotPosition.moved(direction)
            }
        }

        return tileMap
            .findAllPositionsByPredicate { it == NarrowTileType.BOX }
            .sumOf { it.x + it.y * 100L }
    }

    fun part2(input: List<String>): Long = TODO("Must process $input")

    private fun getRobotStartingPosition(input: List<String>) =
        input.to2dCharsListMatrix().findAllPositionsByPredicate { it == '@' }.first()

    private fun extractDirectionCommands(input: List<String>) = input
        .filterNot { it.startsWith('#') || it.isBlank() }
        .map { line ->
            line.map { char ->
                when (char) {
                    '<' -> Direction.LEFT
                    '>' -> Direction.RIGHT
                    '^' -> Direction.UP
                    'v' -> Direction.DOWN
                    else -> error("Unknown direction: $char")
                }
            }
        }.flatten()

    private fun extractTileMap(input: List<String>) = input.filter { it.startsWith('#') }
        .map { line ->
            line.map { char ->
                when (char) {
                    '#' -> NarrowTileType.WALL
                    '.', '@' -> NarrowTileType.EMPTY
                    'O' -> NarrowTileType.BOX
                    else -> throw IllegalArgumentException("Unknown tile type: $char")
                }
            }.toMutableList()
        }

    private fun pushWithNarrowBoxes(
        forceOriginPosition: Position,
        forceDirection: Direction,
        tileMap: List<MutableList<NarrowTileType>>,
    ): Boolean {
        val pushedPosition = forceOriginPosition.moved(forceDirection)
        val nextTile = tileMap.findValueAt(pushedPosition)!!
        val canMoveToNextTile = when (nextTile) {
            NarrowTileType.WALL -> false
            NarrowTileType.BOX -> pushWithNarrowBoxes(pushedPosition, forceDirection, tileMap)
            NarrowTileType.EMPTY -> true
        }
        if (canMoveToNextTile) {
            tileMap[pushedPosition.y][pushedPosition.x] = NarrowTileType.EMPTY
            if (nextTile == NarrowTileType.BOX) {
                val boxTargetPosition = pushedPosition.moved(forceDirection)
                tileMap[boxTargetPosition.y][boxTargetPosition.x] = NarrowTileType.BOX
            }
        }
        return canMoveToNextTile
    }
}

private enum class NarrowTileType {
    WALL, EMPTY, BOX
}

private enum class WideTileType {
    WALL, EMPTY, BOX_LEFT, BOX_RIGHT
}

fun main() {

    val day = 15

    println("\nPart 1:")
    checkOnTestInput(day, 2028L, Day15::part1, suffix = "_small")
    checkOnTestInput(day, 10_092L, Day15::part1, suffix = "_medium")
    solve(day, Day15::part1)

    println("\nPart2:")
    checkOnTestInput(day, 0, Day15::part2)
    solve(day, Day15::part2)
}
