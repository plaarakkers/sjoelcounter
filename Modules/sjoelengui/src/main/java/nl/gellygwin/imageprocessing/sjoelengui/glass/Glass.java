package nl.gellygwin.imageprocessing.sjoelengui.glass;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.Mirror.Timeline.Insert;
import com.google.api.services.mirror.Mirror.Timeline.Update;
import com.google.api.services.mirror.model.TimelineItem;
import java.io.IOException;
import javafx.collections.ObservableList;
import nl.gellygwin.imageprocessing.sjoelengui.SjoelenGuiException;
import nl.gellygwin.imageprocessing.sjoelengui.models.RoundResult;

/**
 *
 * Glass
 */
public class Glass {

    private final Credential credential;

    private String timelineItemId;

    public Glass(Credential credential) {
        this.credential = credential;
    }

    public void init() {
        TimelineItem timelineItem = new TimelineItem();
        timelineItem.setHtml("<article><p>Sjoelcounter gekoppeld</p></article>");

        try {
            Insert insert = getMirrorService().timeline().insert(timelineItem);
            timelineItemId = insert.execute().getId();
        } catch (IOException e) {
            throw new SjoelenGuiException(e.getMessage());
        }
    }

    public void updateResult(ObservableList<RoundResult> resultList) {
        StringBuilder builder = new StringBuilder();
        if (resultList.size() > 0) {
            builder.append("<article><ul>");
            resultList.stream().forEachOrdered(result -> builder.append(String.format("<li>Ronde: %d Score: %d</li>", result.getRound(), result.getScore())));
            builder.append("</ul></article>");
        }
        
        TimelineItem timelineItem = new TimelineItem();        
        timelineItem.setHtml(builder.toString());

         try {
            Update update = getMirrorService().timeline().update(timelineItemId, timelineItem);
            update.execute();
        } catch (IOException e) {
            throw new SjoelenGuiException(e.getMessage());
        }
    }

    private Mirror getMirrorService() {
        return new Mirror.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName("Sjoelcounter").build();
    }

}
