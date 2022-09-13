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
package esa.mo.platform.impl.util;

import esa.mo.com.impl.util.COMServicesProvider;
import esa.mo.platform.impl.provider.gen.ArtificialIntelligenceProviderServiceImpl;
import esa.mo.platform.impl.provider.gen.PowerControlProviderServiceImpl;
import esa.mo.platform.impl.provider.gen.CameraProviderServiceImpl;
import esa.mo.platform.impl.provider.gen.GPSProviderServiceImpl;
import esa.mo.platform.impl.provider.gen.AutonomousADCSProviderServiceImpl;
import esa.mo.platform.impl.provider.gen.OpticalDataReceiverProviderServiceImpl;
import esa.mo.platform.impl.provider.gen.SoftwareDefinedRadioProviderServiceImpl;
import esa.mo.platform.impl.provider.raspberrypi.CameraSingleImageAdapter;
import esa.mo.platform.impl.provider.raspberrypi.GPSSoftSimAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.platform.artificialintelligence.provider.ArtificialIntelligenceInheritanceSkeleton;

/**
 *
 *
 */
public class PlatformServicesProviderRaspberryPi implements PlatformServicesProviderInterface {

    private final static Logger LOGGER = Logger.getLogger(PlatformServicesProviderRaspberryPi.class.getName());

    private final ArtificialIntelligenceProviderServiceImpl aiService
            = new ArtificialIntelligenceProviderServiceImpl();

    private final AutonomousADCSProviderServiceImpl adcsService
            = new AutonomousADCSProviderServiceImpl();

    private final CameraProviderServiceImpl cameraService
            = new CameraProviderServiceImpl();

    private final GPSProviderServiceImpl gpsService
            = new GPSProviderServiceImpl();

    private final OpticalDataReceiverProviderServiceImpl optrxService
            = new OpticalDataReceiverProviderServiceImpl();

    private final PowerControlProviderServiceImpl powerService
            = new PowerControlProviderServiceImpl();

    private final SoftwareDefinedRadioProviderServiceImpl sdrService
            = new SoftwareDefinedRadioProviderServiceImpl();

    public void init(COMServicesProvider comServices) throws MALException {
        try {
            GPSSoftSimAdapter gpsAdapter = new GPSSoftSimAdapter();
            gpsService.init(comServices, gpsAdapter);
            cameraService.init(comServices, new CameraSingleImageAdapter());
        } catch (UnsatisfiedLinkError | NoClassDefFoundError | NoSuchMethodError error) {
            LOGGER.log(Level.SEVERE, "Could not load platform adapters "
                    + "(check for missing JARs and libraries)", error);
        }
    }

    @Override
    public ArtificialIntelligenceInheritanceSkeleton getAIService() {
        return this.aiService;
    }

    @Override
    public GPSProviderServiceImpl getGPSService() {
        return this.gpsService;
    }

    @Override
    public CameraProviderServiceImpl getCameraService() {
        return this.cameraService;
    }

    @Override
    public AutonomousADCSProviderServiceImpl getAutonomousADCSService() {
        return this.adcsService;
    }

    @Override
    public OpticalDataReceiverProviderServiceImpl getOpticalDataReceiverService() {
        return this.optrxService;
    }

    @Override
    public SoftwareDefinedRadioProviderServiceImpl getSoftwareDefinedRadioService() {
        return this.sdrService;
    }

}
