package de.gbrown.aoc2024.util

fun <T> Iterable<T>.withoutItemAt(index: Int): List<T> = filterIndexed { i, _ -> i != index }

fun <T> List<T>.swappedByValue(value1: T, value2: T): List<T> {
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

fun <T> List<List<T>>.findValueInDirection(
    startingPosition: Position,
    direction: Direction,
    steps: Int = 1,
): T? = findValueAt(startingPosition.moved(direction, steps))

