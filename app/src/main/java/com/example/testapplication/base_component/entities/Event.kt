package com.example.testapplication.base_component.entities

/**
 * Event class wrapper
 * it main function is to tag a data notified by live data
 * so we can now if the data already handled by previous session or not
 */
class Event<T>(content: T?) {
    private val mContent: T
    private var hasBeenHandled = false

    val contentIfNotHandled: T?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            mContent
        }

    fun hasBeenHandled(): Boolean {
        return hasBeenHandled
    }

    init {
        requireNotNull(content) { "null values in Event are not allowed." }
        mContent = content
    }
}