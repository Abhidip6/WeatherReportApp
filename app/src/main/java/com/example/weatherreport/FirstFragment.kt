package com.example.weatherreport

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherreport.databinding.FragmentFirstBinding
import com.example.weatherreport.model.WeatherResponse
import com.example.weatherreport.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstFragment : Fragment(R.layout.fragment_first) {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFirstBinding.bind(view)

        binding.fetchButton.setOnClickListener {
            val city = binding.cityEditText.text.toString()
            if (city.isNotEmpty()) {
                fetchWeather(city)
            } else {
                Toast.makeText(requireContext(), "Enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeather(city: String) {
        val apiKey = "90e3ef9b4c9a23c2127403b99ae700f1" // Replace with your actual API key
        RetrofitInstance.api.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    binding.textView.text = "City: ${weather?.name}\nTemp: ${weather?.main?.temp}°C"
                } else {
                    Toast.makeText(requireContext(), "Failed to load weather", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}