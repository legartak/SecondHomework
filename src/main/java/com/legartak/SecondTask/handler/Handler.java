package com.legartak.SecondTask.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import com.legartak.SecondTask.app.Application;
import com.legartak.SecondTask.entity.Fine;
import com.legartak.SecondTask.entity.Type;
import com.legartak.SecondTask.entity.Violator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Handler {
    final static String DATA_FOLDER = "data/fines/";

    @JsonProperty("formed")
    public String formed;

    @JsonProperty("traffic_violations")
    public List<Violator> violations;

    public Handler() {

    }

    private Handler(String formed, List<Violator> violations) {
        this.formed = formed;
        this.violations = violations;
    }

    private Handler(List<Violator> violations) {
        this.violations = violations;
    }

    public static void printOutFinesByAmount() throws IOException {

        List<List<Handler>> newViolations = getViolators();
        Map<Fine, Type> map = new HashMap<>();
        String fileName = "fines_by_amount.xml";

        try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FOLDER + "statistic/" + fileName))) {
            for (List<Handler> list : newViolations) {
                for (Handler vHandler : list) {
                    for (Violator violator : vHandler.violations) {
                        if (map.containsValue(violator.getType())) {
                            for (Map.Entry<Fine, Type> entry : map.entrySet()) {
                                if (entry.getKey().getType().equals(violator.getType())) {
                                    entry.getKey().setFineAmount(entry.getKey().getFineAmount() + violator.getFineAmount());
                                }
                            }
                        } else {
                            map.put(new Fine(violator.getType(), violator.getFineAmount()), violator.getType());
                        }
                    }
                }
            }

            Map<String, Integer> resultMap = new HashMap<>();
            for (Map.Entry<Fine, Type> entry : map.entrySet()) {
                resultMap.put(entry.getKey().getType().toString(), entry.getKey().getFineAmount());
            }

            Map<String, Integer> sortedResultMap = resultMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(5).collect(Collectors
                            .toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o2, LinkedHashMap::new));

            Application app = new Application();
            app.setEntry(sortedResultMap);

            // xml output format
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            out.println(xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(app));

        }
    }

    public static List getViolators() {

        ObjectMapper objectMapper = new ObjectMapper();

        File folder = new File(DATA_FOLDER);
        File[] listOfFiles = folder.listFiles();
        List<Handler> list = new LinkedList<>();
        List<List<Handler>> newViolations = new LinkedList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(Thread.currentThread().getName() + ": " + file.getName() + " processed");
                try {
                    list = objectMapper.readValue(file, new TypeReference<List<Handler>>() {
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            newViolations.add(list);
        }

        return newViolations;
    }
}
