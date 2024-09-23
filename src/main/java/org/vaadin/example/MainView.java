package org.vaadin.example;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;

import org.vaadin.elmot.flow.sensors.GeoLocation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route
@PWA(name = "GeoStyler",
        shortName = "GeoStyler",
        description = "A geolocation display application.",
        enableInstallPrompt = false)
public class MainView extends VerticalLayout {

    private final Span latitudeDisplay = new Span();
    private final Span longitudeDisplay = new Span();
    private final Span timestampDisplay = new Span();

    public MainView() {
        addClassName("main-view");
        injectCustomCss();

        H1 header = new H1("GeoStyler");
        header.addClassName("header");

        Span locationDisplay = new Span("Waiting for location...");
        locationDisplay.addClassName("location-display");

        VerticalLayout locationContainer = new VerticalLayout();
        locationContainer.addClassName("location-container");

        H3 locationTitle = new H3("Location");
        locationTitle.addClassName("location-title");
        locationContainer.add(locationTitle);

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setWatch(true);
        geoLocation.setHighAccuracy(true);
        geoLocation.setTimeout(100000);
        geoLocation.setMaxAge(200000);

        geoLocation.addValueChangeListener(e -> {
            String position = e.getValue().toString();
            locationDisplay.setText("Location: " + position);
            updateLocationDetails(position);
        });

        geoLocation.addErrorListener(e -> locationDisplay.setText("Location: Error retrieving location"));

        VerticalLayout timestampContainer = new VerticalLayout();
        timestampContainer.addClassName("timestamp-container");

        H3 timestampTitle = new H3("Timestamp");
        timestampTitle.addClassName("timestamp-title");
        timestampContainer.add(timestampTitle, timestampDisplay);

        locationContainer.add(latitudeDisplay, longitudeDisplay);
        add(header, locationDisplay, locationContainer, timestampContainer, geoLocation);
    }

    private void injectCustomCss() {
        // Simulate fetching CSS from a backend service         
        String customCss =
                ".main-view { " +
                "   text-align: center; width: 500px !important; border-radius: 8px; margin-top: 40px !important; background-color: white; " +
                "   margin: 0 auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); " +
                "}" +
                ".header { text-align: center; margin-top: 20px; font-size: 1.6rem; font-weight: bolder; }" +
                ".location-display { text-align: center; font-size: 16px; margin-top: 10px; color: #a6aab3; }" +
                ".location-container { background-color: #eff6ff; padding: 20px; border-radius: 8px; text-align: center; }" +
                ".location-title { padding-top: 5px; margin: 0; font-size: 1.2rem; }" +
                ".timestamp-container { background-color: #f0fdf4; padding: 20px; border-radius: 8px; text-align: center; }" +
                ".timestamp-title { margin: 0; padding-top: 5px; font-size: 1.2rem; }" +
                ".latitude-display { color: green; margin: 1px 0; }" +
                ".longitude-display { color: blue; margin: 1px 0; }" +
                ".timestamp-display { color: orange; }" +
                "html, body { height: 100%; margin: 0; background: linear-gradient(45deg, #dbecfc, #dcfaea); }";
        // Store the custom CSS in VaadinSession for access in other views
        VaadinSession.getCurrent().setAttribute("customCss", customCss);

        String style = "<style>" + customCss + "</style>";
        getElement().executeJs("this.insertAdjacentHTML('beforeend', $0)", style);
    }

    private void updateLocationDetails(String position) {
        Pattern pattern = Pattern.compile("Position\\{\\[(.+?); (.+?)\\].*timestamp:(.+?)\\}");
        Matcher matcher = pattern.matcher(position);

        if (matcher.find()) {
            String latitude = matcher.group(1).trim();
            String longitude = matcher.group(2).trim();
            String timestamp = matcher.group(3).trim();

            latitudeDisplay.setText("Latitude: " + latitude);
            latitudeDisplay.addClassName("latitude-display");

            longitudeDisplay.setText("Longitude: " + longitude);
            longitudeDisplay.addClassName("longitude-display");

            timestampDisplay.setText("Timestamp: " + timestamp);
            timestampDisplay.addClassName("timestamp-display");
        }
    }
}
