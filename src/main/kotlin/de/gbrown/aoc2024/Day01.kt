@file:Suppress("MagicNumber")

import Day01.part1
import Day01.part2

object Day01 {

    private val wordsToNumberStrings = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )

    private val encodingStrings = wordsToNumberStrings.keys + wordsToNumberStrings.values

    fun part1(input: List<String>): Int =
        input.map { line -> listOf(line.first { it.isDigit() }, line.last { it.isDigit() }) }
            .map { it.joinToString(separator = "") }
            .sumOf { it.toInt() }

    fun part2(input: List<String>): Int =
        input.map { line ->
            listOf(line.findAnyOf(encodingStrings)!!.second, line.findLastAnyOf(encodingStrings)!!.second)
        }
            .map { firstLastPair -> firstLastPair.map { wordsToNumberStrings[it] ?: it } }
            .map { it.joinToString(separator = "") }
            .sumOf { it.toInt() }
}

fun main() {

    val testInputPart1 = readInput("Day01_test_part1")
    check(part1(testInputPart1).also(::println) == 142)

    val testInputPart2 = readInput("Day01_test_part2")
    check(part2(testInputPart2).also(::println) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
