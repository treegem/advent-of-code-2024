package de.gbrown.aoc2024.util

abstract class Coordinate2D<T : Coordinate2D<T>>(open val x: Int, open val y: Int) {

    protected abstract fun createNewCoordinate(x: Int, y: Int): T

    operator fun plus(other: Coordinate2D<T>) = createNewCoordinate(x + other.x, y + other.y)

    operator fun minus(other: Coordinate2D<T>) = createNewCoordinate(x - other.x, y - other.y)

    operator fun times(scalar: Int) = createNewCoordinate(x * scalar, y * scalar)

    fun <E : Coordinate2D<E>> moved(vector: Coordinate2D<E>, steps: Int = 1): T =
        createNewCoordinate(x + vector.x * steps, y + vector.y * steps)

    fun moved(direction: Direction, steps: Int = 1): T = when (direction) {
        Direction.UP -> createNewCoordinate(x, y - steps)
        Direction.UP_RIGHT -> createNewCoordinate(x + steps, y - steps)
        Direction.RIGHT -> createNewCoordinate(x + steps, y)
        Direction.DOWN_RIGHT -> createNewCoordinate(x + steps, y + steps)
        Direction.DOWN -> createNewCoordinate(x, y + steps)
        Direction.DOWN_LEFT -> createNewCoordinate(x - steps, y + steps)
        Direction.LEFT -> createNewCoordinate(x - steps, y)
        Direction.UP_LEFT -> createNewCoordinate(x - steps, y - steps)
    }
}

data class Position(override val x: Int, override val y: Int) : Coordinate2D<Position>(x, y) {

    override fun createNewCoordinate(x: Int, y: Int) = Position(x, y)

    fun isWithinBounds(xs: IntRange, ys: IntRange) = x in xs && y in ys
}

data class Velocity(override val x: Int, override val y: Int) : Coordinate2D<Velocity>(x, y) {
    override fun createNewCoordinate(x: Int, y: Int) = Velocity(x, y)

}
