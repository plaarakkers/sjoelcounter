package nl.gellygwin.imageprocessing.sjoelengui.configuration;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import nl.gellygwin.imageprocessing.sjoelengui.SjoelenGuiException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 *
 * SjoelenGuiConfiguration
 */
public class SjoelenGuiConfiguration {
    
    private static final String CONFIGURATION_FILE = "sjoelenguiconfig.xml";

    private XMLConfiguration configuration;

    public SjoelenGuiConfiguration() {
        try {
            configuration = new XMLConfiguration(CONFIGURATION_FILE);
            configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            throw  new SjoelenGuiException(String.format("Probleem met het laden van [%s].", CONFIGURATION_FILE), e);
        }
    }
    
    public CaptureType getCaptureType() {
        String captureTypeConfig = configuration.getString("Capturing.CaptureType");
        if (captureTypeConfig == null || captureTypeConfig.isEmpty()) {
            throw new SjoelenGuiException("Geen video capture type geconfigureerd.");
        }
        
        Optional<CaptureType> result = Arrays.stream( CaptureType.values()).filter( v -> v.name().equalsIgnoreCase(captureTypeConfig)).findAny();
        if (result.isPresent()) {
            return result.get();
        }
                
        throw new SjoelenGuiException("Geen correcte video capture type geconfigureerd.");
    }
    
    public String getImageCaptureFile() {
        String imageCaptureFile = configuration.getString("Capturing.ImageCaptureFile");
        if (imageCaptureFile == null || imageCaptureFile.isEmpty()) {
            throw new SjoelenGuiException("Geen image capture file geconfigureerd.");
        }
        
        return imageCaptureFile;
    }
    
    public String getVideoCaptureFile() {
        String imageCaptureFile = configuration.getString("Capturing.VideoCaptureFile");
        if (imageCaptureFile == null || imageCaptureFile.isEmpty()) {
            throw new SjoelenGuiException("Geen video capture file geconfigureerd.");
        }
        
        return imageCaptureFile;
    }
    
    public int getWebcamCaptureDevice() {
        try {
            return configuration.getInt("Capturing.WebcamCaptureDevice");
        }
        catch (ConversionException | NoSuchElementException e) {
             throw new SjoelenGuiException("Geen webcam capture device geconfigureerd.", e);
        }
    }
    
    public String getGoogleApiClientId() {
        String googleApiClientId = configuration.getString("GoogleApi.ClientId");
        if (googleApiClientId == null || googleApiClientId.isEmpty()) {
            throw new SjoelenGuiException("Geen google api client id geconfigureerd.");
        }
        
        return googleApiClientId;
    }
    
    public String getGoogleApiClientSecret() {
        String googleApiClientSecret = configuration.getString("GoogleApi.ClientSecret");
        if (googleApiClientSecret == null || googleApiClientSecret.isEmpty()) {
            throw new SjoelenGuiException("Geen google api client secret geconfigureerd.");
        }
        
        return googleApiClientSecret;
    }
}
