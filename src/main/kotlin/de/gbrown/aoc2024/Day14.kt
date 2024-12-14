@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.Velocity
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day14 {

    fun part1(input: List<String>, width: Int, height: Int): Int {
        val robots = input.map { Robot.from(it) }

        val positionsAfter100Seconds = robots
            .map { robot -> advanceInTime(robot, 100) }
            .map { it.position }
            .map { position -> restrainToBoundaries(position, width, height) }

        return positionsAfter100Seconds
            .splitIntoQuadrants(width, height)
            .map { it.count() }
            .reduce(Int::times)
    }

    fun part2(input: List<String>): Int = TODO("Must process $input")

    private fun advanceInTime(robot: Robot, seconds: Int): Robot = Robot(
        robot.position.moved(robot.velocity * seconds),
        robot.velocity,
    )

    private fun restrainToBoundaries(position: Position, width: Int, height: Int): Position = Position(
        (position.x % width).takeIf { it >= 0 } ?: (width + (position.x % width)),
        (position.y % height).takeIf { (it >= 0) } ?: (height + (position.y % height)),
    )

    private fun List<Position>.splitIntoQuadrants(width: Int, height: Int): List<List<Position>> {

        val leftXs = 0 until width / 2 // knowing width is always odd
        val rightXs = (width / 2 + 1) until width
        val topYs = 0 until height / 2 // knowing height is always odd
        val bottomYs = (height / 2 + 1) until height

        val firstQuadrant = this.filter { it.x in leftXs && it.y in topYs }
        val secondQuadrant = this.filter { it.x in rightXs && it.y in topYs }
        val thirdQuadrant = this.filter { it.x in leftXs && it.y in bottomYs }
        val fourthQuadrant = this.filter { it.x in rightXs && it.y in bottomYs }

        return listOf(firstQuadrant, secondQuadrant, thirdQuadrant, fourthQuadrant)
    }
}

private data class Robot(val position: Position, val velocity: Velocity) {
    companion object {
        fun from(line: String): Robot {
            val (x, y, vx, vy) = line
                .removePrefix("p=")
                .split(" v=")
                .map { it.split(",") }
                .flatten()
                .map { it.toInt() }

            return Robot(Position(x, y), Velocity(vx, vy))
        }
    }
}

fun main() {

    val day = 14

    println("\nPart 1:")
    checkOnTestInput(day, 12, { Day14.part1(it, 11, 7) })
    solve(day) { Day14.part1(it, 101, 103) }

    println("\nPart2:")
    checkOnTestInput(day, 0, Day14::part2)
    solve(day, Day14::part2)
}
