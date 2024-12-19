package co.malvinr.quiz_compose.domain

import co.malvinr.quiz_compose.data.QuizEntity
import co.malvinr.quiz_compose.data.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    operator fun invoke(): Flow<List<QuizEntity>> =
        quizRepository.getQuizResources()
}