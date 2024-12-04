@file:Suppress("MagicNumber")

object Day00Template {

    fun part1(input: List<String>): Int = TODO("Must process $input")

    fun part2(input: List<String>): Int = TODO("Must process $input")
}

fun main() {

    val testInput = readInput("Day00_test")
    check(Day00Template.part1(testInput).also(::println) == 0)

    val input = readInput("Day00")
    Day00Template.part1(input).println()
    Day00Template.part2(input).println()
}
