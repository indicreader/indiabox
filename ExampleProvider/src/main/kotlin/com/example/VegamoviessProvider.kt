package com.lagradost.cloudstream3.plugins

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class VegamoviessProvider : MainAPI() {
    override var mainUrl = "https://vegamoviess.io"
    override var name = "Vegamoviess"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val document = app.get(mainUrl).document
        // TODO: Replace with the CSS selector for movie cards on the homepage
        val items = document.select("div.item, article.post").mapNotNull {
            val title = it.selectFirst(".title, h2, h3")?.text()?.trim() ?: return@mapNotNull null
            val href = fixUrlNull(it.selectFirst("a")?.attr("href")) ?: return@mapNotNull null
            val posterUrl = fixUrlNull(it.selectFirst("img")?.attr("src"))
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
            }
        }
        return newHomePageResponse(listOf(HomePageList("Latest", items)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchUrl = "$mainUrl/?s=$query"
        val document = app.get(searchUrl).document
        return document.select("div.item, article.post").mapNotNull {
            val title = it.selectFirst(".title, h2, h3")?.text()?.trim() ?: return@mapNotNull null
            val href = fixUrlNull(it.selectFirst("a")?.attr("href")) ?: return@mapNotNull null
            val posterUrl = fixUrlNull(it.selectFirst("img")?.attr("src"))
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document
        val title = document.selectFirst("h1, .entry-title")?.text()?.trim() ?: "Vegamovies | Download Bollywood And South Indian Hindi Dubbed Movies For Free , 9xmovies, Katmoviehd,Filmyzilla"
        val poster = fixUrlNull(document.selectFirst("meta[property='og:image']")?.attr("content"))
        val plot = document.selectFirst("meta[name='description']")?.attr("content")

        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
            this.plot = plot
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        
        // Extract embed frame from page:
        val document = app.get(data).document
        val iframe = fixUrlNull(document.selectFirst("iframe")?.attr("src")) ?: return false
        loadExtractor(iframe, referer = "$mainUrl/", subtitleCallback, callback)
        return true
        
    }
}
