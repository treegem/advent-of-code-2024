package de.gbrown.aoc2024.util

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTimedValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()

/**
 * Checks the solution for the TestInputs.
 */
fun checkOnTestInput(
    day: Int,
    expectedSolution: Number,
    solution: (List<String>) -> Number,
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
    solution: (List<String>) -> Number,
) {
    val input = readInput("Day${day.toPaddedString()}")

    measureTimedValue {
        solution(input)
    }.also {
        println("Solution: ${it.value}")
        println("Duration: ${it.duration}")
    }
}

private fun Int.toPaddedString() = toString().padStart(2, '0')
