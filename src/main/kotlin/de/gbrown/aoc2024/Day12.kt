@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day12 {

    fun part1(input: List<String>): Int = TODO("Must process $input")

    fun part2(input: List<String>): Int = TODO("Must process $input")
}

fun main() {

    val day = 12

    println("\nPart 1:")
    checkOnTestInput(day, 140, Day12::part1, suffix = "_ex1")
    checkOnTestInput(day, 772, Day12::part1, suffix = "_ex2")
    checkOnTestInput(day, 1930, Day12::part1, suffix = "_ex3")
    solve(day, Day12::part1)

    println("\nPart2:")
    checkOnTestInput(day, 0, Day12::part2)
    solve(day, Day12::part2)
}
