package di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import repository.MathWorldRepository
import viewModel.ClassManagementViewModel
import viewModel.MainViewModel

class MathWorldViewModelFactory(private val repository: MathWorldRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ClassManagementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClassManagementViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
