package com.challenge.prewave.prewave_challenge

import com.challenge.prewave.prewave_challenge.models.Edge

interface EdgeRepository {
    fun create(fromId: Int, toId: Int): Edge
    fun delete(fromId: Int, toId: Int): Boolean
    fun findByFromIds(ids: List<Int>): List<Edge>

}