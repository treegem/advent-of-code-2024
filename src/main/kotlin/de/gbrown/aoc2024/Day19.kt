@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day19 {

    fun part1(input: List<String>): Int {
        val (towels: List<String>, designs: List<String>) = extractTowelsAndDesigns(input)

        return designs.count { canBeArranged(it, towels) }
    }

    fun part2(input: List<String>): Int {
        val (towels: List<String>, designs: List<String>) = extractTowelsAndDesigns(input)

        return designs
            .filter { canBeArranged(it, towels) }
            .sumOf { countArrangements(it, towels) }
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
    ): Int {
        val arrangements = mutableMapOf<String, MutableSet<List<String>>>().withDefault { mutableSetOf() }
        towels.forEach { arrangements[it] = mutableSetOf(listOf(it)) }

        var updated = -1
        println("design: $design")

        while (updated != 0) {
            updated = updateArrangements(arrangements, design)
        }
        arrangements.filterKeys { design.startsWith(it) }.maxBy { it.key.length }.also { println(it) }
        return arrangements.getValue(design).size
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

    private fun updateArrangements(
        arrangements: MutableMap<String, MutableSet<List<String>>>,
        targetDesign: String,
    ): Int {
        var updated = 0
        val keys = arrangements.keys
        val newArrangements = mutableMapOf<String, MutableSet<List<String>>>()
        keys.forEach { root ->
            keys.forEach inner@{ newBuildingBlock ->
                val combinedKeys = root + newBuildingBlock
                if (targetDesign.startsWith(combinedKeys)) {
                    arrangements.getValue(root).flatMap { rootArrangements ->
                        arrangements.getValue(newBuildingBlock).map { newBlockArrangements ->
                            val combinedBlocks = rootArrangements + newBlockArrangements
                            if (combinedBlocks !in arrangements.getValue(combinedKeys)) {
                                newArrangements[combinedKeys] =
                                    arrangements.getValue(combinedKeys).apply { add(combinedBlocks) }
                                updated++
                            }
                        }
                    }
                }
            }
        }
        newArrangements.forEach { (key, value) ->
            arrangements[key] = (arrangements.getValue(key) + value).toMutableSet()
        }
        return updated
    }
}

fun main() {

    val day = 19

//    println("\nPart 1:")
//    checkOnTestInput(day, 6, Day19::part1)
//    solve(day, Day19::part1)

    println("\nPart2:")
    checkOnTestInput(day, 7, Day19::part2, suffix = "_ex1")
    checkOnTestInput(day, 16, Day19::part2)
    solve(day, Day19::part2)
}
