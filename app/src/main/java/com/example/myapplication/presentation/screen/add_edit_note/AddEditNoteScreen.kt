package com.example.myapplication.presentation.screen.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.NoteApp
import com.example.myapplication.domain.model.Note
import com.example.myapplication.presentation.components.add_edit_note.TransparentHintTextField
import com.example.myapplication.presentation.events.add_edit_note.AddEditNoteEvent
import com.example.myapplication.presentation.viewmodel.add_edit_note.AddEditNoteViewModel
import com.example.myapplication.presentation.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = viewModel(
        factory = viewModelFactory { extras ->
            AddEditNoteViewModel(
                noteUseCases = NoteApp.appModule.noteUseCases,
                savedStateHandle = extras.createSavedStateHandle()
            )
        }
    )
) {

    val titleState = viewModel.noteTitle.collectAsState().value
    val contentState = viewModel.noteContent.collectAsState().value

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val noteBackgroundAnimatable = remember {
        Animatable(
            initialValue = Color(
                if (noteColor != -1) {
                    noteColor
                } else {
                    viewModel.noteColor.value
                }
            )
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(event = AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save note"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = noteBackgroundAnimatable.value)
                .padding(paddingValues = paddingValues)
                .padding(all = 16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Note.noteColor.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(size = 50.dp)
                            .shadow(elevation = 15.dp, shape = CircleShape)
                            .clip(shape = CircleShape)
                            .background(color = color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.collectAsState().value == colorInt) {
                                    Color.Black
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(color = colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }

                                viewModel.onEvent(
                                    event = AddEditNoteEvent.ChangeColor(color = colorInt)
                                )
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {value ->
                    viewModel.onEvent(
                        event = AddEditNoteEvent.EnteredTitle(value = value)
                    )
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(
                        event = AddEditNoteEvent.ChangeTitleFocus(focusState = focusState)
                    )
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = { value ->
                    viewModel.onEvent(
                        event = AddEditNoteEvent.EnteredContent(value = value)
                    )
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(
                        event = AddEditNoteEvent.ChangeContentFocus(focusState = focusState)
                    )
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}