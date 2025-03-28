package dk.easv.ticketapptest.BLL.util;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.transport.http.HttpCredentialsAdapter;
import com.google.auth.transport.apache.HttpTransportFactory;
import com.google.http.client.googleapis.javanet.GoogleNetHttpTransport;

import java.io.IOException;
import java.util.Collections;

public class GmailService {

    private static final String APPLICATION_NAME = "Ticket System App"; // You can change this to your app's name
    private static final String CLIENT_SECRET_FILE = "path_to_your_credentials_file.json"; // Path to your JSON credentials file

    public static Gmail getGmailService() throws IOException {
        // Load credentials from the JSON file
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CLIENT_SECRET_FILE))
                .createScoped(Collections.singleton(GmailScopes.GMAIL_SEND));

        // Build the Gmail service
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new Gmail.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
