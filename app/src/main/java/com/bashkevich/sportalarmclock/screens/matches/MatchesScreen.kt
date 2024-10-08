package com.bashkevich.sportalarmclock.screens.matches

import android.app.PendingIntent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.season.toSeasonString
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.ui.component.BasicHeader
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    modifier: Modifier = Modifier,
    viewModel: MatchesViewModel,
    onTeamsScreenClick: () -> Unit,
    onMatchClick: (Int) -> Unit,
    onSettingsScreenClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val matches = state.matches

    val matchesBySeason = matches.groupBy { Pair(it.leagueType, it.seasonType) }.toSortedMap(
        compareBy {
            val leagueTypeNumber = when (it.first) {
                LeagueType.NHL -> 1
                LeagueType.NBA -> 2
                LeagueType.MLB -> 3
                LeagueType.NFL -> 4
            }

            val seasonTypeNumber = when (it.second) {
                SeasonType.PRE -> 1
                SeasonType.REG -> 2
                SeasonType.STAR -> 3
                SeasonType.POST -> 4
                SeasonType.OFF -> 5
            }

            leagueTypeNumber * LeagueType.entries.size + seasonTypeNumber
        }
    )

    val dateItems = state.dates
    val dates = state.dates.map { it.first }

    val selectedDate = state.selectedDate

    val scope = rememberCoroutineScope()

    val selectedTabIndex =
        if (dates.indexOf(selectedDate) == -1) 0 else dates.indexOf(selectedDate)

    val pagerState = rememberPagerState(initialPage = selectedTabIndex, pageCount = { dates.size })

    SideEffect {
        Log.d("MatchesScreen current page", "$selectedTabIndex")
        Log.d("MatchesScreen selectedDate", "$selectedDate")

        Log.d("MatchesScreen state", "$state")

    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { }, actions = {
                IconButton(onClick = { onTeamsScreenClick() }) {
                    Icon(Icons.Outlined.Star, contentDescription = "Go to teams list")
                }
                IconButton(onClick = { onSettingsScreenClick() }) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Go to settings")
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
                dateItems.forEachIndexed { index, dateItem ->
                    val currentTab = dateItem.first
                    Tab(
                        selected = selectedTabIndex == index,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.outline,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                                viewModel.selectDate(currentTab)
                            }
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = if (dateItem.second) "TODAY" else currentTab.dayOfWeek.toString().substring(0, 3))
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
                when {
                    state.isLoading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    matches.isEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No scheduled matches")
                        }
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            matchesBySeason.forEach { (key, matchesList) ->
                                stickyHeader {
                                    BasicHeader(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = Color.White),
                                        text = "${key.first}. ${key.second.toSeasonString()}"
                                    )
                                }
                                items(matchesList) { match ->
                                    MatchItem(
                                        match = match,
                                        is24HourFormat = state.is24HourFormat,
                                        onMatchClick = onMatchClick,
                                        onToggleFavouriteSign = { isFavourite ->
                                            viewModel.checkFavourite(
                                                matchId = match.id,
                                                dateTime = match.dateTime,
                                                isFavourite = isFavourite
                                            )
                                        })
                                }
                            }
                        }
                    }
                }

            }
        }


    }
}

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    match: Match,
    is24HourFormat: Boolean,
    onMatchClick: (Int) -> Unit,
    onToggleFavouriteSign: (Boolean) -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onMatchClick(match.id) },
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
                val twelveHourFormat = LocalTime.Format {
                    amPmHour(padding = Padding.NONE)
                    char(':')
                    minute()
                    char(' ')
                    amPmMarker(am = "AM", pm = "PM")
                }
                Text(
                    text = "${
                        if (is24HourFormat) match.dateTime.time else match.dateTime.time.format(
                            twelveHourFormat
                        )
                    }"
                )
                Switch(
                    checked = match.isChecked,
                    onCheckedChange = onToggleFavouriteSign,
                    enabled = match.isAbleToAlarm
                )
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

//@Preview
//@Composable
//private fun MatchItemPreview() {
//    MatchItem(
//        match = Match(
//            id = 1,
//            leagueType = LeagueType.MLB,
//            seasonType = SeasonType.OFF,
//            homeTeam = Team(
//                id = 1,
//                leagueType = LeagueType.MLB,
//                city = "Los Angeles",
//                name = "Dodgers",
//                logoUrl = "",
//                isFavorite = false
//            ),
//            awayTeam = Team(
//                id = 2,
//                leagueType = LeagueType.MLB,
//                city = "Los Angeles",
//                name = "Angels",
//                logoUrl = "",
//                isFavorite = false
//            ),
//            dateTime = LocalDateTime(2024, 7, 3, 19, 0, 0),
//            isChecked = false,
//            isAbleToAlarm = true
//        ),
//        onMatchClick = {}
//    )
//}