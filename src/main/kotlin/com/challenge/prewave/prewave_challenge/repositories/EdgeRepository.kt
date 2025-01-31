package com.challenge.prewave.prewave_challenge.repositories

interface EdgeRepository {
    fun create(fromId: Int, toId: Int)
    fun delete(fromId: Int, toId: Int): Boolean
    fun findByFromIds(ids: Collection<Int>): Map<Int, List<Int>>

}