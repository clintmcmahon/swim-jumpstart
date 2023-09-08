package com.l3harris.swim.FlightData;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "tfmDataService", namespace = "urn:us:gov:dot:faa:atm:tfm:tfmdataservice")
@XmlAccessorType(XmlAccessType.FIELD)
public class TfmsFlightDataService {
    @XmlElement(name = "fltdOutput")
    private FltdOutput fltdOutput;

    // Getters and setters
}
