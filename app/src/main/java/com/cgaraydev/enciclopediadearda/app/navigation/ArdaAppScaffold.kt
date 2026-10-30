package com.cgaraydev.enciclopediadearda.app.navigation

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBackground
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBlack
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArdaAppScaffold(
    selectedDestination: NavKey?,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val closeDrawer: () -> Unit = {
        scope.launch { drawerState.close() }
    }

    val selectDestination: (NavKey) -> Unit = { destination ->
        scope.launch {
            drawerState.close()
            onNavigate(destination)
        }
    }

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        scrimColor = Color.Transparent,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerShape = RectangleShape,
                drawerContainerColor = TolkienBackground,
                drawerContentColor = TolkienWhite
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = closeDrawer) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = stringResource(
                                    R.string.close_menu
                                ),
                                tint = TolkienWhite
                            )
                        }
                    }

                    AppDrawerItem(
                        labelRes = R.string.home,
                        iconRes = R.drawable.ic_home,
                        selected = selectedDestination == HomeDestination,
                        onClick = {
                            selectDestination(HomeDestination)
                        }
                    )

                    DrawerSection(
                        titleRes = R.string.drawer_encyclopedia
                    ) {
                        AppDrawerItem(
                            labelRes = R.string.characters_title,
                            iconRes = R.drawable.ic_characters,
                            selected = selectedDestination ==
                                    CharactersDestination,
                            onClick = {
                                selectDestination(CharactersDestination)
                            }
                        )
                    }

                    DrawerSection(
                        titleRes = R.string.drawer_other
                    ) {
                        AppDrawerItem(
                            labelRes = R.string.favorite_header,
                            iconRes = R.drawable.ic_favorite_filled,
                            selected = selectedDestination == FavoritesDestination,
                            onClick = {
                                selectDestination(FavoritesDestination)
                            }
                        )

                        AppDrawerItem(
                            labelRes = R.string.calendar_menu,
                            iconRes = R.drawable.ic_calendar,
                            selected = selectedDestination == CalendarDestination,
                            onClick = {
                                selectDestination(CalendarDestination)
                            }
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = TolkienBlack,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = stringResource(
                                    R.string.open_menu
                                ),
                                tint = TolkienWhite
                            )
                        }
                    },
                    actions = {
                        if (canGoBack) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(
                                        R.drawable.ic_back
                                    ),
                                    contentDescription = stringResource(
                                        R.string.navigate_back
                                    ),
                                    tint = TolkienWhite
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TolkienBlack
                    )
                )
            },
            content = content
        )
    }

    BackHandler(enabled = drawerState.isOpen) {
        closeDrawer()
    }
}

@Composable
private fun DrawerSection(
    @StringRes titleRes: Int,
    content: @Composable () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(true) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button) {
                    expanded = !expanded
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(titleRes).lowercase(),
                style = MaterialTheme.typography.titleMedium,
                color = TolkienWhite.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_right),
                contentDescription = stringResource(
                    if (expanded) {
                        R.string.collapse_section
                    } else {
                        R.string.expand_section
                    }
                ),
                tint = TolkienWhite.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer {
                        rotationZ = if (expanded) -90f else 90f
                    }
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun AppDrawerItem(
    @StringRes labelRes: Int,
    @androidx.annotation.DrawableRes iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.titleSmall,
                color = if (selected) TolkienGold else TolkienWhite
            )
        },
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (selected) TolkienGold else TolkienWhite,
                modifier = Modifier.size(22.dp)
            )
        },
        selected = selected,
        onClick = onClick,
        shape = RectangleShape,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = TolkienGold.copy(alpha = 0.15f),
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}