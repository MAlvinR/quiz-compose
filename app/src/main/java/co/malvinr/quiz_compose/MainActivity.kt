package co.malvinr.quiz_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.malvinr.quiz_compose.data.AnswerEntity
import co.malvinr.quiz_compose.data.QuizEntity
import co.malvinr.quiz_compose.feature.TakeQuizUiState
import co.malvinr.quiz_compose.feature.TakeQuizViewModel
import co.malvinr.quiz_compose.ui.theme.AntiqueWhite
import co.malvinr.quiz_compose.ui.theme.QuizComposeTheme
import co.malvinr.quiz_compose.utils.readHtmlText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TakeQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val quizUIState by viewModel.quizState.collectAsStateWithLifecycle()
            QuizComposeTheme {
                if (quizUIState is TakeQuizUiState.Success) {
                    TakeQuizScreen(
                        quizUIState = (quizUIState as TakeQuizUiState.Success).quizzes,
                        onAnswerClick = { selectedAnswer ->
                            viewModel.chooseAnswer(selectedAnswer)
                            viewModel.setAnswerSelected()
                        },
                        currentPage = { currentPage -> viewModel.currentPage = currentPage }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TakeQuizScreen(
    quizUIState: List<QuizEntity>,
    onAnswerClick: (AnswerEntity) -> Unit,
    currentPage: (Int) -> Unit
) {
    val quizPagerState = rememberPagerState(pageCount = { quizUIState.size })
    val currentQuizPage = quizPagerState.currentPage
    val lastQuizPage = quizPagerState.pageCount - 1
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    currentPage(currentQuizPage)

    Scaffold(
        topBar = { TakeQuizAppBar() },
        bottomBar = {
            TakeQuizBottomBar(
                onPreviousClick = {
                    coroutineScope.launch {
                        quizPagerState.scrollToPage(currentQuizPage.minus(1))
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        quizPagerState.scrollToPage(currentQuizPage.plus(1))
                    }
                },
                onFinishClick = {
                    Toast.makeText(context, "akhir halaman", Toast.LENGTH_SHORT).show()
                },
                showPreviousButton = currentQuizPage > 0,
                showNextButton = currentQuizPage < lastQuizPage,
                showFinishButton = currentQuizPage == lastQuizPage,
                isAnswerSelected = quizUIState[currentQuizPage].isAnswerSelected
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        TakeQuizContent(quizUIState, quizPagerState, onAnswerClick, Modifier.padding(padding))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TakeQuizContent(
    quizzes: List<QuizEntity>,
    quizPagerState: PagerState,
    onAnswerClick: (AnswerEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Ini Ceritanya Buat Progress Bar",
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(16.dp))

        var currentProgress by remember { mutableStateOf(0f) }
        val progressAnimDuration = 300
        val progressAnimation by animateFloatAsState(
            targetValue = currentProgress,
            animationSpec = tween(
                durationMillis = progressAnimDuration,
                easing = FastOutSlowInEasing
            ),
            label = "Quiz Progress Indicator Progress Animation"
        )

        LinearProgressIndicator(
            progress = progressAnimation,
            color = Color.Black,
            modifier = Modifier
                .height(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
        )
        LaunchedEffect(quizPagerState.currentPage) {
            currentProgress = (quizPagerState.currentPage + 1).toFloat() / 10
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = quizPagerState,
            userScrollEnabled = false
        ) { page ->
            QuizItem(
                quiz = quizzes[page],
                currentPage = page + 1,
                totalPage = quizPagerState.pageCount,
                onAnswerClick = onAnswerClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeQuizAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Judul Quiz",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFE1F396),
            titleContentColor = Color(0xFF015055)
        ),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun TakeQuizBottomBar(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onFinishClick: () -> Unit,
    showPreviousButton: Boolean,
    showNextButton: Boolean,
    showFinishButton: Boolean,
    isAnswerSelected: Boolean
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showPreviousButton) {
            TextButton(
                onClick = { onPreviousClick() },
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Previous",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (showNextButton) {
            TextButton(
                onClick = { onNextClick() },
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Gray
                ),
                enabled = isAnswerSelected,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (showFinishButton) {
            TextButton(
                onClick = { onFinishClick() },
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Gray
                ),
                enabled = isAnswerSelected,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Finish",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun QuizItem(
    quiz: QuizEntity,
    currentPage: Int,
    totalPage: Int,
    onAnswerClick: (AnswerEntity) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = AntiqueWhite
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Question $currentPage of $totalPage",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            val spannedQuestionString = HtmlCompat.fromHtml(quiz.question, FROM_HTML_MODE_COMPACT)
            val annotatedQuestionString = buildAnnotatedString {
                append(spannedQuestionString.toString())
            }

            Text(
                text = annotatedQuestionString,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.primary
            )
            AnswerList(
                answers = quiz.answers,
                isAnswerSelected = quiz.isAnswerSelected,
                onAnswerClick = { selectedAnswer ->
                    onAnswerClick(selectedAnswer)
                },
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}

@Composable
fun AnswerList(
    answers: List<AnswerEntity>,
    isAnswerSelected: Boolean,
    onAnswerClick: (AnswerEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,

        ) {
        items(answers) { answer ->
            AnswerItem(
                answer = answer,
                isAnswerSelected = isAnswerSelected,
                onItemClick = { onAnswerClick(answer) }
            )
        }
    }
}

@Composable
fun AnswerItem(
    answer: AnswerEntity,
    isAnswerSelected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = when {
            answer.isCorrectlyMarked || answer.isIncorrectlyMarked -> null
            else -> BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        },
        color = when {
            answer.isCorrectlyMarked -> MaterialTheme.colorScheme.tertiary
            answer.isIncorrectlyMarked -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.surface
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (!isAnswerSelected) onItemClick()
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val answerTextColor = when {
                answer.isCorrectlyMarked || answer.isIncorrectlyMarked -> Color.White
                else -> MaterialTheme.colorScheme.primary
            }
            val answerText = answer.answer

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = answerTextColor)) {
                        append(answerText.readHtmlText())
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF015055)
@Composable
fun TakeQuizScreenPreview() {
    QuizComposeTheme {
        val quizzes = listOf(
            QuizEntity(
                question = "wawawawawa",
                answers = listOf("Lorem", "Ipsum")
                    .map { AnswerEntity(it, false) }
            ))
        TakeQuizScreen(quizzes, {}, {})
    }
}