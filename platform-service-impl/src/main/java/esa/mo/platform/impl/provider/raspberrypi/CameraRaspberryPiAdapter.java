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
package esa.mo.platform.impl.provider.raspberrypi;

import esa.mo.platform.impl.provider.gen.CameraAdapterInterface;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.structures.Duration;
import org.ccsds.moims.mo.mal.structures.UInteger;
import org.ccsds.moims.mo.platform.camera.structures.CameraSettings;
import org.ccsds.moims.mo.platform.camera.structures.Picture;
import org.ccsds.moims.mo.platform.camera.structures.PictureFormat;
import org.ccsds.moims.mo.platform.camera.structures.PictureFormatList;
import org.ccsds.moims.mo.platform.camera.structures.PixelResolution;
import org.ccsds.moims.mo.platform.camera.structures.PixelResolutionList;

/**
 *
 * @author Cesar Coelho
 */
public class CameraRaspberryPiAdapter implements CameraAdapterInterface {

    private static final Duration PREVIEW_EXPOSURE_TIME = new Duration(0.100); // 100ms
    private static final Duration MINIMUM_PERIOD = new Duration(1); // 1 second for now...
    private final PictureFormatList supportedFormats = new PictureFormatList();
    private int nativeImageLength;
    private int nativeImageWidth;
    private boolean unitAvailable = false;

    private static final Logger LOGGER = Logger.getLogger(CameraRaspberryPiAdapter.class.getName());

    public CameraRaspberryPiAdapter() {
        supportedFormats.add(PictureFormat.RAW);
        supportedFormats.add(PictureFormat.RGB24);
        supportedFormats.add(PictureFormat.BMP);
        supportedFormats.add(PictureFormat.PNG);
        supportedFormats.add(PictureFormat.JPG);
        LOGGER.log(Level.INFO, "Initialisation");

        unitAvailable = true;
    }

    @Override
    public boolean isUnitAvailable() {
        return unitAvailable;
    }

    @Override
    public String getExtraInfo() {
        return "";
    }

    @Override
    public PixelResolutionList getAvailableResolutions() {
        PixelResolutionList availableResolutions = new PixelResolutionList();

        return availableResolutions;
    }

    @Override
    public synchronized Picture getPicturePreview() {
        final PixelResolution resolution = new PixelResolution(new UInteger(nativeImageWidth),
                new UInteger(nativeImageLength));
        return null;
    }

    @Override
    public Duration getMinimumPeriod() {
        return MINIMUM_PERIOD;
    }

    @Override
    public PictureFormatList getAvailableFormats() {
        return supportedFormats;
    }

    @Override
    public Picture takePicture(CameraSettings settings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Picture takeAutoExposedPicture(CameraSettings settings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
