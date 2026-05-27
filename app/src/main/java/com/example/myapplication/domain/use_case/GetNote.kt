package com.example.myapplication.domain.use_case

import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.repository.NoteRepository

class GetNote(
    private val repository : NoteRepository
) {
    suspend operator fun invoke(id: Int): Note?{
        return repository.getNoteById(id = id)
    }
}