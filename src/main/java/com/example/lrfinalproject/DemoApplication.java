package com.example.lrfinalproject;

import com.example.lrfinalproject.databases.XmlDocParse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) throws SQLException, ParserConfigurationException, SAXException, ParseException, IOException {
        Controller control = new Controller();
        SpringApplication.run(DemoApplication.class, args);
    }
}
