package de.algoviz.algoviz.util.data_file_manager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to read and write data from the data_resources folder
 *
 * @author Benedikt
 * @version 1.0
 */
public final class DataFileManager {

    private static final String PATH_PREFIX = "./src/main/resources/data_resources/";
    private static final String NEW_LINE = "\n";

    private static final Pattern RELATIVE_PATH_PATTERN = Pattern.compile("(\\w*/)*");
    private static final Pattern DATA_NAME_PATTERN = Pattern.compile("\\w*.[a-zA-Z]*");

    private DataFileManager() {
    }

    /**
     * read a data in the specified folder and return the input as string
     *
     * @param relativePath relative path from the data out of the dataResource folder
     * @param dataName     name from the data including the ending
     * @return the content of the data as string
     * @throws IOException throws this exception if the file at these path doesn't exist.
     *                     Or if it isn't allowed to read the file
     */
    public static String getData(String relativePath, String dataName) throws IOException {
        if (illegalArgument(relativePath, dataName)) {
            throw new FileNotFoundException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fileReader = new FileReader(PATH_PREFIX + relativePath + dataName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine());
            stringBuilder.append(NEW_LINE);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    /**
     * insert a NEW data file into the specified folder. If necessary new folder were also inserted
     *
     * @param relativePath relative path from the data out of the dataResource folder
     * @param dataName     name from the data including the ending
     * @param dataContent  the content of the data
     * @throws IOException throws this exception if the file at these path already exist.
     *                     Or if it isn't allowed to insert the file here
     */
    public static void insertData(String relativePath, String dataName, String dataContent) throws IOException {
        if (illegalArgument(relativePath, dataName)) {
            throw new FileNotFoundException();
        }
        File folder = new File(PATH_PREFIX + relativePath);
        File file = new File(PATH_PREFIX + relativePath + dataName);
        Path path = Paths.get(PATH_PREFIX + relativePath + dataName);

        if (!folder.exists()) {
            Files.createDirectories(path.getParent());
        }
        if (file.exists()) {
            throw new FileNotFoundException();
        }
        Files.write(path, dataContent.getBytes());
    }

    /**
     * delete the file in the specified folder.
     *
     * @param relativePath relative path from the data out of the dataResource folder
     * @param dataName     name from the data including the ending
     * @throws FileNotFoundException throws this exception if the file at these path doesn't exist.
     *                               Or if it isn't allowed to delete a file here.
     */
    public static void removeData(String relativePath, String dataName) throws FileNotFoundException {
        if (illegalArgument(relativePath, dataName)) {
            throw new FileNotFoundException();
        }
        File file = new File(PATH_PREFIX + relativePath + dataName);
        File folder = file.getParentFile();
        if (!file.delete()) {
            throw new FileNotFoundException();
        }
        boolean delete;
        do {
            delete = false;
            File[] children = folder.listFiles();
            if ((children == null) || (children.length == 0)) {
                File parent = folder.getParentFile();
                delete = folder.delete();
                folder = parent;
            }
        } while (delete);
    }

    /**
     * write a new content into an existing file in the specified folder.
     *
     * @param relativePath relative path from the data out of the dataResource folder
     * @param dataName     name from the data including the ending
     * @param dataContent  the content of the data
     * @throws IOException throws this exception if the file at these path doesn't exist.
     *                     Or if it isn't allowed to change the file
     */
    public static void actualiseData(String relativePath, String dataName, String dataContent) throws IOException {
        if (illegalArgument(relativePath, dataName)) {
            throw new FileNotFoundException();
        }
        Path directoryPath = Paths.get(PATH_PREFIX + relativePath + dataName);
        File file = new File(PATH_PREFIX + relativePath + dataName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        Files.write(directoryPath, dataContent.getBytes());
    }

    /**
     * check, whether the input matches to the pattern
     *
     * @param relativePath path input
     * @param dataName     data name input
     * @return returns true if one input does NOT match
     */
    private static boolean illegalArgument(String relativePath, String dataName) {
        Matcher relativePathMatcher = RELATIVE_PATH_PATTERN.matcher(relativePath);
        Matcher dataNameMatcher = DATA_NAME_PATTERN.matcher(dataName);
        return ((!relativePathMatcher.matches()) || (!dataNameMatcher.matches()));
    }
}
