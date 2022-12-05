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

import esa.mo.platform.impl.provider.gen.GPSNMEAonlyAdapter;
import esa.mo.platform.impl.provider.gen.PowerControlAdapterInterface;
import opssat.simulator.main.ESASimulator;

import java.io.IOException;
import org.ccsds.moims.mo.platform.gps.structures.Position;
import org.ccsds.moims.mo.platform.gps.structures.SatelliteInfoList;
import org.orekit.propagation.analytical.tle.TLE;

/**
 * A simple GPS Adapter for testing purposes.
 *
 * @author Cesar Coelho
 */
public class GPSSoftSimAdapter extends GPSNMEAonlyAdapter {


    private ESASimulator instrumentsSimulator = null;
    private PowerControlAdapterInterface pcAdapter = null;

    public GPSSoftSimAdapter() {

    }

    public GPSSoftSimAdapter(ESASimulator instrumentsSimulator, PowerControlAdapterInterface pcAdapter)
    {
      this.instrumentsSimulator = instrumentsSimulator;
      this.pcAdapter = pcAdapter;
    }

    @Override
    public synchronized String getNMEASentence(final String sentenceIdentifier) throws IOException {
        return "123 Test";
    }

    @Override
    public boolean isUnitAvailable() {
        return true;
    }

    @Override
    public Position getCurrentPosition() {
        return new Position((float) 1, (float) 2, (float) 3, null);
    }

    @Override
    public SatelliteInfoList getSatelliteInfoList() {
        return null;
    }

    public TLE getTLE() {
        TLE tle = this.instrumentsSimulator.getSimulatorNode().getTLE();
        return tle;
    }
}
