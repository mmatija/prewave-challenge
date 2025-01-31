package com.challenge.prewave.prewave_challenge.api

import com.challenge.prewave.prewave_challenge.BaseTest
import com.challenge.prewave.prewave_challenge.EdgeService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GetTreeTests(@Autowired val mockMvc: MockMvc) : BaseTest() {

    @Autowired
    lateinit var edgeService: EdgeService

    @Test
    fun `Returns status code 200`() {
        mockMvc.get("/api/v1/tree?rootNode=1").andExpect { status { isOk() } }
    }

    @Test
    fun `Returns correct response body`() {
        edgeService.createEdge(1, 2)
        edgeService.createEdge(1, 3)
        edgeService.createEdge(2, 4)
        edgeService.createEdge(2, 5)
        edgeService.createEdge(3, 6)
        val rootNode = 1
        val expectedResponseBody = """
            {
                "rootNode": $rootNode,
                "connections": {
                    "$rootNode": [2, 3],
                    "2": [4, 5],
                    "3": [6]
                }
            }
        """
        mockMvc.get("/api/v1/tree?rootNode=$rootNode").andExpect { content { json(expectedResponseBody) } }
    }

    @Test
    fun `Returns empty array when node has no connections`() {
        val rootNode = 1
        val expectedResponseBody = """
        {
            "rootNode": $rootNode,
            "connections": {
                "$rootNode": []
            }
        }
        """
        mockMvc.get("/api/v1/tree?rootNode=$rootNode").andExpect { content { json(expectedResponseBody) } }
    }

}