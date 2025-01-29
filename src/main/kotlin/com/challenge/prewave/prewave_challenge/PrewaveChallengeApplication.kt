package com.challenge.prewave.prewave_challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class PrewaveChallengeApplication

fun main(args: Array<String>) {
	runApplication<PrewaveChallengeApplication>(*args)
}
