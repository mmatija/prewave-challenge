package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.tables.Edge.Companion.EDGE
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BaseTest {

    @Autowired
    lateinit var dslContext: DSLContext

    @AfterEach
    fun cleanUp() {
        dslContext.delete(EDGE).execute()
    }

}