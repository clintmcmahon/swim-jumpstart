package com.l3harris.swim.FlightData;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class FltdOutput {
    @XmlElement(name = "fltdMessage", namespace = "urn:us:gov:dot:faa:atm:tfm:flightdata")
    private List<FltdMessage> fltdMessages;

    // Getters and setters
}
