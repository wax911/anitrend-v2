/*
 * Copyright (C) 2019  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.data.core.api.interceptor

import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import io.github.wax911.library.converter.GraphConverter
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ClientInterceptor interceptor add headers dynamically adds accept headers.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
internal class GraphClientInterceptor(
    private val authenticationHelper: IAuthenticationHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val contentLength = original.body?.contentLength() ?: 0
        val requestBuilder = original.newBuilder()

        requestBuilder.header(CONTENT_TYPE, GraphConverter.MIME_TYPE)
            .header(ACCEPT, ACCEPT_TYPE)
            .header(CONTENT_LENGTH, contentLength.toString())
            .method(original.method, original.body)

        if (authenticationHelper.isAuthenticated) {
            authenticationHelper(requestBuilder)
            return chain.proceed(requestBuilder.build())
        }

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val CONTENT_LENGTH = "Content-Length"
        private const val CONTENT_TYPE = "Content-Type"
        private const val ACCEPT = "Accept"

        private const val ACCEPT_TYPE = "application/json"
    }
}
