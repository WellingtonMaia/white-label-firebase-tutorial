package br.com.douglasmotta.whitelabeltutorial.ui.addproduct

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.douglasmotta.whitelabeltutorial.R
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.domain.usecase.CreateProductUseCase
import br.com.douglasmotta.whitelabeltutorial.util.fromCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {


    private val _imageUriErrorResId = MutableLiveData<Int>()
    val imageUriErrorResId: LiveData<Int> = _imageUriErrorResId

    private val _descriptionFieldErrorResId = MutableLiveData<Int?>()
    val descriptionFieldErrorResId: LiveData<Int?> = _descriptionFieldErrorResId

    private val _priceFieldErrorResId = MutableLiveData<Int?>()
    val priceFieldErrorResId: LiveData<Int?> = _priceFieldErrorResId

    private val _productCreated = MutableLiveData<Product>()
    val productCreated: LiveData<Product> = _productCreated
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var isFormValid = false

    fun createProduct(description: String, price: String, imageUri: Uri?) {
        _isLoading.value = true
        viewModelScope.launch {
            isFormValid = true

            _imageUriErrorResId.value = getDrawableResIdIfEmpty(imageUri)
            _descriptionFieldErrorResId.value = getErrorStringResIdIfEmpty(description)
            _priceFieldErrorResId.value = getErrorStringResIdIfEmpty(price)

            if (isFormValid) {
                try {
                    _productCreated.value = createProductUseCase(description, price.fromCurrency(), imageUri!!)
                    _isLoading.value = false
                }catch (e: Exception) {
                    Log.d("CreateProduct", e.toString())
                    _isLoading.value = false
                }
            } else _isLoading.value = false
        }
    }

    private fun getErrorStringResIdIfEmpty(value: String): Int? {
        return if (value.isEmpty()) {
            isFormValid = false
            R.string.add_product_field_error
        } else null
    }

    private fun getDrawableResIdIfEmpty(value: Uri?): Int {
        return if (value == null) {
            isFormValid = false
            R.drawable.background_product_image_error
        } else R.drawable.background_product_image
    }
}