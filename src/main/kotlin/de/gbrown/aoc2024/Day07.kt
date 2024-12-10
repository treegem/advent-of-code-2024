@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day07 {

    fun part1(input: List<String>) = solveWithOperations(input, listOf(::addition, ::multiplication))

    fun part2(input: List<String>) = solveWithOperations(input, listOf(::addition, ::multiplication, ::concatenation))

    private fun solveWithOperations(input: List<String>, operations: List<(Number, Number) -> Long>) =
        input.map { it.toResultAndNumbers() }
            .filter { (result, numbers) -> doNumbersYieldResult(result, numbers, operations) }
            .sumOf { (result, _) -> result }

    private fun doNumbersYieldResult(
        result: Long,
        numbers: List<Long>,
        operations: List<(Number, Number) -> Long>,
    ): Boolean {
        val remainingNumbers = numbers.toMutableList()
        var currentCombinations = listOf(remainingNumbers.removeFirst())
        while (remainingNumbers.isNotEmpty()) {
            val nextNumber = remainingNumbers.removeFirst()
            currentCombinations = currentCombinations.map {
                operations.map { operation -> operation(it, nextNumber) }
            }.flatten()
        }
        return result in currentCombinations
    }

    private fun String.toResultAndNumbers() =
        this.split(": ")
            .let { (result, numbers) -> result.toLong() to numbers.split(" ").map { it.toLong() } }

    private fun addition(a: Number, b: Number) = a.toLong() + b.toLong()
    private fun multiplication(a: Number, b: Number) = a.toLong() * b.toLong()
    private fun concatenation(a: Number, b: Number) = "$a$b".toLong()
}

fun main() {

    val day = 7

    println("\nPart 1:")
    checkOnTestInput(day, 3749L, Day07::part1)
    solve(day, Day07::part1)

    println("\nPart2:")
    checkOnTestInput(day, 11_387L, Day07::part2)
    solve(day, Day07::part2)
}
