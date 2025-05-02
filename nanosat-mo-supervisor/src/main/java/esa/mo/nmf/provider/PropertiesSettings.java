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

import java.util.Properties;
import org.ccsds.moims.mo.mal.helpertools.helpers.HelperMisc;

/**
 *
 * @author Cesar Coelho
 */
public class PropertiesSettings {

    public static Properties getProperties() {
        Properties props = new Properties();

        // To form the Network zone
        props.put(HelperMisc.PROP_ORGANIZATION_NAME, "nmf");
        props.put(HelperMisc.PROP_MISSION_NAME, "rpi");
        props.put(HelperMisc.PROP_NETWORK_ZONE, "space");
        props.put(HelperMisc.PROP_DEVICE_NAME, "Raspberry_Pi");

        // Set the name of the MAL classes to use
        props.put("org.ccsds.moims.mo.mal.factory.class", "esa.mo.mal.impl.MALContextFactoryImpl");

        return props;
    }

}
