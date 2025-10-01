package com.honeypot.app.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.honeypot.app.R

// Empty List View
@Composable
fun EmptyAccountsView(
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_no_account),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.8f),
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.no_account_message),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
        )
        VerticalSpace(20.dp)
        FilledButton(
            text = stringResource(R.string.add),
            modifier = Modifier.padding(horizontal = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            textModifier = Modifier.padding(vertical = 17.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = onClick
        )
        VerticalSpace(40.dp)
    }
}
