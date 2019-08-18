package com.example.event

import net.ntworld.foundation.FakedData
import net.ntworld.foundation.eventSourcing.Event
import net.ntworld.foundation.eventSourcing.EventSourcing
import net.ntworld.foundation.eventSourcing.EventSourcing.Encrypted
import net.ntworld.foundation.eventSourcing.EventSourcing.Metadata

@EventSourcing(type = "todo:updated", variant = 0)
data class TodoUpdatedEvent(

    val id: String,

    @Metadata
    val companyId: String,

    @Encrypted(faked = FakedData.GameOfThrones.character)
    val task: String

) : Event
