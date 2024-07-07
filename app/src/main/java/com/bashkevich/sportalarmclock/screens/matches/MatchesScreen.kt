package com.bashkevich.sportalarmclock.screens.matches

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.team.domain.Team
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    modifier: Modifier = Modifier,
    viewModel: MatchesViewModel,
    onTeamsScreenClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val matches = state.matches

    val dates = state.dates

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { dates.size })

    val selectedTabIndex by remember {
        derivedStateOf { pagerState.currentPage }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { }, actions = {
                IconButton(onClick = { onTeamsScreenClick() }) {
                    Icon(Icons.Outlined.Star, contentDescription = "Go to teams list")
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 4.dp
            ) {
                dates.forEachIndexed { index, currentTab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.outline,
                        onClick = {
                            scope.launch {
                                val newIndex = dates.indexOf(currentTab)
                                pagerState.animateScrollToPage(newIndex)
                                viewModel.selectDate(currentTab)
                            }
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = currentTab.dayOfWeek.toString().substring(0,3))
                                Text(
                                    text = "${
                                        currentTab.dayOfMonth.toString().padStart(2, '0')
                                    }.${currentTab.monthNumber.toString().padStart(2, '0')}"
                                )
                            }

                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = false,
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(matches) { match ->
                        MatchItem(match = match)
                    }
                }
            }
        }


    }
}

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    match: Match
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(width = 2.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TeamItem(team = match.homeTeam)
                TeamItem(team = match.awayTeam)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${match.dateTime.time}")
                Switch(checked = match.isChecked, onCheckedChange = {})
            }
        }

    }

}

@Composable
fun TeamItem(
    modifier: Modifier = Modifier,
    team: Team
) {
    val context = LocalContext.current

    val pngImageLoader = context.imageLoader

    val svgImageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val imageLoader = if (team.logoUrl.endsWith("svg")) svgImageLoader else pngImageLoader

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(size = 2.dp)),
            contentScale = ContentScale.Inside,
            model = team.logoUrl,
            contentDescription = null,
            imageLoader = imageLoader
        )
        Text("${team.city} ${team.name}")
    }
}

@Preview
@Composable
private fun MatchItemPreview() {
    MatchItem(
        match = Match(
            id = 1,
            league = League.MLB,
            homeTeam = Team(
                id = 1,
                league = League.MLB,
                city = "Los Angeles",
                name = "Dodgers",
                logoUrl = "",
                isFavorite = false
            ),
            awayTeam = Team(
                id = 2,
                league = League.MLB,
                city = "Los Angeles",
                name = "Angels",
                logoUrl = "",
                isFavorite = false
            ),
            dateTime = LocalDateTime(2024, 7, 3, 19, 0, 0),
            isChecked = false
        )
    )
}