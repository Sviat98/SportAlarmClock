package com.bashkevich.sportalarmclock.screens.teams

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.ui.component.BasicHeader

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = "Teams") }, navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Go to teams list")
                }
            })
        }
    ) { innerPadding ->

        val teams = state.teams

        val teamsByLeagueType = teams.groupBy { it.leagueType }.toSortedMap(compareBy {
            when (it) {
                LeagueType.NHL -> 0
                LeagueType.NBA -> 1
                LeagueType.MLB -> 2
                LeagueType.NFL -> 3
            }
        })

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            teamsByLeagueType.forEach { (league, teamsInLeague) ->
                stickyHeader {
                    BasicHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                        text = "$league"
                    )
                }
                items(items = teamsInLeague, key = { it.id }) { team ->
                    TeamItem(
                        modifier = Modifier.fillMaxWidth(),
                        team = team,
                        onFavouriteCheck = { isFavourite ->
                            viewModel.checkFavourite(
                                team.id,
                                isFavourite
                            )
                        })
                }
            }
        }
    }
}

//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun TeamsScreenContent(
//    modifier: Modifier = Modifier,
//    state: TeamsScreenState
//) {
//
//}


@Composable
private fun TeamItem(
    modifier: Modifier = Modifier,
    team: Team,
    onFavouriteCheck: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val pngImageLoader = context.imageLoader

    val svgImageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val imageLoader = if (team.logoUrl.endsWith("svg")) svgImageLoader else pngImageLoader


    Card(
        modifier = modifier,
        //elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(width = 2.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(size = 8.dp)),
                    contentScale = ContentScale.Inside,
                    model = team.logoUrl,
                    contentDescription = null,
                    imageLoader = imageLoader
                )
                Text("${team.city} ${team.name}")
            }
            Checkbox(checked = team.isFavorite, onCheckedChange = onFavouriteCheck)
        }
    }
}

@Preview
@Composable
fun TeamItemPreview(modifier: Modifier = Modifier) {
    TeamItem(
        team = Team(
            1,
            LeagueType.NHL,
            city = "Calgary",
            name = "Flames",
            primaryColor = Color.White,
            logoUrl = "https://upload.wikimedia.org/wikipedia/en/6/61/Calgary_Flames_logo.svg",
            isFavorite = false
        ),
        onFavouriteCheck = {}
    )
}