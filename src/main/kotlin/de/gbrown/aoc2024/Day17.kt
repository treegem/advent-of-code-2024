@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.solve
import kotlin.math.pow

object Day17 {

    fun part1(input: List<String>): String {
        val registerA = readRegister('A', input)
        val registerB = readRegister('B', input)
        val registerC = readRegister('C', input)
        val program = input.last().substringAfter(": ").split(",").map(String::toInt)

        return Computer(registerA, registerB, registerC, program)
            .executeProgram()
            .output
    }

    fun part2(input: List<String>): Long {
        val registerB = readRegister('B', input)
        val registerC = readRegister('C', input)
        val program = input.last().substringAfter(": ").split(",").map(String::toInt)

        var registerA = 0L
        var matchedLastOutputs = 0
        var computer = Computer(registerA, registerB, registerC, program)
        while (computer.executeProgram().output != computer.program.joinToString(",")) {
            if (computer.output == computer.program.takeLast(matchedLastOutputs + 1).joinToString(",")) {
                matchedLastOutputs++
                registerA *= 8
            } else {
                registerA++
            }
            computer = Computer(registerA, registerB, registerC, program)
        }
        return registerA
    }

    private fun readRegister(register: Char, input: List<String>): Long {
        val registerLine = input.first { it.startsWith("Register $register") }
        return registerLine.substringAfter(": ").toLong()
    }
}

private class Computer(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    val program: List<Int>,
) {
    var output = ""
    var instructionPointer = 0

    fun executeProgram(): Computer {
        while (instructionPointer < program.size) {
            executeNextInstruction()
        }
        return this
    }

    fun executeWhileOutputMatchesProgram(): Computer {
        val programString = program.joinToString(",")
        while (instructionPointer < program.size && programString.startsWith(output)) {
            executeNextInstruction()
        }
        return this
    }

    private fun executeNextInstruction() {
        val nexInstruction = getNextInstruction()
        val operation = when (nexInstruction) {
            Instruction.ADV -> ::executeAdv
            Instruction.BXL -> ::executeBxl
            Instruction.BST -> ::executeBst
            Instruction.JNZ -> ::executeJnz
            Instruction.BXC -> ::executeBxc
            Instruction.OUT -> ::executeOut
            Instruction.BDV -> ::executeBdv
            Instruction.CDV -> ::executeCdv
        }
        val nextOperand = getNextOperand()
        operation(nextOperand)
    }

    private fun getNextInstruction() = Instruction.from(program[instructionPointer++])

    private fun getNextOperand() = program[instructionPointer++]

    private fun executeAdv(operand: Int) {
        val commonDivision = commonDivision(operand)
        registerA = commonDivision
    }

    private fun executeBxl(operand: Int) {
        registerB = operand.toLong() xor registerB
    }

    private fun executeBst(operand: Int) {
        val combo = combo(operand)
        registerB = combo % 8
    }

    private fun executeJnz(operand: Int) {
        if (registerA == 0L) return
        instructionPointer = operand
    }

    private fun executeBxc(operand: Int) {
        registerB = registerC xor registerB
    }

    private fun executeOut(operand: Int) {
        val combo = combo(operand)
        addToOutput(combo % 8)
    }

    private fun executeBdv(operand: Int) {
        val commonDivision = commonDivision(operand)
        registerB = commonDivision
    }

    private fun executeCdv(operand: Int) {
        val commonDivision = commonDivision(operand)
        registerC = commonDivision
    }

    private fun commonDivision(operand: Int): Long {
        val numerator = registerA
        val denominator = 2.0.pow(combo(operand).toDouble()).toInt()
        return numerator / denominator
    }

    private fun addToOutput(value: Long) {
        output = if (output.isBlank()) {
            value.toString()
        } else {
            listOf(output, value.toString()).joinToString(",")
        }
    }

    private fun combo(operand: Int): Long = when (operand) {
        0, 1, 2, 3 -> operand.toLong()
        4 -> registerA
        5 -> registerB
        6 -> registerC
        else -> error("Invalid combo: $this")
    }
}

private enum class Instruction(val opCode: Int) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7);

    companion object {
        fun from(opCode: Int): Instruction = entries.first { it.opCode == opCode }
    }
}

fun main() {

    val day = 17

    println("Computer Tests:\n")
    Computer(0, 0, 9, listOf(2, 6))
        .executeProgram()
        .let { check(it.registerB == 1L) { it.registerB } }
    Computer(10, 0, 0, listOf(5, 0, 5, 1, 5, 4))
        .executeProgram()
        .let { check(it.output == "0,1,2") { it.output } }
    Computer(2024, 0, 0, listOf(0, 1, 5, 4, 3, 0))
        .executeProgram()
        .let {
            check(it.output == "4,2,5,6,7,7,7,7,3,1,0") { it.output }
            check(it.registerA == 0L) { it.registerA }
        }
    Computer(0, 29, 0, listOf(1, 7))
        .executeProgram()
        .let { check(it.registerB == 26L) { it.registerB } }
    Computer(0, 2024, 43690, listOf(4, 0))
        .executeProgram()
        .let { check(it.registerB == 44354L) { it.registerB } }
    println("All Computer Tests passed.")


    println("\nPart 1:")
    checkOnTestInput(day, "4,6,3,5,6,3,5,2,1,0", Day17::part1)
    solve(day, Day17::part1)

    println("\nPart2:")
    checkOnTestInput(day, 117440L, Day17::part2, suffix = "_part2")
    solve(day, Day17::part2)
}
