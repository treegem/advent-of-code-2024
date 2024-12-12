@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day12 {

    fun part1(input: List<String>): Int =
        input.to2dCharsListMatrix()
            .toRegionsMap()
            .values
            .flatten()
            .sumOf { calculateFencePrice(it) }

    fun part2(input: List<String>): Int =
        input.to2dCharsListMatrix()
            .toRegionsMap()
            .values
            .flatten()
            .sumOf { calculateFencePriceWithDiscount(it) }

    private fun List<List<Char>>.toRegionsMap(): Map<Char, List<List<Position>>> {
        val regions = mutableMapOf<Char, List<List<Position>>>()
        val plants = this.flatten().distinct()
        plants.forEach { plant ->
            val plantPositions = this.findAllPositionsByPredicate { it == plant }
            regions[plant] = plantPositions.separateIntoRegions()
        }
        return regions.toMap()
    }

    private fun calculateFencePrice(region: List<Position>): Int {
        val area = region.size
        val circumference = region.map { it.countDirectNeighbors(region) }.sumOf { 4 - it }
        return area * circumference
    }

    private fun calculateFencePriceWithDiscount(region: List<Position>): Int {
        val area = region.size
        val sides = region.sumOf { position ->
            countCorners(position, region)
        }
        return area * sides
    }

    private fun countCorners(position: Position, region: List<Position>): Int {
        val directNeighbors = region.filter { it.isDirectNeighborOf(position) }

        return when (directNeighbors.size) {
            0 -> 4
            1 -> 2
            2 -> countCornersForTwoDirectNeighbors(position, directNeighbors, region)
            3 -> countCornersForThreeDirectNeighbors(position, directNeighbors, region)
            4 -> countCornersForFourDirectNeighbors(position, region)
            else -> error("Unexpected number of neighbors: ${directNeighbors.size}")
        }
    }

    private fun countCornersForTwoDirectNeighbors(
        position: Position,
        directNeighbors: List<Position>,
        region: List<Position>,
    ) = when {
        position.formsLineWith(directNeighbors) -> 0
        position.enclosesDiagonalNeighbor(directNeighbors, region) -> 1
        else -> 2
    }

    private fun countCornersForThreeDirectNeighbors(
        position: Position,
        directNeighbors: List<Position>,
        region: List<Position>,
    ): Int {
        val sameXNeighbors = directNeighbors.filter { it.x == position.x }
        val sameYNeighbors = directNeighbors.filter { it.y == position.y }

        val lineNeighbors = sameXNeighbors.takeIf { it.size == 2 } ?: sameYNeighbors
        val perpendicularNeighbor = (sameXNeighbors.takeUnless { it.size == 2 } ?: sameYNeighbors).single()

        val enclosedDiagonalNeighbors = lineNeighbors.count { lineNeighbor ->
            val enclosingNeighbors = listOf(lineNeighbor, perpendicularNeighbor)
            position.enclosesDiagonalNeighbor(enclosingNeighbors, region)
        }

        return 2 - enclosedDiagonalNeighbors
    }

    private fun countCornersForFourDirectNeighbors(position: Position, region: List<Position>): Int =
        Direction.diagonals.count { direction ->
            !region.contains(position.moved(direction))
        }

    private fun List<Position>.separateIntoRegions(): List<List<Position>> {
        val regions = mutableListOf<MutableList<Position>>()
        this.forEach { position ->
            val neighboringRegions = regions.filter { region -> region.any { it.isDirectNeighborOf(position) } }

            when (neighboringRegions.size) {
                0 -> regions.add(mutableListOf(position))
                1 -> neighboringRegions.first().add(position)
                else -> {
                    val mergedRegion = neighboringRegions
                        .map { it.toList() }
                        .reduce { acc, region -> acc + region }
                        .toMutableList()
                    mergedRegion.add(position)
                    regions.removeAll(neighboringRegions)
                    regions.add(mergedRegion)
                }
            }
        }
        return regions.map { it.toList() }.toList()
    }

    private fun Position.isDirectNeighborOf(other: Position) = Direction.nonDiagonals.any { this.moved(it) == other }

    private fun Position.countDirectNeighbors(region: List<Position>) = region.count { it.isDirectNeighborOf(this) }

    private fun Position.enclosesDiagonalNeighbor(directNeighbors: List<Position>, region: List<Position>): Boolean {
        val enclosedX = directNeighbors.find { it.x != this.x }!!.x
        val enclosedY = directNeighbors.find { it.y != this.y }!!.y

        return Position(enclosedX, enclosedY) in region

    }

    private fun Position.formsLineWith(directNeighbors: List<Position>) =
        directNeighbors.all { it.x == this.x } || directNeighbors.all { it.y == this.y }
}

fun main() {

    val day = 12

    println("\nPart 1:")
    checkOnTestInput(day, 140, Day12::part1, suffix = "_ex1")
    checkOnTestInput(day, 772, Day12::part1, suffix = "_ex2")
    checkOnTestInput(day, 1930, Day12::part1, suffix = "_ex3")
    solve(day, Day12::part1)

    println("\nPart2:")
    checkOnTestInput(day, 80, Day12::part2, suffix = "_ex1")
    checkOnTestInput(day, 436, Day12::part2, suffix = "_ex2")
    checkOnTestInput(day, 1206, Day12::part2, suffix = "_ex3")
    checkOnTestInput(day, 236, Day12::part2, suffix = "_ex4")
    checkOnTestInput(day, 368, Day12::part2, suffix = "_ex5")
    solve(day, Day12::part2)
}
