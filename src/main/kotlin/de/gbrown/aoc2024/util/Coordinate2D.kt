package de.gbrown.aoc2024.util

abstract class Coordinate2D(open val x: Int, open val y: Int) {

    fun moved(direction: Direction, steps: Int = 1): Position = when (direction) {
        Direction.UP -> Position(x, y - steps)
        Direction.UP_RIGHT -> Position(x + steps, y - steps)
        Direction.RIGHT -> Position(x + steps, y)
        Direction.DOWN_RIGHT -> Position(x + steps, y + steps)
        Direction.DOWN -> Position(x, y + steps)
        Direction.DOWN_LEFT -> Position(x - steps, y + steps)
        Direction.LEFT -> Position(x - steps, y)
        Direction.UP_LEFT -> Position(x - steps, y - steps)
    }

    fun moved(vector: Coordinate2D, steps: Int = 1): Position = Position(x + vector.x * steps, y + vector.y * steps)

    operator fun plus(other: Position) = Position(x + other.x, y + other.y)

    operator fun minus(other: Position) = Position(x - other.x, y - other.y)

    operator fun times(scalar: Int) = Position(x * scalar, y * scalar)
}

data class Position(override val x: Int, override val y: Int) : Coordinate2D(x, y) {

    fun isWithinBounds(xs: IntRange, ys: IntRange) = x in xs && y in ys
}

data class Velocity(override val x: Int, override val y: Int) : Coordinate2D(x, y)
