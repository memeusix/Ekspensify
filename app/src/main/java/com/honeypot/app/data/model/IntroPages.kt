package com.honeypot.app.data.model


import com.honeypot.app.R

import androidx.annotation.StringRes

data class IntroPages(
    val image: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
) {
    companion object {
        fun getPages(): List<IntroPages> {
            val pages = mutableListOf<IntroPages>()
            pages.add(
                IntroPages(
                    image = R.drawable.ic_onboarding_1,
                    titleRes = R.string.intro_title_1,
                    descriptionRes = R.string.intro_desc_1
                )
            )

            pages.add(
                IntroPages(
                    image = R.drawable.ic_onboarding_2,
                    titleRes = R.string.intro_title_2,
                    descriptionRes = R.string.intro_desc_2
                )
            )

            pages.add(
                IntroPages(
                    image = R.drawable.ic_onboarding_3,
                    titleRes = R.string.intro_title_3,
                    descriptionRes = R.string.intro_desc_3
                )
            )

            return pages
        }
    }
}