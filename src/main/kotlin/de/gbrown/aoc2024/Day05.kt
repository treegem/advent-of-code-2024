@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.swapped

object Day05 {

    fun part1(input: List<String>): Int {

        val rules = extractRules(input)

        val pages = extractPrintPages(input)

        return pages
            .filterNot { page -> rules.any { it.isBrokenBy(page) } }
            .sumOf { it.middleNumber }
    }

    fun part2(input: List<String>): Int {

        val rules = extractRules(input)

        val pages = extractPrintPages(input)

        return pages
            .filter { page -> rules.any { it.isBrokenBy(page) } }
            .map { page -> rearrange(page, rules) }
            .sumOf { it.middleNumber }
    }

    private fun extractRules(input: List<String>) = input
        .filter { it.contains('|') }
        .map { Rule.from(it) }

    private fun extractPrintPages(input: List<String>) = input
        .filter { it.contains(',') }
        .map { PrintPage.from(it) }

    private fun rearrange(page: PrintPage, rules: List<Rule>): PrintPage {
        var newPage = page
        while (rules.any { it.isBrokenBy(newPage) }) {
            rules.forEach { rule ->
                if (rule.isBrokenBy(newPage)) {
                    newPage = PrintPage(newPage.numbers.swapped(rule.left, rule.right))
                }
            }
        }
        return newPage
    }
}

data class Rule(val left: Int, val right: Int) {

    fun isBrokenBy(page: PrintPage): Boolean =
        page.numbers.contains(left) && page.numbers.contains(right)
                && page.numbers.indexOf(left) > page.numbers.indexOf(right)

    companion object {
        fun from(line: String): Rule {
            val (left, right) = line.split("|").map { it.toInt() }
            return Rule(left, right)
        }
    }
}

data class PrintPage(val numbers: List<Int>) {

    val middleNumber get() = numbers[numbers.size / 2]

    companion object {
        fun from(line: String): PrintPage {
            val numbers = line.split(",").map { it.toInt() }
            return PrintPage(numbers)
        }
    }
}

fun main() {

    val day = 5

    println("\nPart 1:")
    checkOnTestInput(day, 143, Day05::part1)
    solve(day, Day05::part1)

    println("\nPart2:")
    checkOnTestInput(day, 123, Day05::part2)
    solve(day, Day05::part2)
}
