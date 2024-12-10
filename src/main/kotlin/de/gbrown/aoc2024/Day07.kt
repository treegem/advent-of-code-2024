@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

object Day07 {

    fun part1(input: List<String>) =
        input.map { it.toResultAndNumbers() }
            .filter { (result, numbers) -> doesAdditionAndMultiplicationYieldResult(result, numbers) }
            .sumOf { (result, _) -> result }

    fun part2(input: List<String>): Int = TODO("Must process $input")

    private fun doesAdditionAndMultiplicationYieldResult(result: Long, numbers: List<Long>): Boolean {
        val remainingNumbers = numbers.toMutableList()
        var currentCombinations = listOf(remainingNumbers.removeFirst())
        while (remainingNumbers.isNotEmpty()) {
            val nextNumber = remainingNumbers.removeFirst()
            currentCombinations = currentCombinations.map {
                listOf(it + nextNumber, it * nextNumber)
            }.flatten()
        }
        return result in currentCombinations
    }

    private fun String.toResultAndNumbers() =
        this.split(": ")
            .let { (result, numbers) -> result.toLong() to numbers.split(" ").map { it.toLong() } }
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
