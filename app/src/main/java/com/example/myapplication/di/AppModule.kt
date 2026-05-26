package com.example.myapplication.di

import com.example.myapplication.domain.repository.NoteRepository
import com.example.myapplication.domain.use_case.NoteUseCases

interface AppModule {
    val noteRepository: NoteRepository
    val noteUseCases: NoteUseCases
}