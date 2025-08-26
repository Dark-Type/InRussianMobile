package com.example.inrussian.utils

class CircularQueue<T>(
    initialCapacity: Int = 16
) {
    init {
        require(initialCapacity > 0) { "initialCapacity must be > 0" }
    }

    private var array: Array<Any?> = arrayOfNulls(initialCapacity)
    private var head = 0
    private var tail = 0
    private var _size = 0

    val size: Int get() = _size
    val isEmpty: Boolean get() = _size == 0

    fun offer(element: T) {
        ensureCapacityForInsert()
        array[tail] = element
        tail = (tail + 1) % array.size
        _size++
    }

    fun poll(): T? {
        if (_size == 0) return null
        @Suppress("UNCHECKED_CAST")
        val res = array[head] as T
        array[head] = null
        head = (head + 1) % array.size
        _size--
        return res
    }

    fun peek(): T? {
        if (_size == 0) return null
        @Suppress("UNCHECKED_CAST")
        return array[head] as T
    }

    fun clear() {
        for (i in 0 until array.size) array[i] = null
        head = 0
        tail = 0
        _size = 0
    }

    private fun ensureCapacityForInsert() {
        if (_size < array.size) return
        val newCap = array.size * 2
        val newArray: Array<Any?> = arrayOfNulls(newCap)
        var idx = head
        for (i in 0 until _size) {
            newArray[i] = array[idx]
            idx = (idx + 1) % array.size
        }
        array = newArray
        head = 0
        tail = _size
    }

    fun toList(): List<T> {
        val list = ArrayList<T>(_size)
        var idx = head
        for (i in 0 until _size) {
            @Suppress("UNCHECKED_CAST")
            list.add(array[idx] as T)
            idx = (idx + 1) % array.size
        }
        return list
    }

    override fun toString(): String = toList().toString()
}