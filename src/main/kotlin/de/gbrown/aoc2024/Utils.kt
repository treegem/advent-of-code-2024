package de.gbrown.aoc2024

fun <T> Iterable<T>.withoutItemAt(index: Int): List<T> = filterIndexed { i, _ -> i != index }

fun <T> List<T>.swapped(value1: T, value2: T): List<T> {
    val copy = this.toMutableList()
    val value1Index = this.indexOf(value1)
    val value2Index = this.indexOf(value2)
    val temp = copy[value1Index]
    copy[value1Index] = copy[value2Index]
    copy[value2Index] = temp
    return copy.toList()
}

fun List<String>.to2dCharsListMatrix(): List<List<Char>> = map { it.toList() }

fun <T> List<List<T>>.findValueAt(position: Position): T? = getOrNull(position.y)?.getOrNull(position.x)

fun Position.move(direction: Direction, steps: Int = 1): Position = when (direction) {
    Direction.UP -> Position(x, y - steps)
    Direction.UP_RIGHT -> Position(x + steps, y - steps)
    Direction.RIGHT -> Position(x + steps, y)
    Direction.DOWN_RIGHT -> Position(x + steps, y + steps)
    Direction.DOWN -> Position(x, y + steps)
    Direction.DOWN_LEFT -> Position(x - steps, y + steps)
    Direction.LEFT -> Position(x - steps, y)
    Direction.UP_LEFT -> Position(x - steps, y - steps)
}

fun <T> List<List<T>>.findValueInDirection(
    startingPosition: Position,
    direction: Direction,
    steps: Int = 1,
): T? = findValueAt(startingPosition.move(direction, steps))

enum class Direction {
    UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT
}

data class Position(val x: Int, val y: Int)
