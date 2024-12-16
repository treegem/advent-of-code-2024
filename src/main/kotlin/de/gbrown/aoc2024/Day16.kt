@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix

object Day16 {

    private const val START = 'S'
    private const val END = 'E'


    fun part1(input: List<String>): Long {
        val map2d = input.to2dCharsListMatrix()
        val startingPosition = map2d.findAllPositionsByPredicate { it == START }.single()
        val endPosition = map2d.findAllPositionsByPredicate { it == END }.single()
        val allOtherNodePosition = map2d.findAllPositionsByPredicate { it == '.' }

        val startingNode = Node(startingPosition, Direction.RIGHT)
        val allNodesExceptStart = (allOtherNodePosition + endPosition).map { position ->
            Direction.nonDiagonals.map { Node(position, it) }
        }.flatten()

        val distances = allNodesExceptStart.associateWith { Long.MAX_VALUE }.toMutableMap()
        distances[startingNode] = 0L

        return shortestPath((allNodesExceptStart + startingNode), endPosition, distances)
    }

    fun part2(input: List<String>): Long = TODO("Must process $input")

    private fun shortestPath(
        nodes: List<Node>,
        endPosition: Position,
        distances: MutableMap<Node, Long>,
    ): Long {
        val unvisitedNodes = nodes.toMutableSet()

        while (unvisitedNodes.isNotEmpty()) {
            val currentNode = getNodeWithShortestDistance(unvisitedNodes, distances).also { unvisitedNodes.remove(it) }

            findAllRelevantNeighbors(currentNode, nodes).forEach { nextNode ->
                if (nextNode in unvisitedNodes) {
                    updateDistance(currentNode, nextNode, distances)
                }
            }
            if (unvisitedNodes.none { it.position == endPosition }) {
                return distances.filter { it.key.position == endPosition }.values.minOrNull()!!
            }
        }
        return -1
    }

    private fun updateDistance(
        currentNode: Node,
        nextNode: Node,
        distances: MutableMap<Node, Long>,
    ) {
        val distanceToNextNode = distances[currentNode]!! + distanceBetween(currentNode, nextNode)
        if (distanceToNextNode < distances[nextNode]!!) {
            distances[nextNode] = distanceToNextNode
        }
    }

    private fun distanceBetween(originNode: Node, targetNode: Node): Long {
        val movingDirection = Direction.nonDiagonals.single { originNode.position.moved(it) == targetNode.position }

        val distance = when (movingDirection) {
            originNode.direction -> 1L
            originNode.direction.reversed() -> 2001L
            else -> 1001L
        }

        return distance
    }

    private fun getNodeWithShortestDistance(
        unvisitedNodes: MutableSet<Node>,
        distances: MutableMap<Node, Long>,
    ) = unvisitedNodes.minByOrNull { distances[it]!! }!!
}

private fun findAllRelevantNeighbors(centerNode: Node, nodes: List<Node>): List<Node> =
    Direction.nonDiagonals
        .filterNot { it == centerNode.direction.reversed() }
        .map { Node(centerNode.position.moved(it), it) }
        .filter { it in nodes }


private data class Node(val position: Position, val direction: Direction)

fun main() {

    val day = 16

    println("\nPart 1:")
    checkOnTestInput(day, 7036L, Day16::part1, suffix = "_ex1")
    checkOnTestInput(day, 11048L, Day16::part1, suffix = "_ex2")
    solve(day, Day16::part1)

    println("\nPart2:")
    checkOnTestInput(day, 0L, Day16::part2)
    solve(day, Day16::part2)
}
