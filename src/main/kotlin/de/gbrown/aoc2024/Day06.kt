@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findValueAt
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day06 {

    fun part1(input: List<String>): Int {
        val matrix = input.to2dCharsListMatrix()

        val visited = getAllVisitedPositions(matrix)
        return visited.size
    }

    fun part2(input: List<String>): Int {
        val matrix = input.to2dCharsListMatrix()

        val visited = getAllVisitedPositions(matrix)
        val (initialX: Int, initialY: Int, initialDirection: Direction) = getStart(matrix)

        return visited.count { position ->
            val startingConditions = Pair(Position(initialX, initialY), initialDirection)
            matrix.withBlockAt(position).containsLoop(startingConditions)
        }
    }

    private fun List<List<Char>>.withBlockAt(position: Position): List<List<Char>> {
        val copy = this.map { it.toMutableList() }.toMutableList()
        copy[position.y][position.x] = '#'
        return copy.map { it.toList() }.toList()
    }

    private fun getAllVisitedPositions(
        matrix: List<List<Char>>,
    ): MutableSet<Position> {
        val (initialX: Int, initialY: Int, initialDirection: Direction) = getStart(matrix)
        val visited = mutableSetOf<Position>()
        var currentPosition = Position(initialX, initialY)
        var currentDirection = initialDirection
        while (matrix.findValueAt(currentPosition) != null) {
            visited.add(currentPosition)
            while (matrix.findValueAt(currentPosition.moved(currentDirection)) == '#') {
                currentDirection = currentDirection.turnRight()
            }
            currentPosition = currentPosition.moved(currentDirection)
        }
        return visited
    }

    private fun Direction.turnRight(): Direction = when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> error("Unknown direction: $this")
    }

    private fun getStart(matrix: List<List<Char>>): Triple<Int, Int, Direction> {
        val startingChars = mapOf(
            '<' to Direction.LEFT,
            '>' to Direction.RIGHT,
            '^' to Direction.UP,
            'v' to Direction.DOWN,
        )
        return matrix.mapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char in startingChars) Triple(x, y, startingChars[char]!!) else null
            }
        }.flatten().single()
    }

    private fun List<List<Char>>.containsLoop(startingConditions: Pair<Position, Direction>): Boolean {
        val visited = mutableSetOf<Pair<Position, Direction>>()
        var current = startingConditions
        while (this.findValueAt(current.first) != null) {
            if (visited.contains(current)) return true
            visited.add(current)
            var currentDirection = current.second
            while (this.findValueAt(current.first.moved(currentDirection)) == '#') {
                currentDirection = currentDirection.turnRight()
            }
            current = Pair(current.first.moved(currentDirection), currentDirection)
        }
        return false
    }
}

fun main() {

    val day = 6

    println("\nPart 1:")
    checkOnTestInput(day, 41, Day06::part1)
    solve(day, Day06::part1)

    println("\nPart2:")
    checkOnTestInput(day, 6, Day06::part2)
    solve(day, Day06::part2)
}
