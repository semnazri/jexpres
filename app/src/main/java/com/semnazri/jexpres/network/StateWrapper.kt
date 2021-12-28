package com.semnazri.jexpres.network

open class StateWrapper<out T>(private val content: T) {

    private var hasBeenHandled = false
    fun getEventIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent(): T = content
}