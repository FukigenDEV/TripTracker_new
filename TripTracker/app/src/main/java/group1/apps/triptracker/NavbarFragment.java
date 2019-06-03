package group1.apps.triptracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NavbarFragment extends Fragment {

    private ImageView nav_icon_map;
    private ImageView nav_icon_timeline;
    private ImageView nav_icon_camera;
    private ImageView nav_icon_profile;

    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navbar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null) {
            nav_icon_map = getView().findViewById(R.id.nav_icon_map);
            nav_icon_timeline = getView().findViewById(R.id.nav_icon_timeline);
            nav_icon_camera = getView().findViewById(R.id.nav_icon_camera);
            nav_icon_profile = getView().findViewById(R.id.nav_icon_profile);

            nav_icon_map.setOnClickListener(getOnClickListener());
            nav_icon_timeline.setOnClickListener(getOnClickListener());
            nav_icon_camera.setOnClickListener(getOnClickListener());
            nav_icon_profile.setOnClickListener(getOnClickListener());
        }
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == nav_icon_map) {
                    openMap();
                } else if (v == nav_icon_timeline) {
                    openMemory();
                } else if (v == nav_icon_camera) {
                    openCamera();
                } else if (v == nav_icon_profile) {
                    openProfile();
                }
            }
        };
    }

    public void openMap() {
        startActivity(new Intent(activity, MapsActivityCurrentPlace.class));

        activity.finish();
    }

    public void openMemory() {
        startActivity(new Intent(activity, MemoryActivity.class));

        activity.finish();
    }

    public void openCamera() {
        startActivity(new Intent(activity, CameraActivity.class));

        activity.finish();
    }

    public void openProfile() {
        startActivity(new Intent(activity, MainActivity.class));

        activity.finish();
    }
}
