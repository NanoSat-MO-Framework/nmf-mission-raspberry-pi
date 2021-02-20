/*
 * Copyright (c) 2021 Cesar Coelho
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package esa.mo.nmf.provider;

import esa.mo.helpertools.helpers.HelperAttributes;
import esa.mo.nmf.MCRegistration;
import esa.mo.nmf.MonitorAndControlNMFAdapter;
import esa.mo.sm.impl.util.ShellCommander;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.provider.MALInteraction;
import org.ccsds.moims.mo.mal.structures.Attribute;
import org.ccsds.moims.mo.mal.structures.Duration;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.IdentifierList;
import org.ccsds.moims.mo.mal.structures.LongList;
import org.ccsds.moims.mo.mal.structures.UInteger;
import org.ccsds.moims.mo.mal.structures.UOctet;
import org.ccsds.moims.mo.mal.structures.UShort;
import org.ccsds.moims.mo.mal.structures.Union;
import org.ccsds.moims.mo.mc.action.structures.ActionDefinitionDetails;
import org.ccsds.moims.mo.mc.action.structures.ActionDefinitionDetailsList;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterDefinitionDetails;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterDefinitionDetailsList;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterRawValueList;
import org.ccsds.moims.mo.mc.structures.ArgumentDefinitionDetails;
import org.ccsds.moims.mo.mc.structures.ArgumentDefinitionDetailsList;
import org.ccsds.moims.mo.mc.structures.AttributeValue;
import org.ccsds.moims.mo.mc.structures.AttributeValueList;
import org.ccsds.moims.mo.mc.structures.ConditionalConversionList;

/**
 * The Monitor and Control Adapter for the NanoSat MO Supervisor.
 *
 * @author cbwhi
 */
public class MCRaspberryPiAdapter extends MonitorAndControlNMFAdapter {

    private static final Logger LOGGER = Logger.getLogger(MCRaspberryPiAdapter.class.getName());

    private final static String DATE_PATTERN = "dd MMM yyyy HH:mm:ss.SSS";

    private static final String PARAMETER_CURRENT_PARTITION = "CurrentPartition";
    private static final String CMD_CURRENT_PARTITION = "mount -l | grep \"on / \" | grep -o 'mmc.*[0-9]p[0-9]'";

    private static final String PARAMETER_LINUX_VERSION = "LinuxVersion";
    private static final String CMD_LINUX_VERSION = "uname -a";
    private static final String CMD_LINUX_REBOOT = "reboot";

    private static final String ACTION_GPS_SENTENCE = "GPS_Sentence";
    private static final String ACTION_REBOOT = "Reboot_MityArm";
    private static final String ACTION_CLOCK_SET_TIME = "Clock.setTimeUsingDeltaMilliseconds";

    private final ShellCommander shellCommander = new ShellCommander();

