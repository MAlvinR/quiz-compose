package co.malvinr.quiz_compose.data

import co.malvinr.quiz_compose.network.ApiNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class QuizRepositoryImpl @Inject constructor(
    private val apiNetworkDataSource: ApiNetworkDataSource
) : QuizRepository {

    override fun getQuizResources(): Flow<List<QuizEntity>> = flow {
        // Fetch the list of quizzes from the network
        val quizResponses = apiNetworkDataSource.getQuizzes().results

        // Map the network result (List<QuizResponse>) to a List<QuizEntity>
        val quizEntities = quizResponses.map { quiz ->
            val answers = listOf(
                AnswerEntity(quiz.correctAnswer, isCorrect = true)
            ) + quiz.incorrectAnswers.map { AnswerEntity(it, isCorrect = false) }
            QuizEntity(quiz.question, answers)
        }

        // Emit the transformed list as a Flow<List<QuizEntity>>
        emit(quizEntities)
    }.catch { e ->
        print("BABI $e")
        // Handle any exceptions by emitting an empty list or logging the error
        emit(emptyList()) // Optionally emit an empty list on error
    }.flowOn(Dispatchers.IO) // Make sure the network call is off the main thread
}