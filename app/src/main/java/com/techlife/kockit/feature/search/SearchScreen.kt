package com.techlife.kockit.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.layout.SearchContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberSearchLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = rememberSearchLayoutMetrics()
    var query by remember { mutableStateOf("") }
    var recentSearches by remember { mutableStateOf(SearchFakeData.recentSearches) }

    SearchContentContainer(metrics = metrics, modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground),
            verticalArrangement = Arrangement.spacedBy(metrics.sectionSpacing)
        ) {
            item(key = "top_bar") {
                Spacer(modifier = Modifier.height(metrics.topInset))
                SearchTopBar(
                    query = query,
                    onQueryChange = { query = it },
                    onBackClick = onBackClick
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item(key = "recent") {
                SearchRecentSection(
                    items = recentSearches,
                    onClearAll = { recentSearches = emptyList() },
                    onRemoveItem = { item ->
                        recentSearches = recentSearches.filterNot { it == item }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item(key = "popular") {
                Spacer(modifier = Modifier.height(4.dp))
                SearchPopularTopicsSection(topics = SearchFakeData.popularTopics)
                Spacer(modifier = Modifier.height(metrics.topInset))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
fun SearchScreenTabletPreview() {
    KocKitTheme {
        SearchScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    KocKitTheme {
        SearchScreen(onBackClick = {})
    }
}
