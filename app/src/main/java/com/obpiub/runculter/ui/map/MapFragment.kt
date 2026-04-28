package com.obpiub.runculter.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.R
import com.obpiub.runculter.databinding.FragmentMapBinding
import android.location.Location as AndroidLocation
import android.location.LocationManager
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolygonMapObject
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.runtime.image.ImageProvider
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapFragment : Fragment(), android.location.LocationListener {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private var locationManager: LocationManager? = null
    private var placemarks: List<PlacemarkMapObject> = emptyList()
    private var triggerZonePolygons: List<PolygonMapObject> = emptyList()
    private var userLocationPlacemark: PlacemarkMapObject? = null
    private var lastKnownLocation: AndroidLocation? = null
    private var processedLandmarks = mutableSetOf<Long>()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val TRIGGER_DISTANCE = 100.0 // метров
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RunCultureApplication).repository

        binding.myLocationButton.setOnClickListener {
            centerMapOnUserLocation()
        }

        if (checkLocationPermission()) {
            setupLocationProvider()
            loadLandmarks()
        } else {
            requestLocationPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        locationManager?.removeUpdates(this)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager?.removeUpdates(this)
        _binding = null
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLocationProvider()
                loadLandmarks()
            } else {
                Toast.makeText(requireContext(), "Разрешение на геолокацию необходимо для работы приложения", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupLocationProvider() {
        try {
            locationManager = requireContext().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
            val networkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

            if (gpsEnabled) {
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    10f,
                    this
                )
            } else if (networkEnabled) {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    10f,
                    this
                )
            }

            lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            lastKnownLocation?.let {
                updateMapLocation(it)
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Ошибка доступа к геолокации", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadLandmarks() {
        lifecycleScope.launch {
            val landmarks = repository.getAllLandmarks()
            Log.d("MapFragment", "Загружено достопримечательностей: ${landmarks.size}")
            landmarks.forEach { landmark ->
                Log.d("MapFragment", "Достопримечательность: ${landmark.name} - ${landmark.latitude}, ${landmark.longitude}, посещена: ${landmark.isVisited}")
            }
            displayLandmarks(landmarks)
        }
    }

    private fun displayLandmarks(landmarks: List<com.obpiub.runculter.data.model.Landmark>) {
        val mapObjects = binding.mapView.map.mapObjects

        placemarks.forEach { mapObjects.remove(it) }
        triggerZonePolygons.forEach { mapObjects.remove(it) }

        placemarks = landmarks.map { landmark ->
            val point = Point(landmark.latitude, landmark.longitude)
            val placemark = mapObjects.addPlacemark(point)
            placemark.userData = landmark
            placemark
        }

        triggerZonePolygons = landmarks.map { landmark ->
            val centerPoint = Point(landmark.latitude, landmark.longitude)
            val circlePoints = createCirclePoints(centerPoint, TRIGGER_DISTANCE)
            val polygon = Polygon(LinearRing(circlePoints), emptyList())
            val polygonMapObject = mapObjects.addPolygon(polygon)

            polygonMapObject.fillColor = 0x1AFFB800.toInt()
            polygonMapObject.strokeColor = 0xFFFFB800.toInt()
            polygonMapObject.strokeWidth = 3f
            polygonMapObject.zIndex = 1f
            polygonMapObject.userData = landmark
            polygonMapObject
        }
    }

    private fun createCirclePoints(center: Point, radiusMeters: Double): List<Point> {
        val points = mutableListOf<Point>()
        val numPoints = 64

        val radiusLat = radiusMeters / 111000.0
        val radiusLon = radiusMeters / (111000.0 * kotlin.math.cos(Math.toRadians(center.latitude)))

        for (i in 0 until numPoints) {
            val angle = 2.0 * PI * i / numPoints
            val lat = center.latitude + radiusLat * sin(angle)
            val lon = center.longitude + radiusLon * cos(angle)
            points.add(Point(lat, lon))
        }

        points.add(points[0])
        return points
    }

    override fun onLocationChanged(location: AndroidLocation) {
        lastKnownLocation = location
        updateMapLocation(location)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}

    private fun updateMapLocation(location: AndroidLocation) {
        val point = Point(location.latitude, location.longitude)
        updateUserLocationMarker(point)

        if (lastKnownLocation == null) {
            binding.mapView.map.move(
                CameraPosition(point, 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        }

        checkProximityToLandmarks(point)
    }

    private fun updateUserLocationMarker(point: Point) {
        val mapObjects = binding.mapView.map.mapObjects

        userLocationPlacemark?.let { mapObjects.remove(it) }

        userLocationPlacemark = mapObjects.addPlacemark(point)

        // Создаем несколько меток вокруг точки для визуального увеличения
        val offsets = listOf(
            Point(point.latitude + 0.0001, point.longitude),
            Point(point.latitude - 0.0001, point.longitude),
            Point(point.latitude, point.longitude + 0.0001),
            Point(point.latitude, point.longitude - 0.0001)
        )
        
        offsets.forEach { offsetPoint ->
            val marker = mapObjects.addPlacemark(offsetPoint)
            marker.zIndex = 99f
        }

        try {
            val icon = ImageProvider.fromResource(requireContext(), R.drawable.ic_user_location_very_large)
            userLocationPlacemark?.setIcon(icon)
            Log.d("MapFragment", "Иконка местоположения (очень большая 96dp) установлена")
        } catch (e: Exception) {
            Log.e("MapFragment", "Ошибка при установке очень большой иконки", e)
            try {
                val icon = ImageProvider.fromResource(requireContext(), R.drawable.ic_user_location_large)
                userLocationPlacemark?.setIcon(icon)
                Log.d("MapFragment", "Иконка местоположения (большая 72dp) установлена")
            } catch (e2: Exception) {
                Log.e("MapFragment", "Ошибка при установке иконки", e2)
            }
        }

        userLocationPlacemark?.zIndex = 100f
    }

    private fun centerMapOnUserLocation() {
        lastKnownLocation?.let { location ->
            val point = Point(location.latitude, location.longitude)
            binding.mapView.map.move(
                CameraPosition(point, 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0.5f),
                null
            )
            Toast.makeText(requireContext(), "Карта центрирована на вашем местоположении", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(requireContext(), "Местоположение еще не определено", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkProximityToLandmarks(userPoint: Point) {
        lifecycleScope.launch {
            val landmarks = repository.getAllLandmarks()
            landmarks.forEach { landmark ->
                val landmarkPoint = Point(landmark.latitude, landmark.longitude)
                val distance = calculateDistance(userPoint, landmarkPoint)

                if (distance <= TRIGGER_DISTANCE && !processedLandmarks.contains(landmark.id)) {
                    processedLandmarks.add(landmark.id)
                    showCharacterUnlockDialog(landmark)
                }
            }
        }
    }

    private fun calculateDistance(point1: Point, point2: Point): Double {
        val lat1 = Math.toRadians(point1.latitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lon2 = Math.toRadians(point2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return 6371000 * c
    }

    private fun showCharacterUnlockDialog(landmark: com.obpiub.runculter.data.model.Landmark) {
        lifecycleScope.launch {
            val character = withContext(Dispatchers.IO) {
                repository.getCharacterByLandmarkId(landmark.id)
            }

            if (character != null) {
                val isUnlocked = character.isUnlocked
                
                // Получаем факты из LandmarksData для этой достопримечательности
                val landmarkData = com.obpiub.runculter.data.database.LandmarksData.getLandmarks()
                    .firstOrNull { 
                        kotlin.math.abs(it.landmark.latitude - landmark.latitude) < 0.0001 && 
                        kotlin.math.abs(it.landmark.longitude - landmark.longitude) < 0.0001
                    }
                val facts = landmarkData?.facts ?: emptyList()

                withContext(Dispatchers.Main) {
                    val dialog = CharacterUnlockDialogFragment.newInstance(
                        onYesClick = {
                            if (!isUnlocked) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    // Разблокируем персонажа
                                    val updatedCharacter = character.copy(
                                        isUnlocked = true,
                                        unlockDate = System.currentTimeMillis()
                                    )
                                    repository.updateCharacter(updatedCharacter)
                                    
                                    // Разблокируем факты из LandmarksData
                                    val unlockDate = System.currentTimeMillis()
                                    facts.forEach { factData ->
                                        // Находим факт в базе по title
                                        val factInDb = repository.getAllFacts().firstOrNull { it.title == factData.title }
                                        factInDb?.let { fact ->
                                            if (!fact.isUnlocked) {
                                                val updatedFact = fact.copy(
                                                    isUnlocked = true,
                                                    unlockDate = unlockDate
                                                )
                                                repository.updateFact(updatedFact)
                                            }
                                        }
                                    }
                                    
                                    withContext(Dispatchers.Main) {
                                        loadLandmarks()
                                        navigateToCharacters()
                                    }
                                }
                            } else {
                                navigateToCharacters()
                            }
                        },
                        onNoClick = {},
                        onRepeatClick = {
                            processedLandmarks.remove(landmark.id)
                            lifecycleScope.launch(Dispatchers.Main) {
                                kotlinx.coroutines.delay(300)
                                showCharacterUnlockDialog(landmark)
                            }
                        }
                    )
                    
                    dialog.show(parentFragmentManager, "character_unlock_dialog")
                }
            }
        }
    }

    private fun navigateToCharacters() {
        val mainActivity = requireActivity() as? com.obpiub.runculter.ui.main.MainActivity
        mainActivity?.navigateToAchievements()
        Toast.makeText(requireContext(), "Откройте раздел 'Персонажи' чтобы увидеть нового персонажа!", Toast.LENGTH_SHORT).show()
    }
}
