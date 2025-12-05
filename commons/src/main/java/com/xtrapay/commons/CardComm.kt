package com.xtrapay.commons

object CardComm {

    const val REQUEST_DATA = "request_data"
    const val HOST_CONFIG = "host_config"
    const val READERS = "readers"
    const val TRANSACTION_RESPONSE = "transaction_response"
    const val TRANSACTION_TYPE = "transactionType"
    const val ACCOUNT_DETAILS = "accountDetails"
    const val ERROR = "error"
    const val BUNDLE = "cardOption"

    const val SERVICE_TYPE = "service_type"
    const val EXTRA_META_DATA = "extra_meta_data"

    const val PACKAGE_NAME = "com.tegritech.xtrapay"

    const val CARD_TRANSACTION_CLASS_DETAILS =
        "com.tegritech.xtrapay.ui.card.processor.CardTransactionActivity"

    //specific to extension
    //must be same value as CardComm in the app
    const val EXT_REQUEST_DATA = "ext_request_data"
    const val EXT_AMOUNT = "ext_amount"
    const val EXT_RRN = "ext_rrn"
    const val EXT_ECHO_DATA = "ext_echoData"
}