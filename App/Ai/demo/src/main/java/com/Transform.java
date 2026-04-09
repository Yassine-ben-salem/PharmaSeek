package com;

import java.util.ArrayList;
import java.io.*;
import java.util.regex.*;
import java.util.Arrays;
import java.util.HashMap;

class config {
    public static final String filepath = "/home/ayoub/Desktop/PFA-files/App/Ai/demo/src/main/resources/tunisian_medicine_words.csv";
    int[] n = { 2, 3, 4 };
    public static final String NGramPath = "/home/ayoub/Desktop/PFA-files/App/Ai/demo/src/main/resources/wordsNGrams.txt";
    public static final int[] range = { 2, 3, 4 };
    public static final String invertedIndexPath = "/home/ayoub/Desktop/PFA-files/App/Ai/demo/src/main/resources/invertedIndex.txt";
}

public class Transform {
    public static void generateNGrams(String path, int[] n) {
        File src = new File(path);
        ArrayList<String> nGrams = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(src));
            FileWriter writer = new FileWriter(new File(config.NGramPath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(",");
                for (int i = 0; i < tokens.length; i++) {
                    String word = tokens[i].trim().toLowerCase();
                    if (word.isEmpty())
                        continue;
                    for (int k = 0; k < n.length; k++) {
                        for (int j = 0; j < word.length() - n[k] + 1; j++) {
                            String nGram = word.substring(j, j + n[k]);
                            nGrams.add(nGram);
                        }
                        writer.write(word + ": " + nGrams.toString() + "\n");
                        nGrams.clear();
                    }
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void invertedIndexNGrams() {
        HashMap<String, ArrayList<String>> invertedIndexStruct = new HashMap<>();
        File src = new File(config.NGramPath);
        String word = null;
        String[] nGrams = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(src));
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern p = Pattern.compile(".+(?=:)");
                Matcher m = p.matcher(line);
                if (m.find()) {
                    word = m.group();
                }
                p = Pattern.compile("(?<=\\[).+(?=\\])");
                m = p.matcher(line);
                if (m.find()) {
                    nGrams = m.group().split(", ");
                }
                for (String ngram : nGrams) {
                    if (invertedIndexStruct.containsKey(ngram)) {
                        invertedIndexStruct.get(ngram).add(word);
                    } else {
                        invertedIndexStruct.put(ngram, new ArrayList<String>(Arrays.asList(word)));
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(invertedIndexStruct.toString());
        File invertedIndexFile = new File(config.invertedIndexPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(invertedIndexFile));
            for (String ngram : invertedIndexStruct.keySet()) {
                writer.write(ngram + ": " + invertedIndexStruct.get(ngram).toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generateNGrams(config.filepath, config.range);
        invertedIndexNGrams();
    }
}
