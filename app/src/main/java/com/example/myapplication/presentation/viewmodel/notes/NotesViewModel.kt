package com.example.myapplication.presentation.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.use_case.NoteUseCases
import com.example.myapplication.domain.util.NoteOrder
import com.example.myapplication.domain.util.OrderType
import com.example.myapplication.presentation.events.notes.NotesEvent
import com.example.myapplication.presentation.states.notes.NotesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteUseCases: NoteUseCases
): ViewModel() {
    private val _state = MutableStateFlow<NotesState>(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init{
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> {
                if(state.value.noteOrder::class == event.noteOrder::class
                    && state.value.noteOrder.orderType == event.noteOrder.orderType){
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(note = recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }
    private fun getNotes(noteOrder : NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach{ notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}