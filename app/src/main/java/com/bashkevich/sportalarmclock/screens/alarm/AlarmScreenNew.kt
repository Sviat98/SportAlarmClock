package com.bashkevich.sportalarmclock.screens.alarm

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun AlarmScreenNew(
    modifier: Modifier = Modifier,
    viewModel: AlarmViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            val match = state.match

            MatchCard(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                homeTeam = match.homeTeam,
                awayTeam = match.awayTeam
            )

            AlarmLabelNew(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                text = "${match.leagueType}. ${match.seasonType.toSeasonString()}",
                textColor = Color.Black
            )

            AlarmLabel(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter),
                text = "${match.dateTime}",
                textColor =Color.Black
            )
        }
    }
}

@Composable
fun AlarmLabelNew(modifier: Modifier = Modifier, text: String, textColor: Color) {
    Text(
        text = text,
        color = textColor,
        modifier = modifier,
        fontSize = 24.sp
    )
}

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    homeTeam: Team,
    awayTeam: Team
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamCardNew(
            team = homeTeam
        )
        DismissButtonNew()
        TeamCardNew(
            team = awayTeam
        )
    }
}

@Composable
fun TeamCardNew(
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
            .size(120.dp)
            .background(color = team.primaryColor, shape = CircleShape)
            .clip(RoundedCornerShape(size = 200.dp))
    ){
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .background(color = Color.White, shape = CircleShape)
                .clip(RoundedCornerShape(size = 200.dp))
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Inside,
                model = team.logoUrl,
                contentDescription = null,
                imageLoader = imageLoader
            )
        }
    }
}

@Composable
fun DismissButtonNew(
    modifier: Modifier = Modifier
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

    Box(
        modifier = modifier
            .size(currentSize)
            .background(color = Color.Black.copy(alpha = 0.5f), shape = CircleShape),
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