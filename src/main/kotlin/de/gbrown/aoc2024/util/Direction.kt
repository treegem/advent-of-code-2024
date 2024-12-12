package de.gbrown.aoc2024.util

enum class Direction {
    UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT;

    companion object {
        val nonDiagonals = listOf(UP, RIGHT, DOWN, LEFT)
        val diagonals = listOf(UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT)
    }

}
