
import com.bbn.openmap.LayerHandler;
import com.bbn.openmap.MapBean;
import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.MultipleSoloMapComponentException;
import com.bbn.openmap.event.OMMouseMode;
import com.bbn.openmap.gui.LayerControlButtonPanel;
import com.bbn.openmap.gui.LayersPanel;
import com.bbn.openmap.gui.MapPanel;
import com.bbn.openmap.gui.NavigatePanel;
import com.bbn.openmap.gui.OpenMapFrame;
import com.bbn.openmap.gui.OverlayMapPanel;
import com.bbn.openmap.gui.ScaleTextPanel;
import com.bbn.openmap.gui.ToolPanel;
import com.bbn.openmap.gui.ZoomPanel;
import com.bbn.openmap.layer.imageTile.MapTileLayer;
import com.bbn.openmap.proj.coords.LatLonPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JFrame;

public class OpenMapWindow {

    private Map<String, LatLonPoint> locationMap;
    private MapBean mapBean;
    private JFrame mapFrame;

    public OpenMapWindow() {
        initializeLocationMap(); // Initialize the location map

        try {
            // Initialize the map panel
            MapPanel mapPanel = new OverlayMapPanel();

            // Add default OpenMap components to the panel
            mapPanel.addMapComponent(new LayerHandler());
            mapPanel.addMapComponent(new MouseDelegator());
            mapPanel.addMapComponent(new OMMouseMode());
            mapPanel.addMapComponent(new ToolPanel());
            mapPanel.addMapComponent(new LayersPanel());
            mapPanel.addMapComponent(new LayerControlButtonPanel());
            mapPanel.addMapComponent(new ScaleTextPanel());
            mapPanel.addMapComponent(new NavigatePanel());
            mapPanel.addMapComponent(new ZoomPanel());

            // Add OpenStreetMap tile layer
            MapTileLayer mapTileLayer = createMapTileLayer();
            mapPanel.addMapComponent(mapTileLayer);

            // Add RasterLayer
            RasterLayer rasterLayer = new RasterLayer();
            rasterLayer.addImage("C:/Users/Nada2/OneDrive - King Abdullaziz University/Desktop/Project cpit305/rr.png");// Replace with the actual image path
            mapPanel.addMapComponent(rasterLayer);

            // Set up and display the map
            mapFrame = new OpenMapFrame("OpenMap");
            mapFrame.setSize(800, 600);
            mapPanel.addMapComponent(mapFrame);

            // Get the MapBean from the panel
            mapBean = mapPanel.getMapBean();

            mapFrame.setVisible(true);

        } catch (MultipleSoloMapComponentException e) {
            e.printStackTrace();
        }
    }

    private void initializeLocationMap() {
        // Initialize the location-to-coordinates mapping
        locationMap = new HashMap<>();
        locationMap.put("Riyadh", new LatLonPoint.Double(24.7136, 46.6753));
        locationMap.put("Jeddah", new LatLonPoint.Double(21.4858, 39.1925));
        locationMap.put("Dhahran", new LatLonPoint.Double(26.2361, 50.0393));
        // Add more locations as needed
    }

    private MapTileLayer createMapTileLayer() {
        // Create and configure the OpenStreetMap tile layer
        MapTileLayer mapTileLayer = new MapTileLayer();
        Properties tileLayerProps = new Properties();
        tileLayerProps.put("prettyName", "OpenStreetMap Tiles");
        tileLayerProps.put("tileFactory", "com.bbn.openmap.dataAccess.mapTile.StandardMapTileFactory");
        tileLayerProps.put("baseURL", "https://tile.openstreetmap.org");
        tileLayerProps.put("rootDir", "c:CachDir"); // Cache directory

        // Debugging: Log tile layer properties
        System.out.println("Tile Layer Properties: " + tileLayerProps);

        mapTileLayer.setProperties(tileLayerProps);
        return mapTileLayer;
    }

   public void showMapWithLocation(String location) {
    // Example image bounds (update these with the actual bounds of your image)
    double ullat = 21.729; // Upper-left latitude of the image
    double ullon = 39.092; // Upper-left longitude of the image
    double lrlat = 21.348; // Lower-right latitude of the image
    double lrlon = 39.359; // Lower-right longitude of the image

    // Calculate the center of the image
    double centerLat = (ullat + lrlat) / 2;
    double centerLon = (ullon + lrlon) / 2;

    if (mapBean != null) {
        // Set the map center to the image's center
        mapBean.setCenter(new LatLonPoint.Double(centerLat, centerLon));
        // Set the scale to match 1:2,500,000
        mapBean.setScale(2500000f);
    }

    // Show the map window
    if (mapFrame != null) {
        mapFrame.setVisible(true);
    }
}

    public static void main(String[] args) {
        // Launch the OpenRemoteLayer application
        javax.swing.SwingUtilities.invokeLater(() -> {
            OpenMapWindow openRemoteLayer = new OpenMapWindow();
            openRemoteLayer.showMapWithLocation("Riyadh"); // Example usage
        });
    }
}
