/*
 * Copyright (c) 2020 L3Harris Technologies
 */
package com.l3harris.swim.outputs.custom;

import com.l3harris.swim.FlightData.TfmsFlightDataService;
import com.l3harris.swim.outputs.Output;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.gov.dot.faa.atm.tfm.tfmdataservice.TfmDataService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TfmsDataMySQLOutput extends Output {
    private static Logger logger = LoggerFactory.getLogger(TfmsDataMySQLOutput.class);

    private JAXBContext jaxbContext;
    private Connection connection;

    public TfmsDataMySQLOutput(Config config) {
        super(config);
        Config postgresConfig = config.getConfig("mysql");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    postgresConfig.getString("uri"),
                    postgresConfig.getString("user"),
                    postgresConfig.getString("password"));

            logger.info("Connection to mysql successful");

        } catch (Exception e) {
            logger.error("Failed to connect and/or setup mysql", e);
            System.exit(-1);
        }

        try {
            jaxbContext = JAXBContext.newInstance(TfmsFlightDataService.class);
        } catch (JAXBException e) {
            logger.error("Failed to create the JAXBContext", e);
            System.exit(-1);
        }
    }

    @Override
    public void output(String message) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            TfmsFlightDataService tfmDataService = (TfmsFlightDataService) unmarshaller
                    .unmarshal(XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(message)));

            logger.info(tfmDataService.toString());
            /*
             * switch (tfmDataService.getFiOutput().getFiMessage().getMsgType()) {
             * case RSTR:
             * new RestrictionMessage(connection).insert(tfmDataService.getFiOutput().
             * getFiMessage());
             * break;
             * case APTC:
             * new
             * AirportConfigurationMessage(connection).insert(tfmDataService.getFiOutput().
             * getFiMessage());
             * }
             */
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
