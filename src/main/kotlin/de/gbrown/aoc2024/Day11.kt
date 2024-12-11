@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day11 {

    fun part1(input: List<String>): Int {
        var stones = input.first().split(" ").map { it.toLong() }

        repeat(25) { stones = blink(stones) }

        return stones.count()
    }

    fun part2(input: List<String>): Int {
        var stones = input.first().split(" ").map { it.toLong() }

        repeat(75) { counter ->
            if (counter % 5 == 0) {
                println(counter)
            }
            stones = blink(stones)
        }

        return stones.count()
    }

    private fun addSplitStones(newStones: MutableList<Long>, stone: Long) {
        val stoneString = stone.toString()
        val half = stoneString.length / 2
        newStones.add(stoneString.substring(startIndex = 0, endIndex = half).toLong())
        newStones.add(stoneString.substring(startIndex = half).toLong())
    }

    private fun blink(stones: List<Long>): List<Long> {
        val newStones = mutableListOf<Long>()
        stones.forEach { stone ->
            when {
                stone == 0L -> newStones.add(1)
                (stone.toString().length % 2) == 0 -> addSplitStones(newStones, stone)
                else -> newStones.add(stone * 2024)
            }
        }
        return newStones
    }
}

fun main() {

    val day = 11

    println("\nPart 1:")
    checkOnTestInput(day, 55_312, Day11::part1)
    solve(day, Day11::part1)

    println("\nPart2:")
    checkOnTestInput(day, 0, Day11::part2)
    solve(day, Day11::part2)
}
