package com.flj.latte.web.event

import java.util.*

/**
 * @author 傅令杰
 */
class EventManager private constructor() {

    companion object {
        private val EVENTS = HashMap<String, Event>()

        val instance: EventManager
            get() = Holder.instance
    }

    private object Holder {
        internal val instance = EventManager()
    }

    fun addEvent(name: String, event: Event) {
        EVENTS[name] = event
    }

    fun getEvent(action: String): Event {
        return EVENTS[action] ?: return UndefineEvent()
    }
}
