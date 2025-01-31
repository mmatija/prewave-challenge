package com.challenge.prewave.prewave_challenge

interface EdgeRepository {
    fun create(fromId: Int, toId: Int)
    fun delete(fromId: Int, toId: Int): Boolean
    fun findByFromIds(ids: List<Int>): Map<Int, List<Int>>

}