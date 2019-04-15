package main;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class FileOperator {
    final private static String storageDir = "./storage/";

    /* Gets */

    static String getStorageDir() {
        return storageDir;
    }

    /* **** */

    static class Basic {

        static boolean createFiles(String[] files) {
            for (String file : files) {
                try {
                    File f = new File(storageDir + file.toLowerCase());
                    if (f.exists()) f.delete();
                    f.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            }
            return true;
        }

        static boolean writeIntoFile(String filename, String text) {
            return writeIntoFile(new File(filename), text);
        }

        static boolean writeIntoFile(File filename, String text) {
            return writeIntoFile(filename, text, false);
        }

        static boolean writeIntoFile(String filename, String text, boolean append) {
            return writeIntoFile(new File(filename), text, append);
        }

        static boolean writeIntoFile(File filename, String text, boolean append) {
            new File(storageDir).mkdir();

            try {
                FileWriter fw = new FileWriter(FileOperator.storageDir + filename, append);
                fw.write(text);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
                Data.Processing.throwError("Can't write info file: " + filename.getName());
                return false;
            }

            return true;
        }


        static ArrayList<String> readFile(String filename) {
            return readFile(new File(storageDir + filename.toLowerCase()));
        }

        static ArrayList<String> readFile(File filename) {
            Scanner reader = null;
            try {
                reader = new Scanner(new FileReader(filename));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Data.Processing.throwError("File not found: " + filename.getName());
            }
            assert reader != null;

            ArrayList<String> lines = new ArrayList<>();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.trim().equals("")) continue;

                lines.add(line);
            }

            return lines;
        }

        static void deleteFromFile(String filename, int line) {
            ArrayList<String> fileText = readFile(filename);
            assert fileText != null;

            fileText.remove(line);

            String newFileText = Additional.convertStrListToString(fileText);
            writeIntoFile(filename, newFileText);
        }

        static void replaceInStorage(String filename, int index, String param) {
            ArrayList<String> fileText = readFile(filename);
            assert fileText != null;

            fileText.set(index, param);

            String newFileText = Additional.convertStrListToString(fileText);
            writeIntoFile(filename, newFileText);
        }
    }

    static class Additional {
        static boolean exists(String file) {
            File f = new File(file.toLowerCase());
            return f.isFile() && f.exists();
        }

        static long calculateStorageLength() {
            File dir = new File(storageDir);
            File[] dirs = dir.listFiles();

            if ((dirs != null ? dirs.length : 0) < 1) return 0;

            long bytes = 0;
            for (File f : dirs) {
                if (f.getName().equals(".metadata")) continue;
                bytes += f.length();
            }
            return bytes;
        }

        static boolean validateMetaFile() {
            new File(storageDir).mkdir();
            File meta = new File(storageDir + ".metadata");

            if (!meta.exists()) return false;

            Data.Metadata.updateMetaFile();

            return true;
        }

        static int[] convertListToArrayInt(ArrayList<Integer> list) {
            int[] result = new int[list.size()];
            for (int i = 0, len = list.size(); i < len; i++) {
                result[i] = list.get(i);
            }
            return result;
        }

        static String convertStrListToString(ArrayList<String> list) {
            StringBuilder newString = new StringBuilder();

            for (String line : list) {
                newString
                        .append(line)
                        .append("\t");
            }

            return newString.toString();
        }

        /**
         * @param filename name of file
         * @return T <Int> count of lines in file
         */
        static int countLinesInFile(File filename) {
            ArrayList<String> list = Basic.readFile(filename);
            list.size();
            return Basic.readFile(filename).size();
        }

        static int countLinesInFile(String filename) {
            return countLinesInFile(new File(FileOperator.storageDir + filename));
        }

        static int countLinesInStorage() {
            return countLinesInFile("_id");
        }

        static HashMap<String, ArrayList<String>> getAllDocuments() {
            HashMap<String, ArrayList<String>> allDocuments = new HashMap<>();

            for (String attribute : Data.Metadata.ATTRIBUTES) {
                allDocuments.put(attribute, FileOperator.Basic.readFile(attribute));
            }

            return allDocuments;
        }

    }
}
