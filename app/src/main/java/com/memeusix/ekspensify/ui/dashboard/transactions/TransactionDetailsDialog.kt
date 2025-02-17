package com.memeusix.ekspensify.ui.dashboard.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.memeusix.ekspensify.R
import com.memeusix.ekspensify.components.CustomListItem
import com.memeusix.ekspensify.components.DeleteConfirmDialog
import com.memeusix.ekspensify.components.HorizontalDashedLine
import com.memeusix.ekspensify.components.HorizontalSpace
import com.memeusix.ekspensify.components.VerticalSpace
import com.memeusix.ekspensify.data.model.responseModel.TransactionResponseModel
import com.memeusix.ekspensify.navigation.CreateTransactionScreenRoute
import com.memeusix.ekspensify.navigation.PicturePreviewScreenRoute
import com.memeusix.ekspensify.ui.acounts.components.AmountText
import com.memeusix.ekspensify.ui.acounts.data.BankModel
import com.memeusix.ekspensify.ui.theme.Green100
import com.memeusix.ekspensify.ui.theme.Red100
import com.memeusix.ekspensify.ui.theme.Red75
import com.memeusix.ekspensify.ui.theme.extendedColors
import com.memeusix.ekspensify.utils.DateFormat
import com.memeusix.ekspensify.utils.TransactionType
import com.memeusix.ekspensify.utils.formatDateTime
import com.memeusix.ekspensify.utils.getTransactionType
import com.memeusix.ekspensify.utils.singleClick
import com.memeusix.ekspensify.utils.toJson

@Composable
fun TransactionDetailsDialog(
    transaction: TransactionResponseModel?,
    navController: NavHostController,
    onDismiss: () -> Unit,
    onDeleteClick: (Int?) -> Unit
) {

    val showDeleteConfirm = remember { mutableStateOf(false) }
    val deleteHeight = remember { mutableStateOf(IntSize.Zero) }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
            .onSizeChanged { deleteHeight.value = it }
            .alpha(if (showDeleteConfirm.value) 0.05f else 1f))
        {
            TransactionDetailsHeader(transaction)
            AccountAndCategory(transaction)
            HorizontalDashedLine(width = 5f)
            if (!transaction?.note.isNullOrEmpty()) {
                VerticalSpace(20.dp)
                Text(
                    transaction?.note.orEmpty(),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            VerticalSpace(20.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 20.dp)
            ) {
                transaction?.attachment?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.extendedColors.imageBg)
                            .clickable { navController.navigate(PicturePreviewScreenRoute(it)) },
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Text(
                        "No Image\nAttached",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.extendedColors.imageBg)
                            .wrapContentHeight(Alignment.CenterVertically),
                    )
                }
                HorizontalSpace(20.dp)
                ActionsBtnGroup(
                    modifier = Modifier.weight(1f),
                    onDeleteClick = singleClick {
                        showDeleteConfirm.value = true
                    }, onEditClick = singleClick {
                        navController.navigate(
                            CreateTransactionScreenRoute(
                                transactionType = TransactionType.valueOf(
                                    transaction?.type ?: "CREDIT"
                                ),
                                transactionResponseModelArgs = transaction.toJson()
                            )
                        )
                    }, onCloseClick = singleClick { onDismiss() })
            }
        }

        if (showDeleteConfirm.value) {
            DeleteConfirmDialog(modifier = Modifier
                .height((deleteHeight.value.height / 2.9).dp)
                .pointerInput(Unit) {
                    detectTapGestures {}
                }, onCancel = { showDeleteConfirm.value = false }, onDelete = {
                onDeleteClick(transaction?.id)
            })
        }
    }
}


