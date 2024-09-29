package com.bashkevich.sportalarmclock.screens.alarm

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.season.toSeasonString
import com.bashkevich.sportalarmclock.model.team.domain.Team
import kotlinx.datetime.LocalDateTime

@Composable
fun AlarmScreen(
    modifier: Modifier = Modifier,
    viewModel: AlarmViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            val match = state.match

            HomeTeamHalf(
                modifier = Modifier.fillMaxSize(),
                homeTeam = match.homeTeam,
                leagueType = match.leagueType,
                seasonType = match.seasonType
            )
            AwayTeamHalf(
                modifier = Modifier.fillMaxSize(),
                awayTeam = match.awayTeam,
                dateTime = match.dateTime
            )

            val hasWhitePrimaryColor =
                match.homeTeam.primaryColor == Color.White || match.awayTeam.primaryColor == Color.White

            DismissButton(
                modifier = Modifier.align(alignment = Alignment.Center),
                hasWhitePrimaryColor = hasWhitePrimaryColor
            )
        }
    }
}

@Composable
fun AlarmLabel(modifier: Modifier = Modifier, text: String, textColor: Color) {
    Text(
        text = text,
        color = textColor,
        modifier = modifier,
        fontSize = 24.sp
    )
}

@Composable
fun HomeTeamHalf(
    modifier: Modifier = Modifier,
    homeTeam: Team,
    leagueType: LeagueType,
    seasonType: SeasonType
) {

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // Define the triangle path
            val topPath = Path().apply {
                lineTo(size.width, 0f)
                lineTo(0f, size.height)
                close()
            }

            // Draw the triangle path with a blue color
            drawPath(topPath, homeTeam.primaryColor)
        }
        TeamCard(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 96.dp, start = 24.dp),
            team = homeTeam
        )
        AlarmLabel(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.TopCenter),
            text = "$leagueType. ${seasonType.toSeasonString()}",
            textColor = if (homeTeam.primaryColor == Color.White) Color.Black else Color.White
        )
    }
}

@Composable
fun AwayTeamHalf(modifier: Modifier = Modifier, awayTeam: Team, dateTime: LocalDateTime) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // Draw the triangle path
            val bottomPath = Path().apply {
                moveTo(size.width, 0f)
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }

            // Draw the triangle path with a blue color
            drawPath(bottomPath, awayTeam.primaryColor)
        }
        TeamCard(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 24.dp),
            team = awayTeam
        )
        AlarmLabel(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            text = "$dateTime",
            textColor = if (awayTeam.primaryColor == Color.White) Color.Black else Color.White
        )
    }
}

@Composable
fun TeamCard(
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

    Box(
        modifier = modifier
            .size(200.dp)
            .background(color = Color.White, shape = CircleShape)
            .clip(RoundedCornerShape(size = 200.dp))
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(40.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Inside,
            model = team.logoUrl,
            contentDescription = null,
            imageLoader = imageLoader
        )
    }
}

@Composable
fun DismissButton(
    modifier: Modifier = Modifier,
    hasWhitePrimaryColor: Boolean
) {

    val transition = rememberInfiniteTransition()

    val currentSize by transition.animateValue(
        initialValue = 80.dp, targetValue = 100.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        ), label = "currentSize"
    )

    val backgroundColor =
        if (hasWhitePrimaryColor) Color.Black.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .size(currentSize)
            .background(color = backgroundColor, shape = CircleShape),
    ) {
        IconButton(
            onClick = {}, modifier = Modifier
                .align(Alignment.Center)
        ) {
            Icon(
                modifier = Modifier.size(80.dp),
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

}

@Preview
@Composable
private fun TeamCardPreview(
) {
    TeamCard(
        team = Team(
            id = 1,
            leagueType = LeagueType.NHL,
            city = "",
            name = "",
            primaryColor = Color(4291559424),
            logoUrl = "https://upload.wikimedia.org/wikipedia/en/9/9f/New_Jersey_Devils_logo.svg",
            isFavorite = false
        )
    )
}

//
//@Preview
//@Composable
//private fun AlarmScreenPreview() {
//    AlarmScreen(modifier = Modifier.fillMaxSize())
//}