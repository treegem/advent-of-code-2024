@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findValueInDirection
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day04 {

    fun part1(input: List<String>): Int {

        val xPositions = findAllPositionsOf('X', input)

        return countAllXmas(xPositions, input.to2dCharsListMatrix())
    }

    fun part2(input: List<String>): Int {

        val aPositions = findAllPositionsOf('A', input)

        return countAllMasX(aPositions, input.to2dCharsListMatrix())
    }

    private fun countAllMasX(aPositions: List<Position>, input: List<List<Char>>) =
        aPositions.mapNotNull { aPosition ->
            val smSet = listOf('M', 'S')
            val charDownLeft = input.findValueInDirection(aPosition, Direction.DOWN_LEFT)
            val charUpRight = input.findValueInDirection(aPosition, Direction.UP_RIGHT)
            val charDownRight = input.findValueInDirection(aPosition, Direction.DOWN_RIGHT)
            val charUpLeft = input.findValueInDirection(aPosition, Direction.UP_LEFT)
            if (charDownLeft in smSet && charUpRight == (smSet - charDownLeft).single() &&
                charUpLeft in smSet && charDownRight == (smSet - charUpLeft).single()
            ) {
                1
            } else null
        }.count()

    private fun findAllPositionsOf(target: Char, input: List<String>) = input.mapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            if (char == target) Position(x, y) else null
        }
    }.flatten()

    private fun countAllXmas(
        xPositions: List<Position>,
        input: List<List<Char>>,
    ) = xPositions.sumOf { xPosition ->
        Direction.entries.mapNotNull { direction ->
            if (input.findValueInDirection(xPosition, direction, 1) == 'M' &&
                input.findValueInDirection(xPosition, direction, 2) == 'A' &&
                input.findValueInDirection(xPosition, direction, 3) == 'S'
            ) {
                1
            } else null
        }.sum()
    }
}

fun main() {

    val day = 4

    println("\nPart 1:")
    checkOnTestInput(day, 18, Day04::part1)
    solve(day, Day04::part1)

    println("\nPart2:")
    checkOnTestInput(day, 9, Day04::part2)
    solve(day, Day04::part2)
}
