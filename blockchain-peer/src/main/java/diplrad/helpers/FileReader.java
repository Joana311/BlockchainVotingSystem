package diplrad.helpers;

import diplrad.constants.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileReader {

    public static List<String> readCandidatesFromFile() {
        return readPeopleFromFile(Constants.CANDIDATES_FILE_PATH);
    }

    public static List<String> readVotersFromFile() {
        return readPeopleFromFile(Constants.VOTERS_FILE_PATH);
    }

    private static List<String> readPeopleFromFile(String filePath) {
        List<String> candidates = null;
        try {
            candidates = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Unable to read from file.");
            System.exit(1);
        }

        Set<String> candidatesUnique = new HashSet<>(candidates);

        if (candidates.size() != candidatesUnique.size()) {
            System.out.println("Duplicate entries found in the file.");
            System.exit(1);
        }

        return candidates;
    }

}
