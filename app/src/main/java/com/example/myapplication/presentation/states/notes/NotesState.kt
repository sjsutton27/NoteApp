package com.example.myapplication.presentation.states.notes

import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.util.NoteOrder
import com.example.myapplication.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
