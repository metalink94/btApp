package ru.d_novikov.bluetoothapp.screens.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.d_novikov.bluetoothapp.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private List<LatLng> places = new ArrayList<>();
    private String mapsApiKey;
    private int width;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        places.add(new LatLng(55.754724, 37.621380));
        places.add(new LatLng(55.760133, 37.618697));
        places.add(new LatLng(55.764753, 37.591313));
        places.add(new LatLng(55.728466, 37.604155));

        mapsApiKey = this.getResources().getString(R.string.map);

        width = getResources().getDisplayMetrics().widthPixels;
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*MarkerOptions[] markers = new MarkerOptions[places.size()];
        for (int i = 0; i < places.size(); i++) {
            markers[i] = new MarkerOptions()
                    .position(new LatLng(places.get(i).lat, places.get(i).lng));
            googleMap.addMarker(markers[i]);
        }*/


        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(mapsApiKey)
                .build();
        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.WALKING)
                    .origin(places.get(0))
                    .destination(places.get(places.size() - 1))
                    .waypoints(places.get(1), places.get(2)).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.google.maps.errors.ApiException e) {
            e.printStackTrace();
        }

        List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
        PolylineOptions line = new PolylineOptions();

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < path.size(); i++) {
            line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
            latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
        }

        line.width(16f).color(R.color.colorPrimary);

        googleMap.addPolyline(line);

        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, width, 25);
        googleMap.moveCamera(track);
    }

    @NotNull
    public static Fragment getInstance() {
        return new MapFragment();
    }
}
