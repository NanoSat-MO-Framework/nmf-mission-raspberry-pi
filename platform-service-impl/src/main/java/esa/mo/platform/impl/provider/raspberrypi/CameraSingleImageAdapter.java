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

import esa.mo.helpertools.helpers.HelperTime;
import esa.mo.platform.impl.provider.gen.CameraAdapterInterface;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.structures.Blob;
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
public class CameraSingleImageAdapter implements CameraAdapterInterface {

    private final static Duration MINIMUM_DURATION = new Duration(10); // 10 seconds for now...
    private final static UInteger IMAGE_LENGTH = new UInteger(2048);
    private final static UInteger IMAGE_WIDTH = new UInteger(1944);

    public CameraSingleImageAdapter() {
    }

    @Override
    public String getExtraInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PixelResolutionList getAvailableResolutions() {
        PixelResolutionList availableResolutions = new PixelResolutionList();
        // Only one:
        availableResolutions.add(new PixelResolution(IMAGE_LENGTH, IMAGE_WIDTH));

        // Insert the Available Resolutions
        return availableResolutions;
    }

    @Override
    public synchronized Picture getPicturePreview() {
        return null;
    }

    @Override
    public Picture takePicture(CameraSettings settings) throws IOException {
        // Eiffel Tower (example)
        Picture picture = new Picture();
        picture.setTimestamp(HelperTime.getTimestampMillis());
        picture.setSettings(settings);

        try {
            String fileName = "picture_demo.jpg";
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream in = classLoader.getResourceAsStream(fileName);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            
            picture.setContent(new Blob(response));
        } catch (java.net.UnknownHostException ex) {
            Logger.getLogger(CameraSingleImageAdapter.class.getName()).log(
                    Level.SEVERE, "Maybe there is no internet?", ex);
            throw new IOException(ex); // Wrap into an IOException
        } catch (MalformedURLException ex) {
            Logger.getLogger(CameraSingleImageAdapter.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CameraSingleImageAdapter.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return picture;
    }

    @Override
    public Duration getMinimumPeriod() {
        return MINIMUM_DURATION;
    }

    @Override
    public boolean isUnitAvailable() {
        return true;
    }

    @Override
    public PictureFormatList getAvailableFormats() {
        PictureFormatList list = new PictureFormatList();
        list.add(PictureFormat.RAW);
        list.add(PictureFormat.JPG);
        list.add(PictureFormat.PNG);
        return list;
    }

    @Override
    public Picture takeAutoExposedPicture(CameraSettings settings) throws IOException, MALException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
