package com.example.myapplication.presentation.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.use_case.NoteUseCases
import com.example.myapplication.presentation.events.notes.NotesEvent
import com.example.myapplication.presentation.states.notes.NotesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteUseCases: NoteUseCases
): ViewModel() {
    private val _state = MutableStateFlow<NotesState>(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    private var recentlyDeletedNote: Note? = null

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> {

            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {

                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }
}