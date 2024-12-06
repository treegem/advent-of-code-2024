@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

object Day03 {

    fun part1(input: List<String>): Int {
        val commandRegex = Regex("mul\\(\\d+,\\d+\\)")
        val inputAsOneLine = input.joinToString(separator = "")
        val multiplicationCommands = commandRegex.findAll(inputAsOneLine)

        return multiplicationCommands.fold(0) { sumAcc, command ->
            command.value
                .executeMultiplication()
                .let { it + sumAcc }
        }
    }

    fun part2(input: List<String>): Int {
        var computationEnabled = true

        val commandRegex = Regex("(mul\\(\\d+,\\d+\\)|don't\\(\\)|do\\(\\))")
        val allCommands = commandRegex.findAll(input.joinToString(separator = ""))

        return allCommands.fold(0) { sumAcc, command ->
            when {
                command.value.startsWith("mul") && computationEnabled -> {
                    command.value.executeMultiplication().let { it + sumAcc }
                }

                command.value.startsWith("don't") -> {
                    computationEnabled = false
                    sumAcc
                }

                command.value.startsWith("do") -> {
                    computationEnabled = true
                    sumAcc
                }

                else -> sumAcc
            }
        }
    }

    private fun String.executeMultiplication() =
        this.removePrefix("mul(")
            .removeSuffix(")")
            .split(",")
            .fold(1) { acc, value -> acc * value.toInt() }
}

fun main() {

    val day = 3

    println("\nPart 1:")
    checkOnTestInput(day, 161, Day03::part1, suffix = "part1")
    solve(day, Day03::part1)

    println("\nPart2:")
    checkOnTestInput(day, 48, Day03::part2, suffix = "part2")
    solve(day, Day03::part2)
}
