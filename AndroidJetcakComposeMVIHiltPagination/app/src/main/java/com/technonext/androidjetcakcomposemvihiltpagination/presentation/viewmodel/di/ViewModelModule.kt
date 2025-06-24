//package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.di
//
//// 📄 di/ViewModelModule.kt
//
//
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation_impl.MovieViewModelImpl
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation_impl.UserViewModelImpl
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.interfaces.MovieViewModelInterface
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.interfaces.UserViewModelInterface
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.scopes.ViewModelScoped
//
//@Module
//@InstallIn(ViewModelComponent::class)
//abstract class ViewModelModule {
//
//    @Binds
//    @ViewModelScoped
//    abstract fun bindMovieViewModelInterface(
//        movieViewModel: MovieViewModelImpl
//    ): MovieViewModelInterface
//
//    @Binds
//    @ViewModelScoped
//    abstract fun bindUserViewModelInterface(
//        userViewModel: UserViewModelImpl
//    ): UserViewModelInterface
//}