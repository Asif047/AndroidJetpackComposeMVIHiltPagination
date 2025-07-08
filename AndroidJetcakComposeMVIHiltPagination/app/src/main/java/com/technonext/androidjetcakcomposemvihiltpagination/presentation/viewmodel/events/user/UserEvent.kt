package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.user

sealed class UserEvent {
    object LoadUsers : UserEvent()
}