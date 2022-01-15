/*
 * Copyright (C) 2021 The Android Open Source Project.
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
package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus> = _status

    private val _amphibians = MutableLiveData<List<Amphibian>>()
    val amphibians: LiveData<List<Amphibian>> = _amphibians

    private val _amphibian = MutableLiveData<Amphibian>()
    val amphibian: LiveData<Amphibian> = _amphibian

    fun getAmphibianList() {
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try {
                // Moved network request to I/O Dispatcher to unburden the Main/UI Dispatcher!!!
                val amphibianList = withContext(Dispatchers.IO) {
                    AmphibianApi.amphibianService.getAmphibians()
                }
                _amphibians.value = amphibianList
                _status.value = AmphibianApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AmphibianApiStatus.ERROR
                _amphibians.value = listOf()
            }
        }
    }

    // ------------------ TO DO LIST -----------------
    // <CHECK>
    // TODO: Create properties to represent MutableLiveData and LiveData for the API status
    // <CHECK>
    // TODO: Create properties to represent MutableLiveData and LiveData for a list of amphibian objects
    // <CHECK>
    // TODO: Create properties to represent MutableLiveData and LiveData for a single amphibian object.
    //  This will be used to display the details of an amphibian when a list item is clicked
    // <CHECK>
    // TODO: Create a function that gets a list of amphibians from the api service and sets the
    //  status via a Coroutine

    fun onAmphibianClicked(amphibian: Amphibian) {
        _amphibian.value = amphibian
        // <CHECK>
        // TODO: Set the amphibian object
    }
}
