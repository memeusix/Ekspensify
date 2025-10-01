package com.honeypot.app.ui.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseViewModel
import com.honeypot.app.data.model.responseModel.CategoryListModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.data.model.responseModel.CustomIconModel
import com.honeypot.app.data.repository.CategoryRepository
import com.honeypot.app.utils.CategoryType
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    val spUtilsManager: SpUtilsManager
) : ViewModel(), BaseViewModel {

    private val _selectedCategoryType = MutableStateFlow<CategoryType>(CategoryType.DEBIT)
    val selectedCategoryType = _selectedCategoryType.asStateFlow()
    fun updateSelectedCategoryType(categoryType: CategoryType) {
        _selectedCategoryType.value = categoryType
    }

    /**
     *  get Categories
     */
    private val _getCategories =
        MutableStateFlow<ApiResponse<List<CategoryResponseModel>>>(ApiResponse.idle())
    val getCategories = _getCategories.asStateFlow()

    private fun fetchCategories() {
        spUtilsManager.categoriesData.value?.categories?.takeIf { it.isNotEmpty() }?.let {
            _getCategories.value = ApiResponse.success(it)
        } ?: run {
            getCategories()
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _getCategories.value = ApiResponse.loading(
                _getCategories.value.data,
            )
            val response = handleData(_getCategories.value) { categoryRepository.getCategories() }
            if (response is ApiResponse.Success) {
                spUtilsManager.updateCategoriesData(CategoryListModel(categories = response.data.orEmpty()))
            }
            _getCategories.value = response
        }
    }

    init {
        fetchCategories()
    }


    /**
     * get category icons
     */
    private val _getCustomIcons =
        MutableStateFlow<ApiResponse<List<CustomIconModel>>>(ApiResponse.idle())
    val getCustomIcons = _getCustomIcons.asStateFlow()
        .onStart { getCustomIcons() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000),
            ApiResponse.idle()
        )

    fun getCustomIcons() {
        viewModelScope.launch {
            _getCustomIcons.value = ApiResponse.loading(_getCustomIcons.value.data)
            _getCustomIcons.value =
                handleData(_getCustomIcons.value) { categoryRepository.getCustomIcons() }
        }
    }

    /**
     * create Categories
     */
    private val _createCategory =
        MutableStateFlow<ApiResponse<CategoryResponseModel>>(ApiResponse.idle())
    val createCategory = _createCategory.asStateFlow()
    fun createCategory(categoryResponseModel: CategoryResponseModel) {
        viewModelScope.launch {
            _createCategory.value = ApiResponse.loading()
            _createCategory.value = categoryRepository.createCategory(categoryResponseModel)
        }
    }

    fun resetCreateCategory() {
        _createCategory.value = ApiResponse.idle()
    }

    /**
     * delete Categories
     */
    private val _deleteCategory: MutableStateFlow<ApiResponse<CategoryResponseModel>> =
        MutableStateFlow(ApiResponse.idle())
    val deleteCategory = _deleteCategory.asStateFlow()
    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            _deleteCategory.value = ApiResponse.loading()
            _deleteCategory.value = categoryRepository.deleteCategory(categoryId)
        }
    }

    fun resetDeleteCategory() {
        _deleteCategory.value = ApiResponse.idle()
    }

}