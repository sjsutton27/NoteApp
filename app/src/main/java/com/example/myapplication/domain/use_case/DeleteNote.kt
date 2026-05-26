package com.example.myapplication.domain.use_case

import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.deleteNote(note = note)
    }
}