//package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.di
//
//// 📄 di/DelegateModule.kt
//
//
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.MovieViewModelDelegate
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.MovieViewModelDelegateImpl
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.UserViewModelDelegate
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.UserViewModelDelegateImpl
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.scopes.ViewModelScoped
//
//@Module
//@InstallIn(ViewModelComponent::class)
//abstract class DelegateModule {
//
//    @Binds
//    @ViewModelScoped
//    abstract fun bindMovieViewModelDelegate(
//        movieViewModelDelegateImpl: MovieViewModelDelegateImpl
//    ): MovieViewModelDelegate
//
//    @Binds
//    @ViewModelScoped
//    abstract fun bindUserViewModelDelegate(
//        userViewModelDelegateImpl: UserViewModelDelegateImpl
//    ): UserViewModelDelegate
//}