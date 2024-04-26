package diplrad.helpers;

import diplrad.constants.Constants;
import diplrad.constants.ErrorMessages;
import diplrad.exceptions.InvalidFileException;
import diplrad.exceptions.ReadFromFileException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileReader {

    public static List<String> readCandidatesFromFile() throws InvalidFileException, ReadFromFileException {
        return readPeopleFromFile(Constants.CANDIDATES_FILE_PATH);
    }

    public static List<String> readVotersFromFile() throws InvalidFileException, ReadFromFileException {
        return readPeopleFromFile(Constants.VOTERS_FILE_PATH);
    }

    private static List<String> readPeopleFromFile(String filePath) throws ReadFromFileException, InvalidFileException {
        List<String> candidates = null;
        try {
            candidates = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ReadFromFileException(ErrorMessages.readFromFileErrorMessage);
        }

        Set<String> candidatesUnique = new HashSet<>(candidates);

        if (candidates.size() != candidatesUnique.size()) {
            throw new InvalidFileException(ErrorMessages.duplicateEntriesInFileErrorMessage);
        }

        return candidates;
    }

}
