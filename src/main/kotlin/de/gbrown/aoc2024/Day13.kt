@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.all
import kotlin.math.abs
import kotlin.math.roundToLong

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

        return machineInformation.sumOf { calculateMinimumTokens(it) }
    }

    private fun calculateMinimumTokens(machineInformation: MachineInformation): Long {
        val matrix = createMatrixFrom(machineInformation)
        val resultVector = createResultFrom(machineInformation)

        val wholeNumberSolution = matrix.findWholeNumberSolution(resultVector)

        return wholeNumberSolution.let { (a, b) -> a * 3 + b }
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

    private fun Matrix.findWholeNumberSolution(resultVector: ColumnVector): Pair<Long, Long> {
        val solutionCandidate = mk.linalg.solve(this, resultVector)
        return when {
            solutionCandidate.containsOnlyWholeNumbers() -> solutionCandidate[0].roundToLong() to solutionCandidate[1].roundToLong()
            else -> Pair(0, 0)
        }
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

    private fun NDArray<Double, D1>.containsOnlyWholeNumbers(): Boolean = this.all { abs(it.roundToLong() - it) < 1e-3 }
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
