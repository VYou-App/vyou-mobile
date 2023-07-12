package com.vyou.kmm.client.common.data

data class PKCE(
    val verifier: String,
    val challenge: String
)