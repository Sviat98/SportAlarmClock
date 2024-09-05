package com.bashkevich.sportalarmclock.ui.component

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.bashkevich.sportalarmclock.R
import com.bashkevich.sportalarmclock.model.league.LeagueType
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun DraggableLazyList(
    items: List<LeagueType>,
    dragAndDropListState: DragAndDropListState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = {change,offset->
                        change.consume()
                        dragAndDropListState.onDrag(offset)
                    },
                    onDragStart = { offset -> dragAndDropListState.onDragStart(offset) },
                    onDragEnd = { dragAndDropListState.onDragInterrupted()},
                    onDragCancel = { dragAndDropListState.onDragInterrupted() }
                )
            },
        state = dragAndDropListState.lazyListState
    ) {
        itemsIndexed(items){index,item->
            RearrangeItem(
                modifier = Modifier.composed {
                    val offsetOrNull =
                        dragAndDropListState.elementDisplacement.takeIf {
                            index == dragAndDropListState.currentIndexOfDraggedItem
                        }
                    Modifier.graphicsLayer {
                        translationY = offsetOrNull ?: 0f
                    }
                },
                title = item.name,
                description = "",
            )
        }
    }
}


@Composable
fun RearrangeItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Blue,
                shape = RoundedCornerShape(8.dp),
            )
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BaseIcon(
                modifier = Modifier
                    .size(32.dp),
                iconId = R.drawable.ic_rearrange_drag
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize(),
                    text = title,
                    color = Color.Blue,
                )
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 8.dp),
                    text = description,
                    color = Color.Blue
                )
            }
        }
    }
}

@Composable
fun BaseIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    tint: Color = Color.Unspecified
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    Icon(
        modifier = modifier
            .graphicsLayer {
                if (isRtl) scaleX = -1f
            },
        painter = painterResource(id = iconId),
        tint = tint,
        contentDescription = null
    )
}

data class ListItem(val id: String, val text: String)