package com.example.myapplication.data.data_source

import androidx.room.Database
import com.example.myapplication.domain.model.Note


@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase {
    abstract val noteDao: NoteDao
}