package co.malvinr.quiz_compose.utils

import android.text.Html
import androidx.core.text.HtmlCompat

object StringUtils {
}

fun String.readHtmlText(): String =
    Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
