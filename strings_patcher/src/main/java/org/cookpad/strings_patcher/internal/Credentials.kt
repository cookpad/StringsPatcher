/*
 * Copyright 2017 Cookpad Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cookpad.strings_patcher.internal

import android.content.Context
import android.net.Uri
import android.support.annotation.VisibleForTesting

private val GOOGLE_API_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token"

data class GoogleCredentials(val refreshToken: String, val clientId: String, val clientSecret: String)

internal fun accessToken(googleCredentials: GoogleCredentials, context: Context): String =
        tokenFromDisk(context) ?: tokenFromServer(googleCredentials, context)

private fun tokenFromDisk(context: Context): String? {
    val (accessToken, expirationDate) = loadTokenAndExpirationDate(context)
    return accessToken?.let {
        if (isTokenActive(expirationDate)) return accessToken
        else return null
    }
}

@VisibleForTesting
internal fun isTokenActive(expirationDate: Long): Boolean = System.currentTimeMillis() < expirationDate

private fun tokenFromServer(googleCredentials: GoogleCredentials, context: Context): String {
    val builder = Uri.Builder()
            .appendQueryParameter("refresh_token", googleCredentials.refreshToken)
            .appendQueryParameter("client_id", googleCredentials.clientId)
            .appendQueryParameter("client_secret", googleCredentials.clientSecret)
            .appendQueryParameter("grant_type", "refresh_token")
    val response = jsonFromPostRequest(GOOGLE_API_ENDPOINT, builder)
    val accessToken = response.getString("access_token")

    saveTokenAndExpirationDate(accessToken, expireDate(response.getLong("expires_in")), context)

    return accessToken
}

@VisibleForTesting
internal fun expireDate(expiresInSeconds: Long): Long = expiresInSeconds * 1000 + System.currentTimeMillis()
