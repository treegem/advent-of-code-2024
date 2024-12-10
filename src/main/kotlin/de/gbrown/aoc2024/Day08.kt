@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day08 {

    fun part1(input: List<String>): Int {

        val frequencyPositions = groupPositionsByFrequency(input)
        val antinodeCreationPairs = determineAllAntiNodeCreationPairs(frequencyPositions)

        val xBounds = input.first().indices
        val yBounds = input.indices

        return antinodeCreationPairs.createAllAntinodePositions()
            .distinct()
            .count { it.isWithinBounds(xBounds, yBounds) }
    }

    fun part2(input: List<String>): Int {
        val frequencyPositions = groupPositionsByFrequency(input)
        val antinodeCreationPairs = determineAllAntiNodeCreationPairs(frequencyPositions)

        val xBounds = input.first().indices
        val yBounds = input.indices

        return antinodeCreationPairs.createAllAntinodePositionsReoccurring(xBounds, yBounds)
            .count()
    }

    private fun groupPositionsByFrequency(input: List<String>): Map<Char, List<Position>> {
        val frequencyPositions = mutableMapOf<Char, MutableList<Position>>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char != '.') {
                    frequencyPositions.getOrPut(char) { mutableListOf() }.add(Position(x, y))
                }
            }
        }

        return frequencyPositions.toMap()
    }

    private fun determineAllAntiNodeCreationPairs(frequencyPositions: Map<Char, List<Position>>): List<Pair<Position, Position>> =
        frequencyPositions.map { (_, positions) ->
            positions.mapIndexed { index, position ->
                positions.drop(index + 1).map { otherPosition ->
                    Pair(position, otherPosition)
                }
            }
        }.flatten().flatten()

    private fun List<Pair<Position, Position>>.createAllAntinodePositions(): List<Position> =
        map { (firstPosition, secondPosition) ->
            val diff = firstPosition - secondPosition
            val firstAntinodePosition = firstPosition + diff
            val secondAntinodePosition = secondPosition - diff
            listOf(firstAntinodePosition, secondAntinodePosition)
        }.flatten()

    private fun List<Pair<Position, Position>>.createAllAntinodePositionsReoccurring(
        xBounds: IntRange,
        yBounds: IntRange,
    ): List<Position> {
        val antinodePositions = mutableSetOf<Position>()
        forEach { (firstPosition, secondPosition) ->
            val diff = firstPosition - secondPosition
            var currentAntinodePosition = firstPosition
            while (currentAntinodePosition.isWithinBounds(xBounds, yBounds)) {
                antinodePositions.add(currentAntinodePosition)
                currentAntinodePosition += diff
            }
            currentAntinodePosition = secondPosition
            while (currentAntinodePosition.isWithinBounds(xBounds, yBounds)) {
                antinodePositions.add(currentAntinodePosition)
                currentAntinodePosition -= diff
            }
        }
        return antinodePositions.toList()
    }
}

fun main() {

    val day = 8

    println("\nPart 1:")
    checkOnTestInput(day, 14, Day08::part1)
    solve(day, Day08::part1)

    println("\nPart2:")
    checkOnTestInput(day, 34, Day08::part2)
    solve(day, Day08::part2)
}
