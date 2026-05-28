package com.example.myapplication.presentation.util

sealed class Screen(val route: String){
    object NotesScreen: Screen(route = "note_screen")
    object AddEditNoteScreen: Screen(route = "add_edit_note_screen")
}