@Composable
fun TransactionDetailsHeader(
    transactionResponseModel: TransactionResponseModel?
) {
    val amountText by rememberUpdatedState(transactionResponseModel?.amount ?: 0)
    val transactionType by rememberUpdatedState(transactionResponseModel?.type?.getTransactionType())
    val color =
        remember { if (transactionResponseModel?.type == TransactionType.CREDIT.toString()) Green100 else Red100 }
    val dateText by rememberUpdatedState(transactionResponseModel?.createdAt)
    val textColor = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
    ) {
        AmountText(
            amountText.toString(), color = textColor, modifier = Modifier.padding(top = 15.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = transactionType.orEmpty(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold, color = textColor
                ),
            )
            Text(
                text = dateText.formatDateTime(DateFormat.dd_MMM_yyyy_hh_mm_a),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = textColor
                ),
            )
        }
    }
}


@Composable
private fun AccountAndCategory(
    transactionResponseModel: TransactionResponseModel?
) {
    val (categoryIcon, accountIcon) = remember(transactionResponseModel) {
        transactionResponseModel?.category?.icon to BankModel.getAccounts()
            .find { it.iconSlug == transactionResponseModel?.account?.icon }?.icon
    }

    val accountName =
        remember(transactionResponseModel?.account) { transactionResponseModel?.account?.name.orEmpty() }
    val categoryName =
        remember(transactionResponseModel?.category) { transactionResponseModel?.category?.name.orEmpty() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(IntrinsicSize.Max)
            .border(1.dp, MaterialTheme.extendedColors.primaryBorder, RoundedCornerShape(15.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = accountIcon, contentDescription = null, modifier = Modifier.size(24.dp)
        )
        VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp))
        TextWithIcon(
            icon = categoryIcon, text = categoryName, modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TextWithIcon(
    icon: Any?,
    text: String,
    modifier: Modifier,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = icon, contentDescription = null, modifier = Modifier.size(24.dp)
        )
        HorizontalSpace(8.dp)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}


@Composable
private fun AttachmentPreview(attachment: String?, onClick: () -> Unit) {
    val context = LocalContext.current
    CustomListItem(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .border(1.dp, MaterialTheme.extendedColors.primaryBorder, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .clickable {
//                context.openImageExternally(attachment)
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        leadingContent = {
            AsyncImage(
                model = attachment,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.5.dp))
            )
        },
        title = attachment?.substringAfterLast("/").orEmpty(),
        trailingContent = {
            Text(
                "View", style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        },
        enable = false,
    )
}


@Composable
private fun ActionsBtnGroup(
    modifier: Modifier,
    onDeleteClick: () -> Unit, onEditClick: () -> Unit, onCloseClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        val iconColor = MaterialTheme.extendedColors.iconColor
        val border = Modifier.border(
            1.dp,
            MaterialTheme.extendedColors.primaryBorder,
            RoundedCornerShape(15.dp)
        )
        EditDeleteBtn(
            border, iconColor, onEditClick = onEditClick, onDeleteClick = onDeleteClick
        )
        CloseBtn(border, iconColor, onClick = onCloseClick)
    }
}

@Composable
fun EditDeleteBtn(
    border: Modifier, iconColor: Color, onDeleteClick: () -> Unit, onEditClick: () -> Unit
) {
    Row(modifier = Modifier
        .let { border }
        .height(IntrinsicSize.Max)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val iconModifier = Modifier
            .padding(vertical = 13.dp, horizontal = 20.dp)
            .size(30.dp)
            .weight(1f)
        Icon(painterResource(R.drawable.ic_edit),
            contentDescription = "",
            tint = iconColor,
            modifier = iconModifier.clickable { onEditClick() })
        VerticalDivider(
            Modifier.fillMaxHeight(0.8f),
            color = MaterialTheme.extendedColors.primaryBorder,
        )
        Icon(painterResource(R.drawable.ic_delete),
            contentDescription = "",
            tint = Red75,
            modifier = iconModifier.clickable { onDeleteClick() })
    }
}

@Composable
private fun CloseBtn(
    border: Modifier, iconColor: Color, onClick: () -> Unit
) {
    Row(
        modifier = border
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        Icon(
            painterResource(R.drawable.ic_close),
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier
                .padding(vertical = 18.dp)
                .size(18.dp)
        )
        Text(
            "Close", style = MaterialTheme.typography.bodyMedium.copy(
                color = iconColor, fontSize = 18.sp
            )
        )
    }
}