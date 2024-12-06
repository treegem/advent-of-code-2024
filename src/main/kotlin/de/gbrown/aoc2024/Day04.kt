@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

object Day04 {

    fun part1(input: List<String>): Int {

        val xPositions = findAllPositionsOf('X', input)

        return countAllXmas(xPositions, input)
    }

    fun part2(input: List<String>): Int {

        val aPositions = findAllPositionsOf('A', input)

        return countAllMasX(aPositions, input)
    }

    private fun countAllMasX(aPositions: List<Pair<Int, Int>>, input: List<String>) =
        aPositions.mapNotNull { (y, x) ->
            val smSet = listOf('M', 'S')
            val charDownLeft = input.findCharInDirection(x, y, Direction.DOWN_LEFT)
            val charUpRight = input.findCharInDirection(x, y, Direction.UP_RIGHT)
            val charDownRight = input.findCharInDirection(x, y, Direction.DOWN_RIGHT)
            val charUpLeft = input.findCharInDirection(x, y, Direction.UP_LEFT)
            if (charDownLeft in smSet && charUpRight == (smSet - charDownLeft).single() &&
                charUpLeft in smSet && charDownRight == (smSet - charUpLeft).single()
            ) {
                1
            } else null
        }.count()

    private fun findAllPositionsOf(target: Char, input: List<String>) = input.mapIndexed { lineIndex, line ->
        line.mapIndexedNotNull { charIndex, char ->
            if (char == target) Pair(lineIndex, charIndex) else null
        }
    }.flatten()

    private fun countAllXmas(
        xPositions: List<Pair<Int, Int>>,
        input: List<String>,
    ) = xPositions.sumOf { (y, x) ->
        Direction.entries.mapNotNull { direction ->
            if (input.findCharInDirection(x, y, direction, 1) == 'M' &&
                input.findCharInDirection(x, y, direction, 2) == 'A' &&
                input.findCharInDirection(x, y, direction, 3) == 'S'
            ) {
                1
            } else null
        }.sum()
    }
}


private enum class Direction {
    UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT
}

private fun List<String>.findCharAt(x: Int, y: Int): Char? = getOrNull(y)?.getOrNull(x)

private fun List<String>.findCharInDirection(
    x: Int,
    y: Int,
    direction: Direction,
    steps: Int = 1,
): Char? = when (direction) {
    Direction.UP -> findCharAt(x, y - steps)
    Direction.UP_RIGHT -> findCharAt(x + steps, y - steps)
    Direction.RIGHT -> findCharAt(x + steps, y)
    Direction.DOWN_RIGHT -> findCharAt(x + steps, y + steps)
    Direction.DOWN -> findCharAt(x, y + steps)
    Direction.DOWN_LEFT -> findCharAt(x - steps, y + steps)
    Direction.LEFT -> findCharAt(x - steps, y)
    Direction.UP_LEFT -> findCharAt(x - steps, y - steps)
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
