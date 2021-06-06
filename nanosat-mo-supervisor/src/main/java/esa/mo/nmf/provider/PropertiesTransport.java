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

/**
 *
 * @author Cesar Coelho
 */
public class PropertiesTransport {
    
    
    public static Properties getProperties(){
        Properties props = new Properties();
        
        //The following sets the default protocol used
        props.put("org.ccsds.moims.mo.mal.transport.default.protocol", "maltcp://");
        // props.put("org.ccsds.moims.mo.mal.transport.default.protocol", "malhttp://");
        // props.put("org.ccsds.moims.mo.mal.transport.default.protocol", "rmi://");

        //The following sets the secondary protocol used
        // props.put("org.ccsds.moims.mo.mal.transport.secondary.protocol", "rmi://");

        // TCP-IP
        props.put("org.ccsds.moims.mo.mal.transport.protocol.maltcp", "esa.mo.mal.transport.tcpip.TCPIPTransportFactoryImpl");
        props.put("org.ccsds.moims.mo.mal.encoding.protocol.maltcp", "esa.mo.mal.encoder.binary.fixed.FixedBinaryStreamFactory");
        // props.put("org.ccsds.moims.mo.mal.encoding.protocol.maltcp", "esa.mo.mal.encoder.binary.split.SplitBinaryStreamFactory");
        props.put("org.ccsds.moims.mo.mal.transport.tcpip.autohost", "true");
        
        //props.put("org.ccsds.moims.mo.mal.transport.tcpip.host", "xxx.xxx.xxx.xxx");
        //props.put("org.ccsds.moims.mo.mal.transport.tcpip.port", "54321");
        //props.put("org.ccsds.moims.mo.mal.transport.tcpip.isServer", "true");

        return props;
    }
    
}
