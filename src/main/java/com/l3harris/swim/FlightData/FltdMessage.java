package com.l3harris.swim.FlightData;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class FltdMessage {
    @XmlAttribute(name = "acid")
    private String acid;
    @XmlAttribute(name = "airline")
    private String airline;
    // ... more attributes here

    // @XmlElement(name = "trackInformation", namespace =
    // "urn:us:gov:dot:faa:atm:tfm:flightdata")
    // private TrackInformation trackInformation;

    // Getters and setters
}
