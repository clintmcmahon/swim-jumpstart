/*
 * Copyright (c) 2020 L3Harris Technologies
 */
package com.l3harris.swim.outputs.custom.tfmsFlow;

import com.l3harris.swim.outputs.custom.SqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.gov.dot.faa.atm.tfm.ficommonmessages.RestrictionType;
import us.gov.dot.faa.atm.tfm.flowinformation.FiOutputType;

import javax.xml.bind.JAXBElement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RestrictionMessage {
    private static Logger logger = LoggerFactory.getLogger(RestrictionMessage.class);

    private PreparedStatement ps;
    private String sql = "INSERT INTO RSTR VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public RestrictionMessage(Connection connection) {
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(FiOutputType.FiMessage message) throws SQLException {
        String sourceFacility = message.getSourceFacility();
        Timestamp sourceTimestamp = SqlHelper.fromGregorianCalendar(message.getSourceTimeStamp());
        String sensitivity = message.getSensitivity().value();

        // Parse the attributes
        ps.setString(1, sourceFacility);
        ps.setTimestamp(2, sourceTimestamp);
        ps.setString(3, sensitivity);

        // Parse the restriction
        RestrictionType restriction = message.getRestrictionMessage();
        Timestamp eventTime = SqlHelper.fromGregorianCalendar(restriction.getEventTime());
        Timestamp entryTime = SqlHelper.fromGregorianCalendar(restriction.getEntryTime());
        String facility = restriction.getFacility();
        Integer action = restriction.getAction() == null ? 0 : restriction.getAction().getValue().intValue();
        Long restrictionId = restriction.getRestrictionId();
        String restrictedNasElements = restriction.getRestrictedNasElements() == null ? null
                : restriction.getRestrictedNasElements().getValue();

        Timestamp startTime = SqlHelper.fromGregorianCalendar(restriction.getStartTime());
        Timestamp stopTime = SqlHelper.fromGregorianCalendar(restriction.getStopTime());
        String airports = restriction.getAirports() == null ? null : restriction.getAirports().getValue();

        String aircraftType = restriction.getAircraftType();
        Integer restrictionType = restriction.getRestrictionType().intValue();
        Long mitNumber = restriction.getMitNumber() == null ? 0L : restriction.getMitNumber().getValue();
        Integer mitValue = restriction.getMitValue() == null ? 0 : restriction.getMitValue().getValue().intValue();

        String altitude = restriction.getAltitude() == null ? null : restriction.getAltitude().getValue();
        Integer speed = restriction.getSpeed() == null ? 0 : restriction.getSpeed().getValue().intValue();
        String reasonText = restriction.getReasonText();
        String impactedElements = restriction.getImpactedElements() == null ? null
                : restriction.getImpactedElements().getValue();
        String qualifier = restriction.getQualifier() == null ? null : restriction.getQualifier().getValue();
        String passback = restriction.getPassback();
        String exclusions = restriction.getExclusions() == null ? null : restriction.getExclusions().getValue();

        // put some protection on approval time?
        Timestamp approvalTime = SqlHelper.fromGregorianCalendar(jaxbElementValue(restriction.getApprovalTime()));

        String providerStatus = restriction.getProviderStatus();
        String referenceRestrictionEndtime = jaxbElementValue(restriction.getReferenceRestrictionEndTime());
        Long refreenceRestrictionId = restriction.getReferenceRestrictionId();
        String remarks = restriction.getRemarks() == null ? null : restriction.getRemarks().getValue();

        ps.setTimestamp(4, eventTime);
        ps.setTimestamp(5, entryTime);
        ps.setString(6, facility);
        ps.setInt(7, action);
        ps.setLong(8, restrictionId);
        ps.setString(9, restrictedNasElements);
        ps.setTimestamp(10, startTime);
        ps.setTimestamp(11, stopTime);
        ps.setString(12, airports);
        ps.setString(13, aircraftType);
        ps.setInt(14, restrictionType);
        ps.setLong(15, mitNumber);
        ps.setInt(16, mitValue);
        ps.setString(17, altitude);
        ps.setLong(18, speed);
        ps.setString(19, reasonText);
        ps.setString(20, impactedElements);
        ps.setString(21, qualifier);
        ps.setString(22, passback);
        ps.setString(23, exclusions);
        ps.setTimestamp(24, approvalTime);
        ps.setString(25, providerStatus);
        ps.setString(26, referenceRestrictionEndtime);
        ps.setLong(27, refreenceRestrictionId);
        ps.setString(28, remarks);

        // Execute the updates
        ps.executeUpdate();
    }

    private <T> T jaxbElementValue(JAXBElement<T> element) {
        return (element == null || element.isNil()) ? null : element.getValue();
    }
}