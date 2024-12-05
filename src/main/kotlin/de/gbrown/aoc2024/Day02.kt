@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import kotlin.math.abs
import kotlin.math.sign

object Day02 {

    fun part1(input: List<String>): Int = getReports(input).count { it.isSafeStrict() }

    fun part2(input: List<String>): Int = getReports(input).count { it.isSafeStrict() || it.isSafeWithDampener() }

    private fun getReports(input: List<String>) = input.map { line ->
        line.split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
    }
}

private fun List<Int>.isSafeStrict(): Boolean {
    val monotony = mutableSetOf<Int>()

    this.forEachIndexed { index, value ->
        if (index == 0) return@forEachIndexed

        val diff = value - this[index - 1]

        monotony.add(diff.sign)
        if (monotony.size > 1) return false

        val diffAbs = abs(diff)
        if (diffAbs < 1 || diffAbs > 3) return false
    }
    return true
}

private fun List<Int>.isSafeWithDampener(): Boolean {
    this.indices.forEach { index ->
        val sublist = this.withoutItemAt(index)
        if (sublist.isSafeStrict()) return true
    }

    return false
}

fun main() {

    val day = 2

    println("\nPart 1:")
    checkOnTestInput(day, 2, Day02::part1)
    solve(day, Day02::part1)

    println("\nPart2:")
    checkOnTestInput(day, 4, Day02::part2)
    solve(day, Day02::part2)
}
