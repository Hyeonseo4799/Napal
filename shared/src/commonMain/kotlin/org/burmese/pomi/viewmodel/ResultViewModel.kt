package org.burmese.pomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.burmese.pomi.domain.CardSpecialEffect
import org.burmese.pomi.domain.Prompt
import org.burmese.pomi.network.Repository

class ResultViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    fun generateImage(prompt: Prompt, byteArray: ByteArray) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val image = repository.generateImage(prompt, byteArray)
                image?.let {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generatedImage = image,
                            specialEffect = CardSpecialEffect.rollRandom()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "이미지 생성에 실패했어요")
                }
            }
        }
    }

    fun clearSpecialEffect() {
        _uiState.update { it.copy(specialEffect = null) }
    }
}

data class ResultUiState(
    val isLoading: Boolean = false,
    val generatedImage: ByteArray? = null,
    val error: String? = null,
    val specialEffect: CardSpecialEffect? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ResultUiState
        if (generatedImage != null) {
            if (other.generatedImage == null || !generatedImage.contentEquals(other.generatedImage)) return false
        } else if (other.generatedImage != null) return false
        return isLoading == other.isLoading && error == other.error && specialEffect == other.specialEffect
    }

    override fun hashCode(): Int {
        var result = generatedImage?.contentHashCode() ?: 0
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (specialEffect?.hashCode() ?: 0)
        return result
    }
}