    @Override
    public void initialRegistrations(MCRegistration registration) {
        registration.setMode(MCRegistration.RegistrationMode.DONT_UPDATE_IF_EXISTS);

        // ------------------ Parameters ------------------
        ParameterDefinitionDetailsList defs = new ParameterDefinitionDetailsList();
        IdentifierList paramIdentifiers = new IdentifierList();

        defs.add(new ParameterDefinitionDetails(
                "The Current partition where the OS is running.",
                Union.STRING_SHORT_FORM.byteValue(),
                "",
                false,
                new Duration(10),
                null,
                null
        ));
        paramIdentifiers.add(new Identifier(PARAMETER_CURRENT_PARTITION));

        defs.add(new ParameterDefinitionDetails(
                "The version of the software.",
                Union.STRING_SHORT_FORM.byteValue(),
                "",
                false,
                new Duration(10),
                null,
                null
        ));
        paramIdentifiers.add(new Identifier(PARAMETER_LINUX_VERSION));

        registration.registerParameters(paramIdentifiers, defs);

        ActionDefinitionDetailsList actionDefs = new ActionDefinitionDetailsList();
        IdentifierList actionIdentifiers = new IdentifierList();

        ArgumentDefinitionDetailsList arguments1 = new ArgumentDefinitionDetailsList();
        {
            Byte rawType = Attribute._STRING_TYPE_SHORT_FORM;
            String rawUnit = "NMEA sentence identifier";
            ConditionalConversionList conditionalConversions = null;
            Byte convertedType = null;
            String convertedUnit = null;

            arguments1.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
                    rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
        }

        ActionDefinitionDetails actionDef1 = new ActionDefinitionDetails(
                "Injects the NMEA sentence identifier into the CAN bus.",
                new UOctet((short) 0),
                new UShort(0),
                arguments1
        );

        ArgumentDefinitionDetailsList arguments2 = new ArgumentDefinitionDetailsList();
        {
            Byte rawType = Attribute._LONG_TYPE_SHORT_FORM;
            String rawUnit = "milliseconds";
            ConditionalConversionList conditionalConversions = null;
            Byte convertedType = null;
            String convertedUnit = null;

            arguments2.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
                    rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
        }

        ActionDefinitionDetails actionDef2 = new ActionDefinitionDetails(
                "Sets the clock using a diff between the on-board time and the desired time.",
                new UOctet((short) 0),
                new UShort(0),
                arguments2
        );

        ActionDefinitionDetails actionDef3 = new ActionDefinitionDetails(
                "Reboots the mityArm.",
                new UOctet((short) 0),
                new UShort(0),
                new ArgumentDefinitionDetailsList()
        );

        actionDefs.add(actionDef1);
        actionDefs.add(actionDef2);
        actionDefs.add(actionDef3);
        actionIdentifiers.add(new Identifier(ACTION_GPS_SENTENCE));
        actionIdentifiers.add(new Identifier(ACTION_CLOCK_SET_TIME));
        actionIdentifiers.add(new Identifier(ACTION_REBOOT));

        LongList actionObjIds = registration.registerActions(actionIdentifiers, actionDefs);
    }

    @Override
    public Attribute onGetValue(Identifier identifier, Byte rawType) {
        if (PARAMETER_CURRENT_PARTITION.equals(identifier.getValue())) {
            String msg = shellCommander.runCommandAndGetOutputMessage(CMD_CURRENT_PARTITION);
            return (Attribute) HelperAttributes.javaType2Attribute(msg);
        } else if (PARAMETER_LINUX_VERSION.equals(identifier.getValue())) {
            String msg = shellCommander.runCommandAndGetOutputMessage(CMD_LINUX_VERSION);
            return (Attribute) HelperAttributes.javaType2Attribute(msg);
        }
        return null;
    }

    @Override
    public Boolean onSetValue(IdentifierList identifiers, ParameterRawValueList values) {
        return false;  // to confirm that no variable was set
    }

    @Override
    public UInteger actionArrived(Identifier name, AttributeValueList attributeValues,
            Long actionInstanceObjId, boolean reportProgress, MALInteraction interaction) {
        if (ACTION_GPS_SENTENCE.equals(name.getValue())) {
            return null; // Success!
        }

        if (ACTION_REBOOT.equals(name.getValue())) {
            ShellCommander shell = new ShellCommander();
            String output = shell.runCommandAndGetOutputMessage(CMD_LINUX_REBOOT);
            LOGGER.log(Level.INFO, "Output: " + output);

            return null; // Success!
        }

        if (ACTION_CLOCK_SET_TIME.equals(name.getValue())) {
            if (attributeValues.isEmpty()) {
                return new UInteger(0); // Error!
            }

            AttributeValue aVal = attributeValues.get(0); // Extract the delta!
            long delta = (Long) HelperAttributes.attribute2JavaType(aVal.getValue());

            String str = (new SimpleDateFormat(DATE_PATTERN)).format(new Date(System.currentTimeMillis() + delta));

            ShellCommander shell = new ShellCommander();
            shell.runCommand("date -s \"" + str + " UTC\" | hwclock --systohc");

            return null; // Success!
        }

        return new UInteger(1);  // Action service not integrated
    }

}
