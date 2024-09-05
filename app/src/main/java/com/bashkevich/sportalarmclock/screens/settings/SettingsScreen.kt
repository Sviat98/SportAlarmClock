package com.bashkevich.sportalarmclock.screens.settings

import android.content.ClipData
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.ui.component.DraggableLazyList
import com.bashkevich.sportalarmclock.ui.component.ListItem
import com.bashkevich.sportalarmclock.ui.component.RearrangeItem
import com.bashkevich.sportalarmclock.ui.component.move
import com.bashkevich.sportalarmclock.ui.component.rememberDragAndDropListState

val leaguesList = listOf(LeagueType.NFL,LeagueType.MLB,LeagueType.NBA,LeagueType.NHL)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val leagues = state.leagueTypes

    val teamsMode = state.teamsMode

//    val dndTarget = remember {
//        object : DragAndDropTarget {
//            override fun onDrop(event: DragAndDropEvent): Boolean {
//                val draggedData = event.toAndroidDragEvent()
//                    .clipData.getItemAt(0).text
//                urlState.value = draggedData.toString()
//                return true
//            }
//
//            override fun onEntered(event: DragAndDropEvent) {
//                super.onEntered(event)
//                tintColor = Color(0xff00ff00)
//            }
//
//            override fun onEnded(event: DragAndDropEvent) {
//                super.onEntered(event)
//                tintColor = Color(0xffE5E4E2)
//            }
//
//            override fun onExited(event: DragAndDropEvent) {
//                super.onEntered(event)
//                tintColor = Color(0xffE5E4E2)
//            }
//
//        }
//    }

    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = "Settings") }, navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Go to teams list")
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            var menuExpanded by remember {
                mutableStateOf(false)
            }
            Box {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Show matches of teams:")
                    Text(text = teamsMode.name.lowercase())
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "Choose Teams Mode"
                        )
                    }
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    TeamsMode.entries.forEach { teamMode ->
                        DropdownMenuItem(
                            text = { Text(text = teamMode.name.lowercase()) },
                            onClick = {
                                viewModel.toggleTeamMode(teamMode)
                                menuExpanded = false
                            })
                    }
                }
            }
            Column {
                LeagueType.entries.forEach { league ->
                    Row(
                        modifier = Modifier
                            .dragAndDropSource(
                                drawDragDecoration = {

                                }
                            ) {
                                detectTapGestures(
                                    onLongPress = {
                                        startTransfer(
                                            DragAndDropTransferData(
                                                ClipData.newPlainText("league", league.name)
                                            )
                                        )
                                    }
                                )
                            }
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = {
                                    true
                                    // condition to accept dragged item
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            val draggedData = event.toAndroidDragEvent()
                                                .clipData.getItemAt(0).text
                                            return true
                                        }

                                        override fun onEntered(event: DragAndDropEvent) {
                                            super.onEntered(event)
                                        }

                                        override fun onEnded(event: DragAndDropEvent) {
                                            super.onEntered(event)
                                        }

                                        override fun onExited(event: DragAndDropEvent) {
                                            super.onEntered(event)
                                        }

                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = league in leagues, onCheckedChange = { checked ->
                            viewModel.toggleLeague(league, checked)
                        })
                        Text(text = league.name)
                    }
                }
            }

            val items = leaguesList.toMutableStateList()

            Log.d("SettingsScreen items outside", "${items.map { it.name }}")

            val dragAndDropListState =
                rememberDragAndDropListState(lazyListState) { from, to ->
                    Log.d("SettingsScreen move", "$from $to")
                    Log.d("SettingsScreen items size", "${items.size}")
                    items.move(from, to)
                    Log.d("SettingsScreen items inside", "${items.map { it.name }}")

                }
            DraggableLazyList(
                items = items,
                dragAndDropListState = dragAndDropListState
            )
        }
    }
}