package de.gbrown.aoc2024.util

enum class Direction {
    UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT;

    fun isVertical() = this == DOWN || this == UP

    fun reversed() = when (this) {
        UP -> DOWN
        UP_RIGHT -> DOWN_LEFT
        RIGHT -> LEFT
        DOWN_RIGHT -> UP_LEFT
        DOWN -> UP
        DOWN_LEFT -> UP_RIGHT
        LEFT -> RIGHT
        UP_LEFT -> DOWN_RIGHT
    }

    companion object {
        val nonDiagonals = listOf(UP, RIGHT, DOWN, LEFT)
        val diagonals = listOf(UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT)
    }

}
