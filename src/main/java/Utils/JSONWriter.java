package Utils;

import Entities.Document;
import Entities.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JSONWriter {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void writeDocumentsToJson(List<Document> documents) throws IOException {
        Map<Person, List<Document>> map = documents.stream().sorted(Comparator.comparing(o -> o.getAuthor().getSurname()))
                .collect(Collectors.groupingBy(Document::getAuthor));
        TreeMap<Person, List<Document>> sorted = new TreeMap<>(map);

        for(Map.Entry<Person, List<Document>> item : sorted.entrySet()){
            FileWriter file = new FileWriter("src/main/resources/json/" + item.getKey().getFullName() + ".json");
            file.write(gson.toJson(item.getValue()));
            file.close();
        }
    }




}
