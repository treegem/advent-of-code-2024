package de.gbrown.aoc2024

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()

/**
 * Checks the solution for the TestInputs.
 */
fun checkOnTestInput(
    day: Int,
    expectedSolution: Int,
    solution: (List<String>) -> Int,
    suffix: String = "",
) {
    val testInput = readInput("Day${day.toPaddedString()}${suffix}_test")
    val part1Test = solution(testInput)
    check(part1Test == expectedSolution) { "Test failed: got $part1Test, expected $expectedSolution" }
    println("Test passed.")
}

/**
 * Performs the solution for the given day.
 */
fun solve(
    day: Int,
    solution: (List<String>) -> Int,
) {
    val input = readInput("Day${day.toPaddedString()}")

    measureTime {
        solution(input)
            .also { println("Solution: $it") }
    }.also { println("Duration: $it") }
}

fun <T> Iterable<T>.withoutItemAt(index: Int): List<T> = filterIndexed { i, _ -> i != index }

fun <T> List<T>.swapped(value1: T, value2: T): List<T> {
    val copy = this.toMutableList()
    val value1Index = this.indexOf(value1)
    val value2Index = this.indexOf(value2)
    val temp = copy[value1Index]
    copy[value1Index] = copy[value2Index]
    copy[value2Index] = temp
    return copy.toList()
}

fun List<String>.to2dCharsListMatrix(): List<List<Char>> = map { it.toList() }

fun <T> List<List<T>>.findValueAt(x: Int, y: Int): T? = getOrNull(y)?.getOrNull(x)

fun <T> List<List<T>>.findValueAt(position: Pair<Int, Int>): T? = findValueAt(position.first, position.second)

fun Pair<Int, Int>.move(direction: Direction, steps: Int = 1): Pair<Int, Int> = when (direction) {
    Direction.UP -> first to second - steps
    Direction.UP_RIGHT -> first + steps to second - steps
    Direction.RIGHT -> first + steps to second
    Direction.DOWN_RIGHT -> first + steps to second + steps
    Direction.DOWN -> first to second + steps
    Direction.DOWN_LEFT -> first - steps to second + steps
    Direction.LEFT -> first - steps to second
    Direction.UP_LEFT -> first - steps to second - steps
}

fun <T> List<List<T>>.findValueInDirection(
    x: Int,
    y: Int,
    direction: Direction,
    steps: Int = 1,
): T? = findValueAt((x to y).move(direction, steps))

enum class Direction {
    UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT
}

private fun Int.toPaddedString() = toString().padStart(2, '0')
