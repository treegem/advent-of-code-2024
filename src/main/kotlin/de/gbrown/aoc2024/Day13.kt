@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import kotlin.math.max
import kotlin.math.min

typealias Matrix = D2Array<Long>
typealias ColumnVector = NDArray<Long, D1>

object Day13 {

    fun part1(input: List<String>): Long {
        val machineInformation =
            input.filter { it.isNotBlank() }
                .chunked(3)
                .map { it.toMachineInformation() }

        return machineInformation.sumOf { calculateMinimumTokens(it) }
    }

    fun part2(input: List<String>): Long {
        val machineInformation =
            input.filter { it.isNotBlank() }
                .chunked(3)
                .map { it.toMachineInformation() }
                .map { it.withShiftedPrize() }

        return machineInformation.mapIndexed { index, it ->
            println("Machine $index")
            calculateMinimumTokens(it)
        }.sum()
    }

    private fun calculateMinimumTokens(machineInformation: MachineInformation): Long {
        val matrix = createMatrixFrom(machineInformation)
        val resultVector = createResultFrom(machineInformation)

        val solutions: List<Pair<Long, Long>> = matrix.solve(resultVector)

        return solutions.minOfOrNull { (a, b) -> a * 3 + b } ?: 0
    }

    private fun createMatrixFrom(machineInformation: MachineInformation): Matrix =
        mk.ndarray(
            mk[
                mk[machineInformation.buttonAX, machineInformation.buttonAY],
                mk[machineInformation.buttonBX, machineInformation.buttonBY]
            ]
        ).transpose()

    private fun createResultFrom(machineInformation: MachineInformation): ColumnVector =
        mk.ndarray(mk[machineInformation.prizeX, machineInformation.prizeY]).transpose()

    private fun Matrix.solve(resultVector: ColumnVector): List<Pair<Long, Long>> {
        val maxA = min(resultVector[0] / this[0, 0], resultVector[1] / this[1, 0])
        val maxB = min(resultVector[0] / this[0, 1], resultVector[1] / this[1, 1])

        val solutions = mutableListOf<Pair<Long, Long>>()

        (0..maxA).forEach { a ->
            (minB(a, resultVector, this)..maxB).forEach { b ->
                if (this.dot(mk.ndarray(mk[a, b])) == resultVector) {
                    solutions.add(a to b)
                }
            }
        }

        return solutions.toList().also {
            println(
                "best solution: ${
                    it.map { (a, b) -> listOf(a, b) to a * 3 + b }.minByOrNull { it.second }?.first
                }"
            )
        }
    }

    private fun minB(a: Long, resultVector: ColumnVector, matrix: Matrix): Long {
        val aX = matrix[0, 0] * a
        val ay = matrix[1, 0] * a

        val missingBsByX = (resultVector[0] - aX) / matrix[0, 1]
        val missingBsByY = (resultVector[1] - ay) / matrix[1, 1]

        return max(missingBsByX, missingBsByY)
    }

    private fun List<String>.toMachineInformation(): MachineInformation {

        val information = this.map { it.split(": ").last() }

        val (buttonAX, buttonAY) = information[0].split(", ").map { it.takeLast(2).toLong() }

        val (buttonBX, buttonBY) = information[1].split(", ").map { it.takeLast(2).toLong() }

        val (prizeX, prizeY) = information[2].split(", ")
            .map { pos -> pos.takeLastWhile { it != '=' } }
            .map { it.toLong() }

        return MachineInformation(buttonAX, buttonAY, buttonBX, buttonBY, prizeX, prizeY)
    }
}

private data class MachineInformation(
    val buttonAX: Long,
    val buttonAY: Long,
    val buttonBX: Long,
    val buttonBY: Long,
    val prizeX: Long,
    val prizeY: Long,
) {
    private val shift = 10_000_000_000_000L

    fun withShiftedPrize(): MachineInformation =
        MachineInformation(buttonAX, buttonAY, buttonBX, buttonBY, prizeX + shift, prizeY + shift)
}

fun main() {

    val day = 13

    println("\nPart 1:")
    checkOnTestInput(day, 480L, Day13::part1)
    solve(day, Day13::part1)

    println("\nPart2:")
    solve(day, Day13::part2)
}
