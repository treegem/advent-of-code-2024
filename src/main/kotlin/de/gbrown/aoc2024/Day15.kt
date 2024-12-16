@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.Day15.ROBOT
import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.findValueAt
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day15 {

    const val WALL = '#'
    const val EMPTY = '.'
    const val ROBOT = '@'
    const val BOX_NARROW = 'O'
    const val BOX_LEFT = '['
    const val BOX_RIGHT = ']'

    fun part1(input: List<String>): Long {
        val tileMap: List<MutableList<NarrowTileType>> = input.toNarrowTileMap()

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

    fun part2(input: List<String>): Long {
//        val widenedInput = input
        val widenedInput = input.map { it.widenTiles() }
        val tileMap: List<MutableList<WideTileType>> = widenedInput.toWideTileMap()

        var robotPosition = getRobotStartingPosition(widenedInput)

        val directionCommands = extractDirectionCommands(input)

        directionCommands.forEach { direction ->
            val didMoveSuccessFully = pushWithWideBoxes(robotPosition, direction, tileMap)
            if (didMoveSuccessFully) {
                robotPosition = robotPosition.moved(direction)
            }
            tileMap.printWithRobot(robotPosition)
        }

        return tileMap
            .findAllPositionsByPredicate { it == WideTileType.BOX_LEFT }
            .sumOf { it.x + it.y * 100L }
    }

    private fun getRobotStartingPosition(input: List<String>) =
        input.to2dCharsListMatrix().findAllPositionsByPredicate { it == ROBOT }.first()

    private fun extractDirectionCommands(input: List<String>) = input
        .filterNot { it.startsWith(WALL) || it.isBlank() }
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

    private fun List<String>.toNarrowTileMap() = filter { it.startsWith(WALL) }
        .map { line ->
            line.map { char ->
                when (char) {
                    WALL -> NarrowTileType.WALL
                    EMPTY, ROBOT -> NarrowTileType.EMPTY
                    BOX_NARROW -> NarrowTileType.BOX
                    else -> throw IllegalArgumentException("Unknown tile type: $char")
                }
            }.toMutableList()
        }

    private fun List<String>.toWideTileMap() = filter { it.startsWith(WALL) }
        .map { line ->
            line.map { char ->
                when (char) {
                    WALL -> WideTileType.WALL
                    EMPTY, ROBOT -> WideTileType.EMPTY
                    BOX_LEFT -> WideTileType.BOX_LEFT
                    BOX_RIGHT -> WideTileType.BOX_RIGHT
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

    private fun pushWithWideBoxes(
        forceOriginPosition: Position,
        forceDirection: Direction,
        tileMap: List<MutableList<WideTileType>>,
    ): Boolean {
        val pushedPosition = forceOriginPosition.moved(forceDirection)
        val nextTile = tileMap.findValueAt(pushedPosition)!!
        val canMoveToNextTile = when (nextTile) {
            WideTileType.WALL -> false
            WideTileType.BOX_LEFT -> {
                if (forceDirection.isVertical()) {
                    pushWithWideBoxes(
                        forceOriginPosition = pushedPosition,
                        forceDirection = forceDirection,
                        tileMap = tileMap
                    )
                            && pushWithWideBoxes(
                        forceOriginPosition = pushedPosition.moved(Direction.RIGHT),
                        forceDirection = forceDirection,
                        tileMap = tileMap
                    )
                } else pushWithWideBoxes(pushedPosition, forceDirection, tileMap)
            }

            WideTileType.BOX_RIGHT -> {
                if (forceDirection.isVertical()) {
                    pushWithWideBoxes(
                        forceOriginPosition = pushedPosition,
                        forceDirection = forceDirection,
                        tileMap = tileMap
                    )
                            && pushWithWideBoxes(
                        forceOriginPosition = pushedPosition.moved(Direction.LEFT),
                        forceDirection = forceDirection,
                        tileMap = tileMap
                    )
                } else pushWithWideBoxes(pushedPosition, forceDirection, tileMap)
            }

            WideTileType.EMPTY -> true
        }
        val operations = mutableListOf<Pair<Position, WideTileType>>()
        if (canMoveToNextTile) {
            if (nextTile == WideTileType.BOX_LEFT) {
                if (forceDirection.isVertical()) {
                    val boxRightCurrentPosition = pushedPosition.moved(Direction.RIGHT)
                    val boxRightTargetPosition = boxRightCurrentPosition.moved(forceDirection)
                    tileMap[boxRightCurrentPosition.y][boxRightCurrentPosition.x] = WideTileType.EMPTY
                    tileMap[boxRightTargetPosition.y][boxRightTargetPosition.x] = WideTileType.BOX_RIGHT
                }
                val boxLeftTargetPosition = pushedPosition.moved(forceDirection)
                tileMap[boxLeftTargetPosition.y][boxLeftTargetPosition.x] = WideTileType.BOX_LEFT
            } else if (nextTile == WideTileType.BOX_RIGHT) {
                if (forceDirection.isVertical()) {
                    val boxLeftCurrentPosition = pushedPosition.moved(Direction.LEFT)
                    tileMap[boxLeftCurrentPosition.y][boxLeftCurrentPosition.x] = WideTileType.EMPTY
                    val boxLeftTargetPosition = boxLeftCurrentPosition.moved(forceDirection)
                    tileMap[boxLeftTargetPosition.y][boxLeftTargetPosition.x] = WideTileType.BOX_LEFT
                }
                val boxRightTargetPosition = pushedPosition.moved(forceDirection)
                tileMap[boxRightTargetPosition.y][boxRightTargetPosition.x] = WideTileType.BOX_RIGHT
            }
            tileMap[pushedPosition.y][pushedPosition.x] = WideTileType.EMPTY
        }
        return canMoveToNextTile
    }

    private fun String.widenTiles() = this.map { char ->
        when (char) {
            WALL -> listOf(WALL, WALL)
            EMPTY -> listOf(EMPTY, EMPTY)
            ROBOT -> listOf(ROBOT, EMPTY)
            BOX_NARROW -> listOf(BOX_LEFT, BOX_RIGHT)
            else -> listOf(char)
        }
    }.flatten().joinToString(separator = "")
}

private enum class NarrowTileType {
    WALL, EMPTY, BOX
}

private enum class WideTileType(val associatedChar: Char) {
    WALL(Day15.WALL), EMPTY(Day15.EMPTY), BOX_LEFT(Day15.BOX_LEFT), BOX_RIGHT(Day15.BOX_RIGHT)
}

private fun List<MutableList<WideTileType>>.printWithRobot(robot: Position) {
    this.mapIndexed { y, line ->
        line.mapIndexed { x, tile ->
            if (Position(x, y) == robot) ROBOT
            else tile.associatedChar
        }.joinToString(separator = "")
    }.forEach { println(it) }
    println("\n")
}

fun main() {

    val day = 15

//    println("\nPart 1:")
//    checkOnTestInput(day, 2028L, Day15::part1, suffix = "_small")
//    checkOnTestInput(day, 10_092L, Day15::part1, suffix = "_medium")
//    solve(day, Day15::part1)

    println("\nPart2:")
    checkOnTestInput(day, 9021L, Day15::part2, suffix = "_medium")
//    checkOnTestInput(day, 9021L, Day15::part2, suffix = "_georg")
//    solve(day, Day15::part2)
}
