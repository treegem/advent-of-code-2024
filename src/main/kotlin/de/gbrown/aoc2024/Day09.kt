@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.DigitMeaning.FREE
import de.gbrown.aoc2024.DigitMeaning.USED
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve

object Day09 {

    fun part1(input: List<String>): Long =
        input.first()
            .toIdsAndFreeSpace()
            .defragmentBySingleBlock()
            .checksum()

    fun part2(input: List<String>): Long =
        input.first()
            .toIdsAndFreeSpace()
            .defragmentByCompleteFile()
            .checksum()

    private fun String.toIdsAndFreeSpace(): List<Int?> {
        var meaning = USED
        val resultList = mutableListOf<Int?>()
        var fileIndex = 0

        this.forEach { char ->
            val digit = char.toString().toInt()
            when (meaning) {

                USED -> {
                    repeat(digit) { resultList.add(fileIndex) }
                    fileIndex += 1
                }

                FREE -> repeat(digit) { resultList.add(null) }
            }
            meaning = meaning.switch()
        }
        return resultList.toList()
    }

    private fun List<Int?>.defragmentBySingleBlock(): List<Int?> {
        var firstFreeIndex = this.indexOf(null)
        var lastUsedIndex = this.indexOfLast { it != null }
        val mutableList = this.toMutableList()
        while (firstFreeIndex < lastUsedIndex) {
            mutableList[firstFreeIndex] = mutableList[lastUsedIndex]
            mutableList[lastUsedIndex] = null
            firstFreeIndex = mutableList.indexOf(null)
            lastUsedIndex = mutableList.indexOfLast { it != null }
        }
        return mutableList.toList()
    }

    private fun List<Int?>.defragmentByCompleteFile(): List<Int?> {
        val (nullPositions, fileIdPositions) = scanPositions()

        val mutableList = this.toMutableList()

        val idRange = 0..this.last { it != null }!!
        idRange.reversed().forEach { fileId ->
            val (valueStart, valueLength) = fileIdPositions[fileId] ?: return@forEach
            val nullPosition = nullPositions.firstOrNull { (nullStart, nullLength) ->
                nullStart <= valueStart && nullLength >= valueLength
            } ?: return@forEach
            val (nullStart, nullLength) = nullPosition
            val newNullStart = nullStart + valueLength
            val newNullLength = nullLength - valueLength
            nullPositions[nullPositions.indexOf(nullPosition)] = newNullStart to newNullLength
            repeat(valueLength) { index ->
                mutableList[nullStart + index] = fileId
                mutableList[valueStart + index] = null
            }

        }
        return mutableList.toList()
    }

    private fun List<Int?>.scanPositions(): Pair<MutableList<Pair<Int, Int>>, MutableMap<Int, Pair<Int, Int>>> {
        val nullPositions = mutableListOf<Pair<Int, Int>>() // start, length
        val fileIdPositions = mutableMapOf<Int, Pair<Int, Int>>() // value, <start, length>
        var lastValue: Int? = null
        this.forEachIndexed { index, value ->
            if (value == null) {
                if (lastValue == null) {
                    nullPositions[nullPositions.lastIndex] = nullPositions.last().let { (start, length) ->
                        start to length + 1
                    }
                } else nullPositions.add(Pair(index, 1))
            } else {
                if (value != lastValue) {
                    fileIdPositions[value] = Pair(index, 1)
                } else {
                    fileIdPositions[value] = fileIdPositions[value]!!.let { (start, length) ->
                        start to length + 1
                    }
                }
            }
            lastValue = value
        }
        return Pair(nullPositions, fileIdPositions)
    }

    private fun List<Int?>.checksum() =
        this.foldIndexed(0L) { index, acc, value ->
            if (value == null) {
                acc
            } else {
                acc + value * index
            }
        }
}

private enum class DigitMeaning {
    FREE, USED;

    fun switch() = when (this) {
        FREE -> USED
        USED -> FREE
    }
}

fun main() {

    val day = 9

    println("\nPart 1:")
    checkOnTestInput(day, 1928L, Day09::part1)
    solve(day, Day09::part1)

    println("\nPart2:")
    checkOnTestInput(day, 2858L, Day09::part2)
    solve(day, Day09::part2)
}
