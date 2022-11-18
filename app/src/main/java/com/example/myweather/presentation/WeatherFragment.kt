package com.example.myweather.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.presentation.adapters.DailyForecastAdapter
import com.example.myweather.presentation.adapters.HourlyForecastAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentWeatherBinding is null")

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val hourlyAdapter by lazy {
        HourlyForecastAdapter()
    }

    private val dailyAdapter by lazy {
        DailyForecastAdapter()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLastLoadedCity()
        setupRefreshLayout()
        setupCurrentConditions()
        setupHourlyForecast()
        setupDailyForecast()
        setupErrorSnackBars()
        setupSearchByCityName()
        setupLocationSearch()
        viewModel.loadDataByCityName(getLastLoadedCity())
    }

    private fun getLastLoadedCity(): String {
        val preferences = activity?.getPreferences(Context.MODE_PRIVATE)
        return preferences?.getString(KEY_CITY_NAME, DEFAULT_CITY) ?: DEFAULT_CITY
    }

    private fun updateLastLoadedCity(newCityName: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString(KEY_CITY_NAME, newCityName)?.apply()
    }

    private fun setupRefreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadDataByCityName(getLastLoadedCity())
        }
    }

    private fun setupCurrentConditions() {
        viewModel.currentConditions.observe(viewLifecycleOwner) {
            updateLastLoadedCity(it.address.substringBefore(","))
            with(binding) {
                stopRefreshingAnimation()
                hideSearchIfVisible()
                textViewCityName.text = it.address.substringBefore(",")
                textViewDate.text = convertTimestampToStringDate(it.datetimeEpoch)

                val icon = ResourcesCompat.getDrawable(resources, mapIconIdToResId(it.icon), null)
                imageViewIcon.setImageDrawable(icon)
                textViewCurrentTemp.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.temperature_template),
                    it.temp.roundToInt()
                )

                textViewFeelsLikeTemp.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.feels_like_temperature_template),
                    it.feelsLike.roundToInt()
                )

                textViewDescription.text = it.conditions

                val lastUpdateTime = convertTimestampToStringLastUpdated(it.datetimeEpoch)

                textViewLastUpdated.text = String.format(
                    Locale.getDefault(),
                    getString(R.string.last_update_time),
                    lastUpdateTime
                )
                textViewHumidity.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.humidity_template),
                    it.humidity.roundToInt()
                )

                textViewPressure.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.pressure_template),
                    it.pressure.roundToInt()
                )

                textViewWindSpeed.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.wind_speed_template),
                    it.windSpeed
                )
                textViewPrecip.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.precip_count_template),
                    it.precip
                )
                textViewPrecipProb.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.precip_prob_template),
                    it.precipProb.roundToInt()
                )
                textViewVisibility.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.visibility_template),
                    it.visibility
                )
                textViewUvIndex.text = it.uvIndex.toString()
                textViewSunriseTime.text = it.sunrise.substringBeforeLast(":")
                textViewSunsetTime.text = it.sunset.substringBeforeLast(":")
            }
        }
    }

    private fun setupHourlyForecast() {
        binding.recyclerViewHourlyForecast.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.refreshLayout.isEnabled = newState == RecyclerView.SCROLL_STATE_IDLE
            }
        })
        binding.recyclerViewHourlyForecast.adapter = hourlyAdapter

        viewModel.hourlyForecast.observe(viewLifecycleOwner) {
            hourlyAdapter.submitList(it)
        }
    }

    private fun setupDailyForecast() {
        binding.recyclerViewDailyForecast.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.refreshLayout.isEnabled = newState == RecyclerView.SCROLL_STATE_IDLE
            }
        })
        binding.recyclerViewDailyForecast.adapter = dailyAdapter

        viewModel.weeklyForecast.observe(viewLifecycleOwner) {
            dailyAdapter.submitList(it)
        }
    }

    private fun setupErrorSnackBars() {
        viewModel.clientError.observe(viewLifecycleOwner) {
            stopRefreshingAnimation()
            showInvalidLocationSnackbar()
        }

        viewModel.networkError.observe(viewLifecycleOwner) {
            stopRefreshingAnimation()
            showNoInternetSnackbar()
        }

        viewModel.serverError.observe(viewLifecycleOwner) {
            stopRefreshingAnimation()
            showServerErrorSnackbar()
        }
    }

    private fun setupSearchByCityName() {
        binding.imageViewCity.setOnClickListener {
            binding.editTextCityName.visibility = View.VISIBLE
            binding.editTextCityName.requestFocus()
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.showSoftInput(binding.editTextCityName, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.editTextCityName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.editTextCityName.visibility = View.INVISIBLE
                binding.editTextCityName.setText("")
                val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        binding.editTextCityName.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.loadDataByCityName(view.text.trim().toString())
                startRefreshingAnimation()
                return@setOnEditorActionListener true
            }
            false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                hideSearchIfVisible()
            }
        }
    }

    private fun setupLocationSearch() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    if (isLocationEnabled()) {
                        startRefreshingAnimation()
                        tryToSearchWithLastLocation()
                    } else {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                        Toast.makeText(
                            context,
                            getString(R.string.please_turn_on_location),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.give_location_permission_please),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

        binding.ivLocation.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun tryToSearchWithLastLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                if (lastLocation != null) {
                    viewModel.loadDataByCoordinates(lastLocation.latitude, lastLocation.longitude)
                } else {
                    searchWithCurrentLocation()
                }
            }
        }
    }

    private fun searchWithCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_LOW_POWER, null)
                .addOnSuccessListener { currentLocation ->
                    if (currentLocation != null) {
                        viewModel.loadDataByCoordinates(
                            currentLocation.latitude,
                            currentLocation.longitude
                        )
                    } else {
                        showNoInternetSnackbar()
                        stopRefreshingAnimation()
                    }
                }
        }
    }

    private fun hideSearchIfVisible() {
        if (binding.editTextCityName.isVisible) {
            binding.editTextCityName.visibility = View.INVISIBLE
            binding.editTextCityName.setText("")
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(binding.editTextCityName.windowToken, 0)
        }
    }

    private fun startRefreshingAnimation() {
        binding.refreshLayout.isRefreshing = true
    }

    private fun stopRefreshingAnimation() {
        binding.refreshLayout.isRefreshing = false
    }

    private fun showInvalidLocationSnackbar() {
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.unknown_location_check_city_name),
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    private fun showNoInternetSnackbar() {
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.check_network_connection),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(getString(R.string.try_again)) {
            viewModel.loadDataByCityName(getLastLoadedCity())
        }
        snackbar.show()
    }

    private fun showServerErrorSnackbar() {
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.server_error_occurred),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(getString(R.string.try_again)) {
            viewModel.loadDataByCityName(getLastLoadedCity())
        }
        snackbar.show()
    }

    private fun checkLocationPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun convertTimestampToStringDate(time: Long): String {
        return SimpleDateFormat("d MMMM, EEEE", Locale.getDefault()).format(time * 1000)
    }

    private fun convertTimestampToStringLastUpdated(timestamp: Long): String {
        return SimpleDateFormat("d/MM H:mm", Locale.getDefault()).format(timestamp * 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_CITY_NAME = "city_name"
        private const val DEFAULT_CITY = "Санкт-Петеребург"
    }
}