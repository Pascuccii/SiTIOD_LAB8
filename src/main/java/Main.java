import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String URI = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&ssl=false";
            MongoClientURI clientURI = new MongoClientURI(URI);
            MongoClient mongoClient = new MongoClient(clientURI);
            MongoDatabase db = mongoClient.getDatabase("cpdb");
            MongoCollection<Document> collection = db.getCollection("log");
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/file_for_LAB8.csv"));

            for (String line : lines) {
                String[] parsedLine = line.split(",");
                System.out.println("CSV: " + Arrays.toString(parsedLine));
                JSONObject json = new JSONObject("{" +
                        "'URL':'" + parsedLine[0].trim() + "', " +
                        "'IP':'" + parsedLine[1].trim() + "', " +
                        "'timeStemp':'" + parsedLine[2].trim() + "', " +
                        "'timeSpent':'" + parsedLine[3].trim() + "'," +
                        "}");
                System.out.println("JSON: " + json.toString() + "\n");
                Document dbObject = Document.parse(json.toString());
                collection.insertOne(dbObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
