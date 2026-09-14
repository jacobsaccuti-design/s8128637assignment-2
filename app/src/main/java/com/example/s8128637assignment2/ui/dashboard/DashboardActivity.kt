package com.example.s8128637assignment2.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s8128637assignment2.databinding.ActivityDashboardBinding
import com.example.s8128637assignment2.ui.details.DetailsActivity
import com.example.s8128637assignment2.util.Constants
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private val gson = Gson()

    private val adapter = EntityAdapter { entity -> openDetails(entity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = "Dashboard"

        binding.recyclerEntities.layoutManager = LinearLayoutManager(this)
        binding.recyclerEntities.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { loadDashboard() }

        observeUiState()
        loadDashboard()
    }

    private fun loadDashboard() {
        val keypass = intent.getStringExtra(Constants.EXTRA_KEYPASS)
        if (keypass.isNullOrBlank()) {
            binding.textStatus.text = "Missing session key. Please log in again."
            binding.textStatus.visibility = View.VISIBLE
            return
        }
        viewModel.loadDashboard(keypass)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: DashboardUiState) {
        binding.swipeRefresh.isRefreshing = false
        when (state) {
            is DashboardUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.textStatus.visibility = View.GONE
                binding.recyclerEntities.visibility = View.GONE
            }
            is DashboardUiState.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerEntities.visibility = View.GONE
                binding.textStatus.text = "No entities to display"
                binding.textStatus.visibility = View.VISIBLE
            }
            is DashboardUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.textStatus.visibility = View.GONE
                binding.recyclerEntities.visibility = View.VISIBLE
                title = "Dashboard (${state.entityTotal})"
                adapter.submitList(state.entities)
            }
            is DashboardUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerEntities.visibility = View.GONE
                binding.textStatus.text = state.message
                binding.textStatus.visibility = View.VISIBLE
            }
        }
    }

    private fun openDetails(entity: Map<String, Any>) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(Constants.EXTRA_ENTITY_JSON, gson.toJson(entity))
        }
        startActivity(intent)
    }
}
