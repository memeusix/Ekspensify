package com.honeypot.app.components

import androidx.compose.runtime.Composable
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

@Composable
fun PagingListLoader(loadState: CombinedLoadStates) {
    when {
        loadState.refresh is LoadState.Loading -> ShowBottomLoader()
        loadState.append is LoadState.Loading -> ShowBottomLoader()
        loadState.prepend is LoadState.Loading -> ShowBottomLoader()
        else -> Unit
    }
}
