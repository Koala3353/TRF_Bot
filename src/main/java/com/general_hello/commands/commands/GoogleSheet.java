package com.general_hello.commands.commands;

import com.general_hello.Config;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.Challenge;
import com.general_hello.commands.Objects.Level.Level;
import com.general_hello.commands.Objects.Level.Rank;
import com.general_hello.commands.Objects.Player;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class GoogleSheet {
    private static final String APPLICATION_NAME = "Pvp bot";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheet.class);

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheet.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("online")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Prints the names and majors of students in a spreadsheet:
     */
    public static void writeData(TextChannel textChannel, Challenge challenge) {
        // Build a new authorized API client service.
        try {
            String enumNameFromChannel = Level.getEnumNameFromChannel(textChannel);
            int rangeRetrieved = DataUtils.getRange(enumNameFromChannel);
            if (rangeRetrieved == -1) {
                DataUtils.newRange(enumNameFromChannel);
                rangeRetrieved = DataUtils.getRange(enumNameFromChannel);
            }
            final String range = enumNameFromChannel + "!A" + rangeRetrieved;
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final String spreadsheetId = Config.get("spreadsheet");
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            Player challenger = new Player(challenge.getChallenger().getUserid(), enumNameFromChannel);
            Player acceptor = new Player(challenge.getAcceptor().getUserid(), enumNameFromChannel);
            int pointsWinChallenger = 0;
            int pointsLossChallenger = 0;
            int pointsWinAcceptor = 0;
            int pointsLossAcceptor = 0;
            if (challenge.getWinner().getUserid() == challenge.getWinner().getUserid()) {
                pointsWinChallenger = Rank.getPointsWin(Rank.getRankFromPoints(challenge.getWinner().getPoints()), Rank.getRankFromPoints(challenge.getLoser().getPoints()));
                pointsLossAcceptor = Rank.getPointsLost(Rank.getRankFromPoints(challenge.getWinner().getPoints()), Rank.getRankFromPoints(challenge.getLoser().getPoints()));
            } else {
                pointsLossChallenger = Rank.getPointsLost(Rank.getRankFromPoints(challenge.getWinner().getPoints()), Rank.getRankFromPoints(challenge.getLoser().getPoints()));
                pointsWinAcceptor = Rank.getPointsWin(Rank.getRankFromPoints(challenge.getWinner().getPoints()), Rank.getRankFromPoints(challenge.getLoser().getPoints()));
            }
            List<List<Object>> values = List.of(
                    Arrays.asList(
                            enumNameFromChannel + "_" + OffsetDateTime.now().getDayOfMonth() + OffsetDateTime.now().getSecond(), challenge.getChallenger().getUser().getAsTag(),
                            challenge.getChallenger().getPoints(), Rank.getRankFromPoints(challenge.getChallenger().getPoints()).getName(),
                            challenger.getPoints(), Rank.getRankFromPoints(challenger.getPoints()).getName(),
                            challenge.getAcceptor().getUser().getAsTag(), challenge.getAcceptor().getPoints(),
                            Rank.getRankFromPoints(challenge.getAcceptor().getPoints()).getName(), acceptor.getPoints(),
                            Rank.getRankFromPoints(acceptor.getPoints()).getName(), challenge.getWinner().getUser().getAsTag(),
                            challenge.getLoser().getUser().getAsTag(), pointsWinChallenger, pointsLossChallenger, pointsWinAcceptor, pointsLossAcceptor
                    )
            );
            DataUtils.setRange(enumNameFromChannel);
            ValueRange body = new ValueRange()
                    .setValues(values);
            service.spreadsheets().values().append(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } catch (Exception e) {
            LOGGER.error("An error occurred", e) ;
        }
    }
}
