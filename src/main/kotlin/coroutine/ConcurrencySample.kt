package com.coordi.coroutine

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

/**
 * 클래스명: 동시성 테스트 샘플
 *
 * 📌 설명:
 * - 이 클래스는 Coroutine의 동시성 을 테스트 하는 용도로 만들어진 클래스 입니다.
 * - 주요 기능: Coroutine
 *
 * 🧠 학습 목적: 동시성 테스트
 * -
 *
 * 🕒 작성일: 2025-06-06
 * 👤 작성자: kimdongho
 */
val log: Logger = LoggerFactory.getLogger("ConcurrencySample")

fun main() = runBlocking {

    val treadCnt = 4
    val taskCnt = 10

    val dispatcher = Executors.newFixedThreadPool(treadCnt).asCoroutineDispatcher()
    val time = measureTimeMillis {
        val jobs = List(taskCnt) { i ->
            launch(Dispatchers.Unconfined) {
                println("\uD83D\uDE80  [${Thread.currentThread().name}] Start task [$i]")
                loadData(i)
                println("✅ [${Thread.currentThread().name}] End task [$i]")
            }
        }

        jobs.forEach {
            it.join()
        }
    }
    dispatcher.close()
    println("\n🕒 총 소요 시간: $time ms")
}

suspend fun loadData(i:Int) {
    repeat(10){ sec ->
        delay(1000)
        println("⏳ [${Thread.currentThread().name}] Waited task [$i] -> ${sec + 1} sec")
    }
}
