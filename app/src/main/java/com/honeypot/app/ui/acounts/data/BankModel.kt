package com.honeypot.app.ui.acounts.data

import com.honeypot.app.R
import com.honeypot.app.utils.generateIconSlug

data class BankModel(
    val icon: Int?,
    val name: String = "",
    val shortName: String = "",
    val iconSlug: String,
    var isSelected: Boolean = false,
    var isEnable: Boolean = true,
) {
    companion object {
        fun getBanks(): List<BankModel> {
            return listOf(
                createBankModel("State Bank of India", "SBI", R.drawable.ic_state_bank_of_india),
                createBankModel("Bank of Baroda", "BOB", R.drawable.ic_bob_bank),
                createBankModel("HDFC Bank", "HDFC", R.drawable.ic_hdfc_bank),
                createBankModel("ICICI Bank", "ICICI", R.drawable.ic_icici_bank),
                createBankModel("Axis Bank", "Axis", R.drawable.ic_axis_bank),
                createBankModel("Kotak Mahindra Bank", "Kotak", R.drawable.ic_kotak_mahindra_bank),
                createBankModel("Punjab National Bank", "PNB", R.drawable.ic_punjab_national_bank),
                createBankModel("Union Bank of India", "Union", R.drawable.ic_union_bank),
                createBankModel(
                    "Central Bank of India",
                    "CBI",
                    R.drawable.ic_centeral_bank_of_india
                ),
                createBankModel("YES Bank", "Yes", R.drawable.ic_yes_bank),
                createBankModel("Citi Bank", "Citi", R.drawable.ic_citi_bank),
                createBankModel("IDFC FIRST Bank", "IDFC", R.drawable.ic_idfc_bank),
                createBankModel(
                    "India Post Payments Bank",
                    "IPPB",
                    R.drawable.ic_post_payments_bank
                ),
                createBankModel("Indian Bank", "Indian Bank", R.drawable.ic_indian_bank),
                createBankModel("IndusInd Bank", "IndusInd", R.drawable.ic_induslnd_bank),
            )
        }

        fun getWallet(): List<BankModel> {
            return listOf(
                createBankModel("Wallet", "Wallet", R.drawable.ic_wallet)
            )
        }

        fun getAccounts() = getBanks() + getWallet()

        private fun createBankModel(
            name: String, shortName: String, icon: Int, isSelected: Boolean = false
        ): BankModel {
            return BankModel(
                icon = icon,
                name = name,
                shortName = shortName,
                iconSlug = name.generateIconSlug(),
                isSelected = isSelected
            )
        }
    }
}
