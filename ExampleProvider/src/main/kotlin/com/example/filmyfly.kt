package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class ExampleProviderPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(FilmyflyProvider())
    }
}

class FilmyflyProvider : MainAPI() {
    override var mainUrl = "https://filmyfly.army"
    override var name = "Filmyfly"
    override var lang = "en"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val testItem = newMovieSearchResponse(
            name = "Filmyfly Official Site - South Hindi And Bollywood On filmyfly.army",
            url = "$mainUrl/",
            type = TvType.Movie
        ) {
            this.posterUrl = "https://img.iwebp.store/images/files/afaa901b76bc48d57a346319423035dd384208.png"
        }
        return newHomePageResponse(HomePageList("Latest", listOf(testItem)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return listOf(
            newMovieSearchResponse(
                name = "Filmyfly Official Site - South Hindi And Bollywood On filmyfly.army",
                url = "$mainUrl/",
                type = TvType.Movie
            ) {
                this.posterUrl = "https://img.iwebp.store/images/files/afaa901b76bc48d57a346319423035dd384208.png"
            }
        )
    }

    override suspend fun load(url: String): LoadResponse {
        return newMovieLoadResponse(
            name = "Filmyfly Official Site - South Hindi And Bollywood On filmyfly.army",
            url = url,
            type = TvType.Movie,
            dataUrl = url
        ) {
            this.posterUrl = "https://img.iwebp.store/images/files/afaa901b76bc48d57a346319423035dd384208.png"
            this.plot = "Extracted from $mainUrl"
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        
        // Generic fallback: fetch page and check for iframes
        val doc = app.get(data).document
        val iframe = doc.selectFirst("iframe")?.attr("src") ?: return false
        loadExtractor(fixUrl(iframe), referer = "$mainUrl/", subtitleCallback, callback)
        return true
        
    }
}
