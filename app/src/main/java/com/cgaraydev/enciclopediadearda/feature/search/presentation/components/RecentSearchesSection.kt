package com.cgaraydev.enciclopediadearda.feature.search.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.LightGray
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray08
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun RecentSearchesSection(
    entries: List<String>,
    isClearing: Boolean,
    updateFailed: Boolean,
    onSearchClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item(key = "history-heading") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.recent_searches),
                    color = TolkienWhite,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { heading() }
                )

                IconButton(
                    onClick = onClearHistory,
                    enabled = !isClearing
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(
                            R.string.clear_search_history
                        ),
                        tint = TolkienGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (updateFailed) {
            item(key = "history-update-error") {
                Text(
                    text = stringResource(
                        R.string.search_history_update_error
                    ),
                    color = TolkienWhite,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        items(
            items = entries,
            key = { it }
        ) { term ->
            Card(
                onClick = { onSearchClick(term) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = TolkienGray08
                ),
                border = BorderStroke(
                    1.dp,
                    TolkienGold.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = TolkienGold,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = term,
                        color = TolkienWhite,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_right),
                            contentDescription = null,
                            tint = LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}