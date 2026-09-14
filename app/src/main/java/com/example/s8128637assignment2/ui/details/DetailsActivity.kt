package com.example.s8128637assignment2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.s8128637assignment2.R
import com.example.s8128637assignment2.databinding.ActivityDetailsBinding
import com.example.s8128637assignment2.databinding.RowDetailBinding
import com.example.s8128637assignment2.util.Constants
import com.example.s8128637assignment2.util.EntityUiMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = getString(R.string.details_title)

        val entity = parseEntity()
        if (entity == null) {
            binding.textDescription.text = "Unable to load entity details."
            return
        }

        renderFields(entity)
        binding.textDescription.text = EntityUiMapper.description(entity)
    }

    private fun parseEntity(): Map<String, Any>? {
        val json = intent.getStringExtra(Constants.EXTRA_ENTITY_JSON) ?: return null
        val type = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun renderFields(entity: Map<String, Any>) {
        val inflater = LayoutInflater.from(this)
        EntityUiMapper.allFieldsOrdered(entity).forEach { (key, value) ->
            val row = RowDetailBinding.inflate(inflater, binding.containerFields, true)
            row.textLabel.text = key
            row.textValue.text = value
        }
    }
}
