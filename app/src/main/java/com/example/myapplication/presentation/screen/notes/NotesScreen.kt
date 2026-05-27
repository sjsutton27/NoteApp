package com.example.myapplication.presentation.screen.notes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.NoteApp
import com.example.myapplication.presentation.viewmodel.notes.NotesViewModel
import com.example.myapplication.presentation.viewmodel.viewModelFactory

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = viewModel(
        factory = viewModelFactory {
            NotesViewModel(
                noteUseCases = NoteApp.appModule.noteUseCases
            )
        }
    )
) {
    
}