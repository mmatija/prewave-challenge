package com.challenge.prewave.prewave_challenge.api

import com.challenge.prewave.prewave_challenge.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DeleteEdgeTests(@Autowired val mockMvc: MockMvc) : BaseTest() {

    @Test
    fun `Returns status code 204 when edge is deleted`() {
        val fromNode = 1
        val toNode = 2
        createEdge(fromNode = fromNode, toNode = toNode)
        sendDeleteRequest(fromNode, toNode).andExpect { status { isNoContent() } }
    }

    @Test
    fun `Returns empty body when edge is deleted`() {
        val fromNode = 1
        val toNode = 2
        createEdge(fromNode = fromNode, toNode = toNode)
        sendDeleteRequest(fromNode, toNode).andExpect { content { string("") } }
    }

    @Test
    fun `Returns status code 422 when trying to delte edge which does not exist`() {
        sendDeleteRequest(1, 2).andExpect { status { isUnprocessableEntity() } }
    }

    @Test
    fun `Returns error message when deleting edge that does not exist`() {
        val fromNode = 1
        val toNode = 2
        val expectedErrorMessage = """{"errors": ["Edge from node $fromNode to node $toNode does not exist"]}"""
        sendDeleteRequest(fromNode, toNode).andExpect { content { json(expectedErrorMessage) } }
    }

    @Test
    fun `Returns status code 400 when fromNode parameter is missing`() {
        mockMvc.delete("/api/v1/edges?toNode=2").andExpect { status { isBadRequest() } }
    }

    @Test
    fun `Returns error message when fromNode parameter is missing`() {
        val expectedErrorMessage = """{"errors": ["fromNode must be set"]}"""
        mockMvc.delete("/api/v1/edges?toNode=2").andExpect { content { json(expectedErrorMessage) } }
    }

    @Test
    fun `Returns status code 400 when toNode parameter is missing`() {
        mockMvc.delete("/api/v1/edges?fromNode=1").andExpect { status { isBadRequest() } }
    }


    @Test
    fun `Returns error message when toNode parameter is missing`() {
        val expectedErrorMessage = """{"errors": ["toNode must be set"]}"""
        mockMvc.delete("/api/v1/edges?fromNode=1").andExpect { content { json(expectedErrorMessage) } }
    }

    fun sendDeleteRequest(fromNode: Int, toNode: Int): ResultActionsDsl {
        return mockMvc.delete("/api/v1/edges?fromNode=$fromNode&toNode=$toNode")
    }

    fun createEdge(fromNode: Int, toNode: Int): ResultActionsDsl {
        val jsonBody = """{"fromNode": ${fromNode}, "toNode": ${toNode}}"""
        return mockMvc.post("/api/v1/edges") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonBody
        }
    }

}