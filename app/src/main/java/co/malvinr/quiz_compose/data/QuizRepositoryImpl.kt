package co.malvinr.quiz_compose.data

import co.malvinr.quiz_compose.network.ApiNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class QuizRepositoryImpl @Inject constructor(
    private val apiNetworkDataSource: ApiNetworkDataSource
) : QuizRepository {
    override fun getQuizResources(): Flow<List<QuizEntity>> = flow {
        val quizResponses = apiNetworkDataSource.getQuizzes().results

        val quizEntities = quizResponses.map { quiz ->
            val answers = listOf(
                AnswerEntity(quiz.correctAnswer, isCorrect = true)
            ) + quiz.incorrectAnswers.map { AnswerEntity(it, isCorrect = false) }
            QuizEntity(quiz.question, answers)
        }

        emit(quizEntities)
    }.catch { e ->
        emit(emptyList())
    }.flowOn(Dispatchers.IO)
}