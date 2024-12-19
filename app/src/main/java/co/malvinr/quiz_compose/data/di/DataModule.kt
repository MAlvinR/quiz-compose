package co.malvinr.quiz_compose.data.di

import co.malvinr.quiz_compose.data.QuizRepository
import co.malvinr.quiz_compose.data.QuizRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsQuizRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ) : QuizRepository
}