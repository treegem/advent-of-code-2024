@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day19 {

    fun part1(input: List<String>): Int {
        val (towels: List<String>, designs: List<String>) = extractTowelsAndDesigns(input)

        return designs.count { canBeArranged(it, towels) }
    }

    fun part2(input: List<String>): Long {
        val (towels: List<String>, designs: List<String>) = extractTowelsAndDesigns(input)
        val arrangementCache = mutableMapOf<String, Long>()
        return designs.sumOf { countArrangements(it, towels, arrangementCache) }
    }

    private fun extractTowelsAndDesigns(input: List<String>): Pair<List<String>, List<String>> {
        val (rawTowels, designs) =
            input.filter { it.isNotBlank() }
                .partition { it.contains(",") }
        val towels = rawTowels.single().split(", ")
            .sortedByDescending { it.length }

        return Pair(towels, designs)
    }

    private fun canBeArranged(design: String, towels: List<String>): Boolean {
        val constructionAttempts = mutableListOf("")

        while (constructionAttempts.isNotEmpty()) {
            val nextAttempts = calculateNextAttempts(constructionAttempts, towels, design)
            constructionAttempts.clear()
            constructionAttempts.addAll(nextAttempts.filter { it.length <= design.length }.toSet())
            if (design in constructionAttempts) return true
        }

        return false
    }

    private fun countArrangements(
        design: String,
        towels: List<String>,
        arrangementCache: MutableMap<String, Long>,
    ): Long =
        when {
            design.isEmpty() -> 1
            design in arrangementCache -> arrangementCache[design]!!
            else -> {
                towels.sumOf { towel ->
                    if (design.startsWith(towel)) {
                        countArrangements(design.removePrefix(towel), towels, arrangementCache)
                    } else {
                        0
                    }
                }.also { arrangementCache[design] = it }
            }
        }

    private fun calculateNextAttempts(
        constructionAttempts: MutableList<String>,
        towels: List<String>,
        design: String,
    ): List<String> {
        val nextAttempts = mutableListOf<String>()
        constructionAttempts.forEach { attempt ->
            towels.forEach { towel ->
                if (design.startsWith(attempt + towel)) {
                    nextAttempts.add(attempt + towel)
                }
            }
        }
        return nextAttempts.toList()
    }
}

fun main() {

    val day = 19

    println("\nPart 1:")
    checkOnTestInput(day, 6, Day19::part1)
    solve(day, Day19::part1)

    println("\nPart2:")
    checkOnTestInput(day, 7L, Day19::part2, suffix = "_ex1")
    checkOnTestInput(day, 16L, Day19::part2)
    solve(day, Day19::part2)
}
