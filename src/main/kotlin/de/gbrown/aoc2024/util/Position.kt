package de.gbrown.aoc2024.util


data class Position(val x: Int, val y: Int) {

    fun isWithinBounds(xs: IntRange, ys: IntRange) = x in xs && y in ys

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

    operator fun plus(other: Position) = Position(x + other.x, y + other.y)

    operator fun minus(other: Position) = Position(x - other.x, y - other.y)
}
