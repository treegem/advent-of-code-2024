package de.gbrown.aoc2024.util

import java.util.PriorityQueue

abstract class Dijkstra<N> {

    abstract fun distance(from: N, to: N): Long

    abstract fun findNextNodes(currentNode: N, nodesBetween: List<N>): List<N>

    fun calculateShortestDistance(
        nodes: List<N>,
        start: N,
        end: N,
    ): Long? {

        val distances = mutableMapOf<N, Long>().withDefault { Long.MAX_VALUE }
        distances[start] = 0

        val priorityQueue = PriorityQueue<Pair<N, Long>>(compareBy { it.second })
        priorityQueue.add(start to 0)

        while (priorityQueue.isNotEmpty()) {
            val (currentNode, currentDistance) = priorityQueue.poll()
            findNextNodes(currentNode, nodes).forEach { nextNode ->
                val totalDistance = currentDistance + distance(currentNode, nextNode)
                if (totalDistance < distances.getValue(nextNode)) {
                    distances[nextNode] = totalDistance
                    priorityQueue.add(nextNode to totalDistance)
                }
            }
            if (currentNode == end) break
        }
        val result = distances[end]
        return result
    }
}
