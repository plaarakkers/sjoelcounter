package nl.gellygwin.imageprocessing.sjoelengui;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import nl.gellygwin.imageprocessing.sjoelengui.configuration.SjoelenGuiConfiguration;
import nl.gellygwin.imageprocessing.sjoelengui.glass.Glass;

/**
 *
 * GlassController
 */
public class GlassController {

    private static final String APPROVAL_URL_LOCATION_START = "https://accounts.google.com/o/oauth2/approval";

    private static final String APPROVAL_URL_SUCCESS_START = "Success code=";

    private static final List<String> GOOGLE_API_SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/glass.timeline");

    private static final String GOOGLE_API_REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    
    private static final List<String> GOOGLE_API_RESPONSE_TYPES = Arrays.asList("code");

    private final SjoelenGuiConfiguration sjoelenGuiConfiguration;
    
    private final String googleOauthUrl;
    
    private Glass glass;

    @FXML
    private WebView webView;

    private Stage stage;

    public GlassController() {
        this.sjoelenGuiConfiguration = new SjoelenGuiConfiguration();
        this.googleOauthUrl = new GoogleAuthorizationCodeRequestUrl(sjoelenGuiConfiguration.getGoogleApiClientId(), GOOGLE_API_REDIRECT_URI, GOOGLE_API_SCOPES).setResponseTypes(GOOGLE_API_RESPONSE_TYPES).build();
    }
    
    @FXML
    private void initialize() {
         webView.getEngine().load(googleOauthUrl);
         
         webView.getEngine().getLoadWorker().stateProperty().addListener(this::loadListener);
    }

    private void loadListener(ObservableValue ov, State oldState, State newState) {
        try {
            if (newState == State.SUCCEEDED) {
                WebEngine webEngine = webView.getEngine();
                //Check the results of authorization if on the correct url.
                if (webEngine.getLocation().startsWith(APPROVAL_URL_LOCATION_START)) {
                    //Only get the credentials and initialize the timeline when authorized.
                    if (webEngine.getTitle().startsWith(APPROVAL_URL_SUCCESS_START)) {
                        //The authorization code is the second part of the title.
                        String authorizationCode = webEngine.getTitle().split("=")[1];

                        HttpTransport httpTransport = new NetHttpTransport();
                        JacksonFactory jsonFactory = new JacksonFactory();

                        GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory, sjoelenGuiConfiguration.getGoogleApiClientId(), sjoelenGuiConfiguration.getGoogleApiClientSecret(), authorizationCode, GOOGLE_API_REDIRECT_URI);

                        GoogleTokenResponse tokenResponse = tokenRequest.execute();
                        Credential credential = new GoogleCredential.Builder().setClientSecrets(sjoelenGuiConfiguration.getGoogleApiClientId(), sjoelenGuiConfiguration.getGoogleApiClientSecret())
                                .setTransport(httpTransport).setJsonFactory(jsonFactory).build().setFromTokenResponse(tokenResponse);
                        
                        glass = new Glass(credential);
                    }

                    stage.close();
                }
            }
        } catch (IOException e) {
            throw new SjoelenGuiException(e.getMessage());
        }
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * @return the glass
     */
    public Glass getGlass() {
        return glass;
    }
}
