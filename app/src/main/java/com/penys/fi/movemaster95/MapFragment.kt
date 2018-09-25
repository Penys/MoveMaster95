package com.penys.fi.movemaster95

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.MapView


class MapFragment : Fragment(), LocationListener {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {



        val view = inflater?.inflate(R.layout.map_layout, container, false)
        val map = view!!.findViewById<MapView>(R.id.map)


        val ctx = activity

        Configuration.getInstance().load(ctx,
                PreferenceManager.getDefaultSharedPreferences(ctx))
        val myLocation = MyLocationNewOverlay(map)
        myLocation.enableMyLocation()
        myLocation.enableFollowLocation()
        map?.overlays?.add(myLocation)


        map?.setTileSource(TileSourceFactory.MAPNIK)
        map?.setBuiltInZoomControls(true)
        map?.setMultiTouchControls(true)

        map?.controller?.setZoom(15.0)


        val lm = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        if ((Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity!!.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity as Activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)

        } else {
            lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    60 * 1000,
                    50f,
                    this)
        }


        return view

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onLocationChanged(location: Location?) {
    }

}


