@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.Velocity
import de.gbrown.aoc2024.util.solve
import java.io.File

object Day14 {

    const val WIDTH = 101
    const val HEIGHT = 103

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

    fun part2(input: List<String>): Int {
        var robots = input.map { Robot.from(it) }
        val file = File("test.txt")
        file.writeText("")
        repeat(10000) { iteration ->
            if ((iteration - 28) % 101 == 0) {
                file.appendText("\nIteration: $iteration \n")
            }
            robots = robots
                .map { robot -> advanceInTime(robot, 1) }
                .map { robot -> Robot(restrainToBoundaries(robot.position, WIDTH, HEIGHT), robot.velocity) }
            if ((iteration - 28) % 101 == 0) {
                robots.map { it.position }
                    .toStringList(WIDTH, HEIGHT)
                    .map {
                        file.appendText("$it\n")
                    }
                file.appendText("\n")
            }
        }
        return 0
    }

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

    private fun List<Position>.toStringList(width: Int, height: Int): List<String> {
        val resultList = mutableListOf<String>()
        (0 until height).forEach { y ->
            val row = (0 until width).joinToString("") { x ->
                if (Position(x, y) in this) "#" else "."
            }
            resultList.add(row)
        }
        return resultList
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

//    println("\nPart 1:")
//    checkOnTestInput(day, 12, { Day14.part1(it, 11, 7) })
//    solve(day) { Day14.part1(it, WIDTH, HEIGHT) }

    println("\nPart2:")
    solve(day, Day14::part2)
}
