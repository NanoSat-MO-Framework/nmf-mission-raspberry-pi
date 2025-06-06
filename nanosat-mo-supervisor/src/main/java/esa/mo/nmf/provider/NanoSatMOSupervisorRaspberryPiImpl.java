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

import esa.mo.com.impl.util.COMServicesProvider;
import esa.mo.nmf.MonitorAndControlNMFAdapter;
import esa.mo.nmf.nanosatmosupervisor.NanoSatMOSupervisor;
import esa.mo.nmf.nmfpackage.NMFPackagePMBackend;
import esa.mo.platform.impl.util.PlatformServicesConsumer;
import esa.mo.platform.impl.util.PlatformServicesProviderInterface;
import esa.mo.platform.impl.util.PlatformServicesProviderRaspberryPi;
import esa.mo.platform.impl.util.PlatformServicesProviderSoftSim;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;

/**
 * The implementation of the NanoSat MO Supervisor for a Raspberry Pi.
 *
 * @author Cesar Coelho
 */
public final class NanoSatMOSupervisorRaspberryPiImpl extends NanoSatMOSupervisor {

    //  private PlatformServicesProviderSoftSim platformServicesProviderSoftSim;
    private PlatformServicesProviderInterface platformServicesRaspberryPi;

    /**
     * Main command line entry point.
     *
     * @param args the command line arguments
     * @throws java.lang.Exception if there is an error
     */
    public static void main(final String args[]) throws Exception {
        NanoSatMOSupervisorRaspberryPiImpl supervisor = new NanoSatMOSupervisorRaspberryPiImpl();
        supervisor.init(new MCRaspberryPiAdapter(supervisor));
    }

    @Override
    public void init(final MonitorAndControlNMFAdapter mcAdapter) {
        // Set the properties specific to this mission
        Properties defaultprops = System.getProperties();
        defaultprops.putAll(PropertiesTransport.getProperties());
        defaultprops.putAll(PropertiesSettings.getProperties());
        defaultprops.putAll(PropertiesProvider.getProperties());

        Properties systemProps = System.getProperties();
        systemProps.putAll(defaultprops);
        System.setProperties(systemProps);

        super.init(mcAdapter,
                new PlatformServicesConsumer(),
                null
        );
    }

    @Override
    public void initPlatformServices(COMServicesProvider comServices) {
        try {
            // TODO: choose based on system properties
            if (true) {
                platformServicesRaspberryPi = new PlatformServicesProviderRaspberryPi();
            } else {
                platformServicesRaspberryPi = new PlatformServicesProviderSoftSim();
            }
            platformServicesRaspberryPi.init(comServices);

        } catch (MALException ex) {
            Logger.getLogger(NanoSatMOSupervisorRaspberryPiImpl.class.getName()).log(
                    Level.SEVERE, "Something went wrong!", ex);
        }
    }

    @Override
    protected void startStatusTracking() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
