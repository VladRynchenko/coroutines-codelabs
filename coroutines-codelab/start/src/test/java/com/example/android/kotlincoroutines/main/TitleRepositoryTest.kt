/*
 * Copyright (C) 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.kotlincoroutines.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.kotlincoroutines.fakes.MainNetworkFake
import com.example.android.kotlincoroutines.fakes.TitleDaoFake
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TitleRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: TitleRepository
    private lateinit var dao: TitleDaoFake
    private lateinit var network: MainNetworkFake

    @Before
    fun setUp() = runBlockingTest {
        dao = TitleDaoFake("title")
        network = MainNetworkFake("OK")
        repository = TitleRepository(network, dao)
    }

    @Test
    fun whenRefreshTitleSuccess_insertsRows() = runBlockingTest {
        repository.refreshTitle()
        Truth.assertThat(dao.nextInsertedOrNull()).isEqualTo("OK")
    }

    @Test(expected = TitleRefreshError::class)
    fun whenRefreshTitleTimeout_throws() = runBlockingTest {
        launch {
            repository.refreshTitle()
        }
        advanceTimeBy(5_000)
    }
}