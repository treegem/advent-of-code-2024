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
) {
    val testInput = readInput("Day${day.padded()}_test")
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
    val input = readInput("Day${day.padded()}")

    measureTime {
        solution(input)
            .also { println("Solution: $it") }
    }.also { println("Duration: $it") }
}

fun <T> Iterable<T>.withoutItemAt(index: Int): List<T> = filterIndexed { i, _ -> i != index }

private fun Int.padded() = toString().padStart(2, '0')
