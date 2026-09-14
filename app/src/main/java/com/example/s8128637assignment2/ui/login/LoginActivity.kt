package com.example.s8128637assignment2.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.s8128637assignment2.R
import com.example.s8128637assignment2.databinding.ActivityLoginBinding
import com.example.s8128637assignment2.ui.dashboard.DashboardActivity
import com.example.s8128637assignment2.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = getString(R.string.login_title)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            viewModel.login(username, password)
        }

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: LoginUiState) {
        when (state) {
            is LoginUiState.Idle -> setLoading(false)
            is LoginUiState.Loading -> setLoading(true)
            is LoginUiState.Success -> {
                setLoading(false)
                navigateToDashboard(state.keypass)
            }
            is LoginUiState.Error -> {
                setLoading(false)
                binding.textError.text = state.message
                binding.textError.visibility = View.VISIBLE
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.buttonLogin.isEnabled = !loading
        if (loading) {
            binding.textError.visibility = View.GONE
        }
    }

    private fun navigateToDashboard(keypass: String) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            putExtra(Constants.EXTRA_KEYPASS, keypass)
        }
        startActivity(intent)
        finish()
    }
}
