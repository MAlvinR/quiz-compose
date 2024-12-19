package co.malvinr.quiz_compose.network

import co.malvinr.quiz_compose.BuildConfig
import co.malvinr.quiz_compose.network.model.ListDataResponse
import co.malvinr.quiz_compose.network.model.QuizResponse
import dagger.Lazy
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private const val TRIVIA_DB_BASE_URL = BuildConfig.BASE_URL

private interface RetrofitNetworkApi {
    @GET(value = "api.php")
    suspend fun getQuizzes(
        @Query("amount") amount: Int?,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String?,
    ): ListDataResponse<List<QuizResponse>>
}

@Singleton
internal class ApiNetwork @Inject constructor(
    networkJson: Json,
    okHttpCallFactory: Lazy<Call.Factory>
) : ApiNetworkDataSource {

    private val networkApi =
        Retrofit.Builder()
            .baseUrl(TRIVIA_DB_BASE_URL)
            .callFactory { okHttpCallFactory.get().newCall(it) }
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RetrofitNetworkApi::class.java)

    override suspend fun getQuizzes(): ListDataResponse<List<QuizResponse>> {
        return networkApi.getQuizzes(
            10,
            11,
            "easy",
            "multiple"
        )
    }
}