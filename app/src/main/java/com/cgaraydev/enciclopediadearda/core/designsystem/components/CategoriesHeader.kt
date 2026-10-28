package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGolden07

@Composable
fun CategoriesHeader(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    color: Color = TolkienGolden07
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DividerRunes(
            color = color,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(textRes).lowercase(),
            style = MaterialTheme.typography.titleLarge.copy(
                letterSpacing = 1.sp,
                color = color
            ),
            maxLines = 5,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            modifier = Modifier.semantics { heading() }
        )

        DividerRunes(
            color = color,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DividerRunes(
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.divider_runes),
        fontFamily = FontFamily(Font(R.font.dwarf)),
        color = color,
        fontSize = 4.sp,
        maxLines = 1,
        modifier = modifier.padding(horizontal = 10.dp)
    )
}