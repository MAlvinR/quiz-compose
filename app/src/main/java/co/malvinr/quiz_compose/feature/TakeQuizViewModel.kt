package co.malvinr.quiz_compose.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.malvinr.quiz_compose.data.AnswerEntity
import co.malvinr.quiz_compose.data.QuizEntity
import co.malvinr.quiz_compose.data.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TakeQuizViewModel @Inject constructor(
    quizRepository: QuizRepository
) : ViewModel() {

    val quizState: StateFlow<TakeQuizUiState> =
        quizRepository.getQuizResources()
            .map<List<QuizEntity>, TakeQuizUiState> {
                TakeQuizUiState.Success(it)
            }
            .onStart { TakeQuizUiState.Loading }
            .catch { emit(TakeQuizUiState.Error(it)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TakeQuizUiState.Loading
            )

    var currentPage by mutableStateOf(-1)

    fun selectAnswer(selectedAnswer: AnswerEntity) {
        val quiz = (quizState.value as TakeQuizUiState.Success).quizzes[currentPage]
        quiz.answers.forEach { answer ->
            answer.isSelected = answer == selectedAnswer
        }
    }


    fun isSelectedAnswerCorrect(selectedAnswer: AnswerEntity) {
        (quizState.value as TakeQuizUiState.Success).quizzes.forEach {
            it.isAnswerSelected = true
        }
    }
}

sealed interface TakeQuizUiState {
    data object Loading : TakeQuizUiState
    data class Success(val quizzes: List<QuizEntity>) : TakeQuizUiState
    data class Error(val throwable: Throwable) : TakeQuizUiState
}