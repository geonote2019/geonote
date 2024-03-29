package com.geonote.core

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.geonote.data.AppRepository
import com.geonote.data.model.db.Marker
import com.geonote.helper.K
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeoManager private constructor(
    private val mContext: Context,
    private val mAppRepository: AppRepository
) {

    val mGeofencingClient = LocationServices.getGeofencingClient(mContext)

    private val geofencePendingIntent by lazy {
        val intent = Intent(mContext, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun addGeofence() {
        val geofence = Geofence.Builder()
            .setRequestId("geo1")
            .setCircularRegion(53.8733697, 27.546868, 1000F)
            .setExpirationDuration(600 * 1000)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER
                        or Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(mContext, GeofenceBroadcastReceiver::class.java)
        val geofencePendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        mGeofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                K.d("addGeofences success")
            }
            addOnFailureListener {
                K.e("addGeofences failure", it)
            }
        }
    }

    fun removeGeofence() {
        mGeofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                K.d("removeGeofences success")
            }
            addOnFailureListener {
                K.e("removeGeofences failure", it)
            }
        }
    }


    fun addMarker(marker: Marker) {
    }

    fun removeMarker(id: Int) {
    }

    fun clearAllMarkers() {
    }

    fun restoreAllMarkers() {
    }

    // ...

    companion object {
        private var INSTANCE: GeoManager? = null

        fun getInstance(context: Context): GeoManager {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = GeoManager(
                            context,
                            AppRepository.getInstance(context)
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

}