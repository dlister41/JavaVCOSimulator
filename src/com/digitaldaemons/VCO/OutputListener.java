/*
 OutputListener.java

The MIT License (MIT)

Copyright (c) 2014 Digital Daemons, Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

 */
package com.digitaldaemons.VCO;

/**
 * The OutputListener interface is very simple in nature. It provides a means
 to sendOutput the VCO output to some device, file, or output stream.
 * 
 * @author David G. Lister, Digital Daemons, Inc.
 */
public interface OutputListener {

    /**
     * The VCO output is sent via calls to the sendOutput() method. Each an every
 time the VCO calculates a new value, the output is sent via the sendOutput
 method.
     * 
     * @param timeStamp this is the time in milliseconds of the current sample as
     * returned by System.currentTimeMillis().
     * @param value this is the value of the output of the VCO output which
     * ranges between -1.0 and 1.0 as returned by the WaveFunction.
     */
    public void sendOutput(long timeStamp, double value);
}

