@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Dijkstra
import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day20 {

    private val dijkstra = Day20Dijkstra()

    fun part1(input: List<String>): Int {
        val matrix = input.to2dCharsListMatrix()
        val availablePositions = matrix.findAllPositionsByPredicate { it != '#' }
        val wallPositions = matrix.findAllPositionsByPredicate { it == '#' }
        val startPosition = matrix.findAllPositionsByPredicate { it == 'S' }.single()
        val endPosition = matrix.findAllPositionsByPredicate { it == 'E' }.single()

        println("wall positions: ${wallPositions.size}")
        var wallCounter = 0
        val distanceNoCheat = dijkstra.calculateShortestDistance(availablePositions, startPosition, endPosition)!!
        return wallPositions.mapNotNull { wallPosition ->
            println(++wallCounter)
            dijkstra.calculateShortestDistance(availablePositions + wallPosition, startPosition, endPosition)
        }.count { it <= distanceNoCheat - 100 }
    }

    fun part2(input: List<String>): Long = TODO("Must process $input")
}

private class Day20Dijkstra : Dijkstra<Position>() {
    override fun distance(from: Position, to: Position): Long = 1

    override fun findNextNodes(currentNode: Position, availableNodes: List<Position>): List<Position> =
        Direction.nonDiagonals.map { currentNode.moved(it) }
            .filter { it in availableNodes }
}

fun main() {

    val day = 20

    println("\nPart 1:")
    checkOnTestInput(day, 0, Day20::part1)
    solve(day, Day20::part1)

    println("\nPart2:")
    checkOnTestInput(day, 0L, Day20::part2)
    solve(day, Day20::part2)
}
