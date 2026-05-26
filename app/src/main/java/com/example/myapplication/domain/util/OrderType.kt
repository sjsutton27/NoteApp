package com.example.myapplication.domain.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
