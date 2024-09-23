package org.vaadin.example;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.vaadin.elmot.flow.sensors.GeoLocation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A sample Vaadin view class.
 */
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
        setUpCustomCss(); // Inject custom CSS

        // Header for the application
        H1 header = new H1("GeoStyler");
        header.getElement().getStyle().set("text-align", "center");
        header.getElement().getStyle().set("margin-top", "20px");
        
        // Display for showing location updates
        Span locationDisplay = new Span("Waiting for location...");
        locationDisplay.getElement().getClassList().add("locationstyle");
        locationDisplay.getElement().getStyle().set("text-align", "center");
        locationDisplay.getElement().getStyle().set("font-size", "20px");
        locationDisplay.getElement().getStyle().set("margin-top", "10px");

        // Setup GeoLocation component
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setWatch(true);
        geoLocation.setHighAccuracy(true);
        geoLocation.setTimeout(100000);
        geoLocation.setMaxAge(200000);
        
        // Handle successful location retrieval
        geoLocation.addValueChangeListener(e -> {
            String position = e.getValue().toString();
            locationDisplay.setText("Location: " + position);
            updateLocationDetails(position);
        });
        
        // Handle location errors
        geoLocation.addErrorListener(e -> {
            locationDisplay.setText("Location: Error retrieving location");
        });

        // Add components to layout
        add(header, locationDisplay, latitudeDisplay, longitudeDisplay, timestampDisplay, geoLocation);

        // Optional: Styling
        setMargin(true);
        setSpacing(true);
        getElement().getStyle().set("text-align", "center");
    }

    private void setUpCustomCss() {
        // Simulate fetching CSS from a backend service
        String customCss = ".locationstyle { font-size: 16px; color: darkgreen; }"; // Example CSS

        // Inject the fetched CSS into the document
        String style = "<style>" + customCss + "</style>";
        getElement().executeJs("this.insertAdjacentHTML('beforeend', $0)", style);
    }

    private void updateLocationDetails(String position) {
        // Regex to extract latitude, longitude, and timestamp
        Pattern pattern = Pattern.compile("Position\\{\\[(.+?); (.+?)\\].*timestamp:(.+?)\\}");
        Matcher matcher = pattern.matcher(position);
        
        if (matcher.find()) {
            String latitude = matcher.group(1).trim();
            String longitude = matcher.group(2).trim();
            String timestamp = matcher.group(3).trim();

            // Update displays with different colors
            latitudeDisplay.setText("Latitude: " + latitude);
            latitudeDisplay.getElement().getStyle().set("color", "green");

            longitudeDisplay.setText("Longitude: " + longitude);
            longitudeDisplay.getElement().getStyle().set("color", "blue");

            timestampDisplay.setText("Timestamp: " + timestamp);
            timestampDisplay.getElement().getStyle().set("color", "orange");
        }
    }
}
