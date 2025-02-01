package com.challenge.prewave.prewave_challenge.services

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.repositories.PersistentEdgeRepository
import com.challenge.prewave.prewave_challenge.api.errors.EdgeAlreadyExistsException
import com.challenge.prewave.prewave_challenge.api.errors.EdgeDoesNotExistException
import com.challenge.prewave.prewave_challenge.api.errors.SourceAndDestinationNodesSameException
import com.challenge.prewave.prewave_challenge.api.errors.UnexpectedException
import com.challenge.prewave.prewave_challenge.models.Edge
import com.challenge.prewave.prewave_challenge.repositories.EdgeRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class TreeServiceTest(@Autowired edgeRepository: PersistentEdgeRepository): BaseTest() {

    val treeService = TreeService(edgeRepository = edgeRepository)

    @Test
    fun `connectNodes method returns edge between two given nodes`() {
        val result = treeService.connectNodes(1, 2)
        val expectedResult = Edge(1, 2)
        assertEquals(result, expectedResult)
    }

    @Test
    fun `connectNodes raises an error when trying to connectNodes from node to itself`() {
        assertThrows<SourceAndDestinationNodesSameException> { treeService.connectNodes(1, 1) }
    }

    @Test
    fun `connectNodes raises an error if connection between two nodes already exists`() {
        treeService.connectNodes(1, 2)
        assertThrows<EdgeAlreadyExistsException> { treeService.connectNodes(1, 2) }
    }

    @Test
    fun `connectNodes raises an exception if there is a problem with database`() {
        val treeService = TreeService(edgeRepository = BrokenEdgeRepository())
        assertThrows<UnexpectedException> { treeService.connectNodes(1, 2) }
    }

    @Test
    fun `disconnectNodes removes the given edge`() {
        treeService.connectNodes(1, 2)
        treeService.disconnectNodes(1,2)
        val connectedNodes = treeService.getTree(1)
        assertEquals(emptyMap(), connectedNodes)
    }

    @Test
    fun `disconnectNodes raises an exception if edge does not exist`() {
        assertThrows<EdgeDoesNotExistException> { treeService.disconnectNodes(1,2) }
    }

    @Test
    fun `disconnectNodes raises an exception if there is a problem with database`() {
        val treeService = TreeService(edgeRepository = BrokenEdgeRepository())
        assertThrows<UnexpectedException> { treeService.disconnectNodes(1,2) }
    }

    @Test
    fun `getTree returns the whole tree when given node is root`() {
        treeService.connectNodes(1, 2)
        treeService.connectNodes(1, 3)
        treeService.connectNodes(2, 4)
        treeService.connectNodes(2, 5)
        treeService.connectNodes(3, 6)
        treeService.connectNodes(10, 11)
        val connectedNodes = treeService.getTree(rootNodeId = 1);
        val expectedResult = mapOf(1 to listOf(2, 3), 2 to listOf(4, 5), 3 to listOf(6))
        assertEquals(expectedResult, connectedNodes)
    }

    @Test
    fun `getTree returns the subtree when given node is not root`() {
        treeService.connectNodes(1, 2)
        treeService.connectNodes(1, 3)
        treeService.connectNodes(2, 4)
        treeService.connectNodes(2, 5)
        treeService.connectNodes(3, 6)
        treeService.connectNodes(10, 11)
        val connectedNodes = treeService.getTree(rootNodeId = 2);
        val expectedResult = mapOf(2 to listOf(4, 5))
        assertEquals(expectedResult, connectedNodes)
    }

    @Test
    fun `getTree returns empty map if given node has no connections`() {
        val connectedNodes = treeService.getTree(rootNodeId = 1);
        val expectedResult = emptyMap<Int, List<Int>>()
        assertEquals(expectedResult, connectedNodes)
    }

    @Test
    fun `getTree raises an exception if there is a problem with database`() {
        val treeService = TreeService(edgeRepository = BrokenEdgeRepository())
        assertThrows<UnexpectedException> { treeService.getTree(1) }
    }

    class BrokenEdgeRepository: EdgeRepository {
        override fun create(fromId: Int, toId: Int) {
            throw RuntimeException()
        }

        override fun delete(fromId: Int, toId: Int): Boolean {
            throw RuntimeException()
        }

        override fun findByFromIds(ids: Collection<Int>): Map<Int, List<Int>> {
            throw RuntimeException()
        }

    }

}
