package com.legartak.FirstTask;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    //as test were used example data
    public static void main(String... args) throws FileNotFoundException {
        File input = new File("input.xml");
        File output = new File("output.xml");

        SurnameUpdater.updateYourXml(input, output);
    }
}
