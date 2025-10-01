package com.honeypot.app.ui.dashboard.profile.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.honeypot.app.R
import com.honeypot.app.ui.theme.Blue20
import com.honeypot.app.ui.theme.Green20
import com.honeypot.app.ui.theme.Red20
import com.honeypot.app.ui.theme.Violet20
import com.honeypot.app.ui.theme.Yellow20


enum class ProfileOptions(
    @StringRes val titleRes: Int,
    @DrawableRes val icon: Int,
    val iconColor: Color = Violet20
) {
    ACCOUNT(R.string.accounts, R.drawable.ic_wallet, Violet20),
    CATEGORY(R.string.categories, R.drawable.ic_category, Yellow20),
    AUTO_TRACKING(R.string.auto_tracking, R.drawable.ic_autotracking),
    EXPORT(R.string.export, R.drawable.ic_exoprt, Green20),
    ABOUT(R.string.about, R.drawable.ic_about, Blue20),
    LOGOUT(R.string.logout, R.drawable.ic_logout, Red20);
}

enum class AboutOptions(
    @StringRes val titleRes: Int,
) {
    WEBSITE(R.string.website),
    PRIVACY_POLICY(R.string.privacy_policy),
    TERMS_AND_CONDITION(R.string.terms_of_service),
    SEND_FEEDBACK(R.string.honeypot_feedback),
    CONTACT_US(R.string.contact_us)
}


object ProfileOptionProvider {
    fun getGeneralOptions(): List<ProfileOptions> {
        return listOf(
            ProfileOptions.ACCOUNT,
            ProfileOptions.CATEGORY,
            ProfileOptions.EXPORT,
            ProfileOptions.AUTO_TRACKING,
            ProfileOptions.ABOUT
        )
    }

    fun getDangerOptions(): List<ProfileOptions> {
        return listOf(ProfileOptions.LOGOUT)
    }
}

enum class AvatarOptions(
    val avatarSlug: String,
    @DrawableRes val icon: Int,
) {
    DEFAULT("ic_avatar_default", R.drawable.ic_user_new_fill),
    MALE("ic_avatar_male", R.drawable.ic_avatar_male),
    FEMALE("ic_avatar_female", R.drawable.ic_avatar_female),
}
