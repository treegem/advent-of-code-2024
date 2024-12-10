@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve
import kotlin.math.abs

object Day01 {

    private fun splitLeftAndRight(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input.forEach { line ->
            val values = line.split(" ").filter { it.isNotBlank() }
            leftList.add(values[0].toInt())
            rightList.add(values[1].toInt())
        }
        return Pair(leftList, rightList)
    }

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = splitLeftAndRight(input)
        return leftList.sorted()
            .zip(rightList.sorted())
            .sumOf { (left, right) ->
                abs(left - right)
            }
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = splitLeftAndRight(input)

        return leftList.sumOf { leftValue ->
            leftValue * rightList.count { it == leftValue }
        }
    }
}

fun main() {

    val day = 1

    println("\nPart 1:")
    checkOnTestInput(day, 11, Day01::part1)
    solve(day, Day01::part1)

    println("\nPart2:")
    checkOnTestInput(day, 31, Day01::part2)
    solve(day, Day01::part2)
}
