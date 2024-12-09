@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

object Day04 {

    fun part1(input: List<String>): Int {

        val xPositions = findAllPositionsOf('X', input)

        return countAllXmas(xPositions, input.to2dCharsListMatrix())
    }

    fun part2(input: List<String>): Int {

        val aPositions = findAllPositionsOf('A', input)

        return countAllMasX(aPositions, input.to2dCharsListMatrix())
    }

    private fun countAllMasX(aPositions: List<Pair<Int, Int>>, input: List<List<Char>>) =
        aPositions.mapNotNull { (y, x) ->
            val smSet = listOf('M', 'S')
            val charDownLeft = input.findValueInDirection(x, y, Direction.DOWN_LEFT)
            val charUpRight = input.findValueInDirection(x, y, Direction.UP_RIGHT)
            val charDownRight = input.findValueInDirection(x, y, Direction.DOWN_RIGHT)
            val charUpLeft = input.findValueInDirection(x, y, Direction.UP_LEFT)
            if (charDownLeft in smSet && charUpRight == (smSet - charDownLeft).single() &&
                charUpLeft in smSet && charDownRight == (smSet - charUpLeft).single()
            ) {
                1
            } else null
        }.count()

    private fun findAllPositionsOf(target: Char, input: List<String>) = input.mapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            if (char == target) Pair(y, x) else null
        }
    }.flatten()

    private fun countAllXmas(
        xPositions: List<Pair<Int, Int>>,
        input: List<List<Char>>,
    ) = xPositions.sumOf { (y, x) ->
        Direction.entries.mapNotNull { direction ->
            if (input.findValueInDirection(x, y, direction, 1) == 'M' &&
                input.findValueInDirection(x, y, direction, 2) == 'A' &&
                input.findValueInDirection(x, y, direction, 3) == 'S'
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
