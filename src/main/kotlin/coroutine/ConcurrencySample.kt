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
    // 코루틴 실행을 위한 블로킹 진입점 (main 함수에서 사용)

    val treadCnt = 4         // 사용할 스레드 수
    val taskCnt = 10         // 실행할 코루틴 개수 (작업 개수)

    // 고정된 스레드 풀을 생성하고, 이를 코루틴 디스패처로 변환
    val dispatcher = Executors.newFixedThreadPool(treadCnt).asCoroutineDispatcher()

    // 전체 실행 시간 측정을 위한 래퍼
    val time = measureTimeMillis {
        // taskCnt 개수만큼 코루틴 리스트 생성
        val jobs = List(taskCnt) { i ->
            /**
              * dispatcher에 설정된 Thread 실행 이 된다.
              * 설정 변경해 가며 테스트 필요
              * Dispatchers.Default
              * Dispatchers.IO
              * Dispatchers.Unconfined
              * Dispatchers.Main
             **/

            launch(dispatcher) {
                println("🚀  [${Thread.currentThread().name}] Start task [$i]")
                loadData(i)  // 실제 작업 수행 (10초간 delay)
                println("✅ [${Thread.currentThread().name}] End task [$i]")
            }
        }

        jobs.forEach {
            it.join()  // 모든 작업이 끝날 때까지 대기
        }
    }

    dispatcher.close()  // 커스텀 디스패처 자원 해제

    println("\n🕒 총 소요 시간: $time ms")  // 실행 시간 출력
}

suspend fun loadData(i:Int) {
    repeat(10){ sec ->
        delay(1000)  // 1초 대기 (비동기 중단)
        println("⏳ [${Thread.currentThread().name}] Waited task [$i] -> ${sec + 1} sec")
    }
    // 총 10초 동안 1초마다 로그 출력
}