package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray04

@Composable
fun CharacteristicsSection(
    labelValuePairs: List<Pair<String, String?>>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val visiblePairs = labelValuePairs.filter {
        !it.second.isNullOrBlank()
    }
    if (visiblePairs.isEmpty()) return

    Column(modifier = modifier) {
        CategoriesHeader(textRes = R.string.characteristics)

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            visiblePairs.forEach { (label, value) ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(
                            R.string.characteristic_label,
                            label
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TolkienGray04,
                        modifier = Modifier
                            .alignBy(FirstBaseline)
                            .padding(end = 6.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .alignBy(FirstBaseline)
                    ) {
                        HtmlText(
                            html = value.orEmpty(),
                            onLinkClick = onLinkClick
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}