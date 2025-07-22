package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.di

import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.data.PrivateMessageRepositoryImpl
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.PrivateMessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PrivateMessageModule {

    @Binds
    abstract fun bindPrivateMessageRepository(
        privateMessageRepositoryImpl: PrivateMessageRepositoryImpl
    ): PrivateMessageRepository
}