package com.yuvahelp.app.util

import org.jsoup.Jsoup

object HtmlUtils {
    fun htmlToPlainText(html: String): String = Jsoup.parse(html).text()

    fun inferCategory(title: String, excerpt: String): String {
        val text = "${title.lowercase()} ${excerpt.lowercase()}"
        return when {
            "result" in text -> "Results"
            "admit card" in text -> "Admit Cards"
            "scheme" in text || "yojana" in text -> "Government Schemes"
            "job" in text || "vacancy" in text || "recruitment" in text -> "Latest Jobs"
            else -> "Education News"
        }
    }
}
