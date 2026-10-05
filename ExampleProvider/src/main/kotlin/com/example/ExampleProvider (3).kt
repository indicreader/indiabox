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
        registerMainAPI(MymovieboxproappProvider())
    }
}

class MymovieboxproappProvider : MainAPI() {
    override var mainUrl = "https://mymovieboxproapp.com"
    override var name = "Mymovieboxproapp"
    override var lang = "en"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val testItem = newMovieSearchResponse(
            name = "MovieBox APK Download For Android Free | Latest V4.0 2026",
            url = "$mainUrl/",
            type = TvType.Movie
        ) {
            this.posterUrl = ""
        }
        return newHomePageResponse(HomePageList("Latest", listOf(testItem)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return listOf(
            newMovieSearchResponse(
                name = "MovieBox APK Download For Android Free | Latest V4.0 2026",
                url = "$mainUrl/",
                type = TvType.Movie
            ) {
                this.posterUrl = ""
            }
        )
    }

    override suspend fun load(url: String): LoadResponse {
        return newMovieLoadResponse(
            name = "MovieBox APK Download For Android Free | Latest V4.0 2026",
            url = url,
            type = TvType.Movie,
            dataUrl = url
        ) {
            this.posterUrl = ""
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
