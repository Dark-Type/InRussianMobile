package com.example.inrussian.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SuspendingQueue<T>(initialCapacity: Int = 16) {
    private val q = CircularQueue<T>(initialCapacity)
    private val mutex = Mutex()

    suspend fun offer(element: T) = mutex.withLock {
        q.offer(element)
    }

    suspend fun poll(): T? = mutex.withLock {
        q.poll()
    }
    fun pollS(): T? =  q.poll()
    suspend fun peek(): T? = mutex.withLock {
        q.peek()
    }
    fun offerSync(element: T)  {
        q.offer(element)
    }

    suspend fun size(): Int = mutex.withLock { q.size }

    suspend fun isEmpty(): Boolean = mutex.withLock { q.isEmpty }

    suspend fun clear() = mutex.withLock { q.clear() }

    suspend fun toList(): List<T> = mutex.withLock { q.toList() }

}
fun <T> queueOf() = SuspendingQueue<T>()
