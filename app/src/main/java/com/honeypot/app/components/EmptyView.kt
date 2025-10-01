package com.honeypot.app.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.ui.theme.EmptyGrey

@Composable
fun EmptyView(
    title: String,
    description: String,
    @DrawableRes image: Int? = R.drawable.ic_empty_list,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        image?.let {
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = "empty",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = EmptyGrey,
            )
        )
        Text(
            description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = EmptyGrey
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}
