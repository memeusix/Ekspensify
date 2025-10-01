package com.honeypot.app.ui.dashboard.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.R
import com.honeypot.app.components.CustomOutlineTextField
import com.honeypot.app.components.FilledButton
import com.honeypot.app.data.model.TextFieldStateModel
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.ui.dashboard.profile.data.AvatarOptions
import com.honeypot.app.utils.getInitials

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditProfileBottomSheet(
    user: UserResponseModel,
    onDismiss: (user: UserResponseModel?) -> Unit
) {
    val editTextState = remember { mutableStateOf(TextFieldStateModel()) }
    var selectedAvatarOptions by remember { mutableStateOf(AvatarOptions.DEFAULT) }
    val focusRequester = remember { FocusRequester() }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    LaunchedEffect(Unit) {
        user.avatar?.let { avatar ->
            selectedAvatarOptions =
                AvatarOptions.entries.find { it.avatarSlug == avatar } ?: AvatarOptions.DEFAULT
        }
        user.name?.let {
            editTextState.value = editTextState.value.copy(text = it)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Text(
                stringResource(R.string.edit_profile),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold, fontSize = 18.sp
                )
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                items(
                    AvatarOptions.entries
                ) { avatar ->
                    AvatarOptionsItem(
                        avatar,
                        editTextState.value.text,
                        avatar == selectedAvatarOptions
                    ) {
                        selectedAvatarOptions = avatar
                    }
                }
            }

            CustomOutlineTextField(
                state = editTextState,
                placeholder = "Enter Your Name",
                isExpendable = false,
                focusRequester = focusRequester
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Email : ")
                    }
                    append(user.email)
                }, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.fillMaxWidth()
            )
            FilledButton(
                text = "Update",
                onClick = {
                    val requestModel = UserResponseModel(
                        name = editTextState.value.text,
                        avatar = selectedAvatarOptions.avatarSlug
                    )
                    onDismiss(requestModel)
                },
                enabled = editTextState.value.text.isNotEmpty(),
                textModifier = Modifier.padding(vertical = 17.dp),
                modifier = Modifier.padding(top = 4.dp),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun AvatarOptionsItem(
    avatar: AvatarOptions,
    userName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val baseModifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
        .clickable(
            indication = null,
            interactionSource = null,
            onClick = onSelect
        )

    val selectedModifier = if (isSelected) {
        Modifier
            .shadow(elevation = 2.dp, shape = CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .then(
                baseModifier
            )
    } else {
        baseModifier
    }

    if (avatar == AvatarOptions.DEFAULT) {
        Text(
            text = userName.getInitials(2),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = selectedModifier
                .wrapContentHeight(Alignment.CenterVertically)
        )
    } else {
        Image(
            painter = painterResource(avatar.icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = selectedModifier
        )
    }
}