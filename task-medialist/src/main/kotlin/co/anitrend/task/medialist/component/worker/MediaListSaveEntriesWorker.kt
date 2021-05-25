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

package co.anitrend.task.medialist.component.worker

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.data.medialist.SaveMediaListEntriesInteractor
import co.anitrend.domain.medialist.model.MediaListParam
import co.anitrend.navigation.MediaListTaskRouter
import kotlinx.coroutines.flow.first

class MediaListSaveEntriesWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: SaveMediaListEntriesInteractor
) : SupportCoroutineWorker(context, parameters) {

    private val data by lazy(UNSAFE) {
        val map = parameters.inputData.keyValueMap
        MediaListTaskRouter.Param.SaveEntries.fromMap(map)
    }

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
        val param = MediaListParam.SaveEntries(
            status = data.status,
            score = data.score,
            scoreRaw = data.scoreRaw,
            progress = data.progress,
            progressVolumes = data.progressVolumes,
            repeat = data.repeat,
            priority = data.priority,
            private = data.private,
            notes = data.notes,
            hiddenFromStatusLists = data.hiddenFromStatusLists,
            advancedScores = data.advancedScores,
            startedAt = data.startedAt,
            completedAt = data.completedAt,
            ids = data.ids
        )

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