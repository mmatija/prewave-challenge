package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.models.Edge
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CreateEdgeTests(@Autowired val restTemplate: TestRestTemplate) {


    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Returns status code 200`() {

        mockMvc.post("/api/v1/edges") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

    }
}