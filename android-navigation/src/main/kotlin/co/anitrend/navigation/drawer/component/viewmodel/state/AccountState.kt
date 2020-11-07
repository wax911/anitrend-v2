/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.navigation.drawer.component.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import co.anitrend.core.settings.Settings
import co.anitrend.data.account.action.AccountAction
import co.anitrend.data.account.usecase.AccountUseCaseContract
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.model.account.Account
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

internal class AccountState(
    private val settings: IAuthenticationSettings,
    private val useCase: AccountUseCaseContract
) : ISupportViewModelState<List<Account>> {

    private val useCaseResult = MutableLiveData<DataState<List<User>?>>()

    override val model =
        Transformations.switchMap(useCaseResult) { dataState ->
            dataState.model.map { users ->
                if (users.isNullOrEmpty())
                    listOf(
                        Account.Group(
                            titleRes = R.string.account_header_active,
                            groupId = R.id.account_group_active
                        ),
                        Account.Anonymous(
                            titleRes = R.string.label_account_anonymous,
                            imageRes = R.mipmap.ic_launcher,
                            isActiveUser = true
                        ),
                        Account.Group(
                            titleRes = R.string.account_header_other,
                            groupId = R.id.account_group_other
                        ),
                        Account.Authorize(
                            titleRes = R.string.label_account_add_new
                        )
                    )
                else {
                    val activeUser = users.firstOrNull { user ->
                        settings.authenticatedUserId == user.id
                    }

                    val accounts = mutableListOf<Account>(
                        Account.Group(
                            titleRes = R.string.account_header_active,
                            groupId = R.id.account_group_active
                        )
                    )

                    if (activeUser != null) {
                        accounts.add(
                            Account.Authenticated(
                                id = activeUser.id,
                                isActiveUser = true,
                                userName = activeUser.name,
                                coverImage = activeUser.avatar
                            )
                        )

                        if (users.size > 1) {
                            accounts.add(
                                Account.Group(
                                    titleRes = R.string.account_header_inactive,
                                    groupId = R.id.account_group_inactive
                                )
                            )
                            accounts += users.filter {
                                activeUser.id != it.id
                            }.map { user ->
                                Account.Authenticated(
                                    id = user.id,
                                    isActiveUser = false,
                                    userName = user.name,
                                    coverImage = user.avatar
                                )
                            }
                        }
                    }

                    accounts += listOf(
                        Account.Group(
                            titleRes = R.string.account_header_other,
                            groupId = R.id.account_group_other
                        ),
                        Account.Authorize(
                            titleRes = R.string.label_account_add_new
                        )
                    )
                    accounts
                }
            }
        }

    override val networkState =
        Transformations.switchMap(useCaseResult) { it.networkState.asLiveData() }

    override val refreshState =
        Transformations.switchMap(useCaseResult) { it.refreshState.asLiveData() }

    operator fun invoke() {
        val result = useCase.getAuthorizedAccounts()
        useCaseResult.postValue(result)
    }

    fun signOut(account: Account) {
        useCase.signOut(
            AccountAction.SignOut(
                userId = account.id
            )
        )
    }

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {
        useCase.onCleared()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
        val uiModel = useCaseResult.value
        uiModel?.refresh?.invoke()
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }
}