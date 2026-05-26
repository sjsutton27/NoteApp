package com.example.myapplication.domain.use_case

import androidx.room.Index
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.repository.NoteRepository
import com.example.myapplication.domain.util.NoteOrder
import com.example.myapplication.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(orderType = OrderType.Descending)
    ): Flow<List<Note>>{
        return repository.getNotes().map{ notes ->
            when(noteOrder.orderType){
                is OrderType.Ascending ->{
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedBy { notes ->
                            notes.title.lowercase()
                        }
                        is NoteOrder.Date -> notes.sortedBy { notes ->
                            notes.timestamp
                        }
                        is NoteOrder.Color ->notes.sortedBy { notes ->
                            notes.color
                        }
                    }
                }
                is OrderType.Descending ->{
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedByDescending { notes ->
                            notes.title.lowercase()
                        }
                        is NoteOrder.Date -> notes.sortedByDescending { notes ->
                            notes.timestamp
                        }
                        is NoteOrder.Color ->notes.sortedByDescending { notes ->
                            notes.color
                        }
                    }
                }
            }
        }
    }
}