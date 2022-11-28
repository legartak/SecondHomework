package com.legartak.FirstTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurnameUpdater {
    public static void updateYourXml(File inputXML, File outputXML) {
        Scanner scanner = null;

        final Pattern namePattern = Pattern.compile("(\\bname\\b\\s*=\\s*)\"\\s*(\\S*[^ ])\\s*\"");
        final Pattern surnamePattern = Pattern.compile("\\bsurname\\b\\s*=\\s*\"\\s*(\\S*[^ ])\\s*\"");

        try {
            scanner = new Scanner(inputXML);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder result = new StringBuilder();
        StringBuilder temporary = new StringBuilder();

        String name = null, surname = null, introduction = null;

        while(scanner.hasNextLine()) {
            String string = scanner.nextLine();

            if (string.contains("<persons>") || string.contains("</persons>")) {
                result.append(string).append(System.lineSeparator());
                continue;
            }

            if(string.contains("/>")) {
                temporary.append(string);
                Matcher surnameMatcher = surnamePattern.matcher(temporary);
                if (surnameMatcher.find()) {
                    surname = surnameMatcher.group(1);
                }
                temporary = new StringBuilder(surnameMatcher.replaceAll(""));

                Matcher nameMatcher = namePattern.matcher(temporary);
                if (nameMatcher.find()) {
                    introduction = nameMatcher.group(1);
                    name = nameMatcher.group(2);
                }

                temporary = new StringBuilder(nameMatcher.replaceAll(
                        introduction + "\"" + name + " " + surname + "\""
                ));
                temporary.append(System.lineSeparator());
                result.append(temporary);
            }

            if(!string.contains("/>")) {
                temporary.append(string).append(System.lineSeparator());
                continue;
            }
            temporary = new StringBuilder();
        }
        scanner.close();

        try {
            FileWriter fileWriter = new FileWriter(outputXML);
            fileWriter.write(result.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
