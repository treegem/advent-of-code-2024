@file:Suppress("MagicNumber")

package de.gbrown.aoc2024

import de.gbrown.aoc2024.util.Direction
import de.gbrown.aoc2024.util.Position
import de.gbrown.aoc2024.util.checkOnTestInput
import de.gbrown.aoc2024.util.findAllPositionsByPredicate
import de.gbrown.aoc2024.util.solve
import de.gbrown.aoc2024.util.to2dCharsListMatrix
import java.util.PriorityQueue

object Day16 {

    private const val START = 'S'
    private const val END = 'E'


    fun part1(input: List<String>): Long {
        val (startingPosition, endPosition, allOtherNodePosition) = findAllNodePositions(input)

        val startingNode = Node(startingPosition, Direction.RIGHT)
        val allNodesExceptStart = createAllNodesExceptStart(allOtherNodePosition, endPosition)

        val distances = allNodesExceptStart.associateWith { Long.MAX_VALUE }.toMutableMap()
        distances[startingNode] = 0L

        return shortestPath((allNodesExceptStart + startingNode), endPosition, distances)
    }

    fun part2(input: List<String>): Long {
        val (startingPosition, endPosition, allOtherNodePosition) = findAllNodePositions(input)

        val startingNode = Node(startingPosition, Direction.RIGHT)
        val allNodesExceptStart = createAllNodesExceptStart(allOtherNodePosition, endPosition)

        val distances = allNodesExceptStart.associateWith { Long.MAX_VALUE }.toMutableMap()
        distances[startingNode] = 0L

        val shortestPaths = findAllShortestPaths((allNodesExceptStart + startingNode), endPosition, distances)
        return shortestPaths.map { it.position }.toSet().size.toLong()
    }

    private fun createAllNodesExceptStart(
        allOtherNodePosition: List<Position>,
        endPosition: Position,
    ) = (allOtherNodePosition + endPosition).map { position ->
        Direction.nonDiagonals.map { Node(position, it) }
    }.flatten()

    private fun shortestPath(
        nodes: List<Node>,
        endPosition: Position,
        distances: MutableMap<Node, Long>,
    ): Long {
        val unvisitedNodes = PriorityQueue(compareBy<Node> { distances[it]!! })
        unvisitedNodes.addAll(nodes)

        while (unvisitedNodes.isNotEmpty()) {
            val currentNode = unvisitedNodes.poll()

            findAllRelevantNeighbors(currentNode, nodes).forEach { nextNode ->
                if (nextNode in unvisitedNodes) {
                    val oldDistance = distances[nextNode]!!
                    updateDistance(currentNode, nextNode, distances)
                    if (distances[nextNode]!! < oldDistance) {
                        unvisitedNodes.remove(nextNode)
                        unvisitedNodes.add(nextNode)
                    }
                }
            }
            if (currentNode.position == endPosition) {
                return distances[currentNode]!!
            }
        }
        return -1
    }

    private fun findAllShortestPaths(
        nodes: List<Node>,
        endPosition: Position,
        distances: MutableMap<Node, Long>,
    ): List<Node> {
        val unvisitedNodes = PriorityQueue(compareBy<Node> { distances[it]!! })
        unvisitedNodes.addAll(nodes)
        val predecessors: MutableMap<Node, List<Node>> = mutableMapOf()

        while (unvisitedNodes.isNotEmpty()) {
            val currentNode = unvisitedNodes.poll()

            findAllRelevantNeighbors(currentNode, nodes).forEach { nextNode ->
                if (nextNode in unvisitedNodes) {
                    val oldDistance = distances[nextNode]!!
                    updateDistanceAndPredecessors(currentNode, nextNode, distances, predecessors)
                    if (distances[nextNode]!! < oldDistance) {
                        unvisitedNodes.remove(nextNode)
                        unvisitedNodes.add(nextNode)
                    }
                }
            }
            if (currentNode.position == endPosition) {
                return extractPaths(predecessors, currentNode)
            }
        }
        error("Algorithm failed")
    }

    private fun extractPaths(
        predecessors: MutableMap<Node, List<Node>>,
        endNode: Node,
    ): List<Node> {
        val path = mutableListOf(endNode)
        addAllPredecessorsToPath(endNode, predecessors, path)
        return path
    }

    private fun addAllPredecessorsToPath(
        currentNode: Node,
        predecessors: MutableMap<Node, List<Node>>,
        path: MutableList<Node>,
    ) {
        val currentPredecessors = predecessors[currentNode]
        if (currentPredecessors != null) {
            path.addAll(currentPredecessors)
            currentPredecessors.forEach { addAllPredecessorsToPath(it, predecessors, path) }
        }
    }

    private fun updateDistanceAndPredecessors(
        currentNode: Node,
        nextNode: Node,
        distances: MutableMap<Node, Long>,
        predecessors: MutableMap<Node, List<Node>>,
    ) {
        val distanceToNextNode = distances[currentNode]!! + distanceBetween(currentNode, nextNode)
        if (distanceToNextNode == distances[nextNode]) {
            predecessors[nextNode] = predecessors[nextNode]!! + currentNode
        }
        if (distanceToNextNode < distances[nextNode]!!) {
            distances[nextNode] = distanceToNextNode
            predecessors[nextNode] = listOf(currentNode)
        }
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

    private fun findAllNodePositions(input: List<String>): Triple<Position, Position, List<Position>> {
        val map2d = input.to2dCharsListMatrix()
        val startingPosition = map2d.findAllPositionsByPredicate { it == START }.single()
        val endPosition = map2d.findAllPositionsByPredicate { it == END }.single()
        val allOtherNodePosition = map2d.findAllPositionsByPredicate { it == '.' }

        return Triple(startingPosition, endPosition, allOtherNodePosition)
    }
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
    checkOnTestInput(day, 45L, Day16::part2, suffix = "_ex1")
    checkOnTestInput(day, 64L, Day16::part2, suffix = "_ex2")
    solve(day, Day16::part2)
}
