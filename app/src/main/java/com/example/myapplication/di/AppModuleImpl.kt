package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.data_source.NoteDatabase
import com.example.myapplication.data.repository.NoteRepositoryImpl
import com.example.myapplication.domain.repository.NoteRepository
import com.example.myapplication.domain.use_case.AddNote
import com.example.myapplication.domain.use_case.DeleteNote
import com.example.myapplication.domain.use_case.GetNote
import com.example.myapplication.domain.use_case.GetNotes
import com.example.myapplication.domain.use_case.NoteUseCases

class AppModuleImpl(
    private val appContext: Context
): AppModule {

    private val db: NoteDatabase by lazy {
        Room.databaseBuilder(
            context = appContext,
            klass = NoteDatabase::class.java,
            name = NoteDatabase.DATABASE_NAME
        ).build()
    }

    override val noteRepository: NoteRepository by lazy {
        NoteRepositoryImpl(dao = db.noteDao)
    }

    override val noteUseCases: NoteUseCases by lazy {
        NoteUseCases(
            getNotes = GetNotes(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            addNote = AddNote(noteRepository),
            getNote = GetNote(noteRepository)
        )
    }
}
