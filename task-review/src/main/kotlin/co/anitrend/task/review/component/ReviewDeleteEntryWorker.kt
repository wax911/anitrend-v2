/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.task.review.component

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.data.review.DeleteReviewInteractor
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.navigation.extensions.fromWorkerParameters
import co.anitrend.navigation.extensions.transform
import kotlinx.coroutines.flow.first

class ReviewDeleteEntryWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: DeleteReviewInteractor
) : SupportCoroutineWorker(context, parameters) {

    private val param by parameters.transform<
        ReviewTaskRouter.Param.DeleteEntry,
        ReviewParam.Delete
    > { ReviewParam.Delete(id = it.id) }

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     *
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [androidx.work.ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        val dataState = interactor(param)

        val networkState = dataState.loadState.first { state ->
            state is LoadState.Success || state is LoadState.Error
        }

        return when (networkState) {
            is LoadState.Success -> Result.success()
            else -> Result.failure()
        }
    }
}
