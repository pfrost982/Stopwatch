package com.gb.stopwatch.model.time

interface TimestampProvider {
    fun getMilliseconds() : Long
}