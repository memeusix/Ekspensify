package com.honeypot.app.ui.categories.components


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.ui.theme.Red75
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.getCategoryType
import com.honeypot.app.utils.getColor


@Composable
fun CategoryList(
    modifier: Modifier,
    categoryList: List<CategoryResponseModel>,
    onDeleteClick: (Int) -> Unit,
) {

    val lazyListState = rememberLazyListState()
    var previousListSize by remember { mutableIntStateOf(categoryList.size) }
    LaunchedEffect(categoryList) {
        if (categoryList.size > previousListSize) {
            lazyListState.animateScrollToItem(0)
        }
        previousListSize = categoryList.size
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        userScrollEnabled = true,
        flingBehavior = ScrollableDefaults.flingBehavior(),
    ) {
        items(
            categoryList,
            key = { it.id!! },
        ) { category ->
            val subtitle = remember(category.type) { category.type.getCategoryType() }
            CustomListItem(
                title = category.name.orEmpty(),
                subtitle = subtitle,
                leadingContent = {
                    CategoryIcon(category.icon, getColor(category.icFillColor).copy(alpha = 0.2f))
                },
                modifier = Modifier
                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
                    .padding(vertical = 14.dp, horizontal = 20.dp),
                enable = false,
                trailingContent = {
                    DeleteIconBtn(category, onDeleteClick)
                },
            )
            HorizontalDivider(color = MaterialTheme.extendedColors.primaryBorder)
        }
    }
}


@Composable
private fun DeleteIconBtn(
    category: CategoryResponseModel,
    onDeleteClick: (Int) -> Unit
) {
    val isEnabled = category.userId != null
    val alpha = if (isEnabled) 1f else 0.2f
    Icon(
        painter = rememberAsyncImagePainter(R.drawable.ic_delete),
        contentDescription = stringResource(R.string.delete),
        tint = Red75,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled, onClick = { onDeleteClick(category.id!!) })
            .alpha(alpha)
            .border(1.dp, MaterialTheme.extendedColors.primaryBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 7.dp, horizontal = 10.dp)
    )
}