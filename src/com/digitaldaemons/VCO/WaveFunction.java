/*
 VCOWaveFunction.java

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
 * This interface defines the wave function that the VCO outputs. The input is
 * the measurement in radians and the output is the sample value between -1.0 and
 * 1.0 corresponding to the radians.
 *
 * @author David G. Lister, Digital Daemons, Inc.
 */
public interface WaveFunction {

    /**
     * The getSample() is the one and only publish method for the WaveFunction
     * interface. The purpose of the method is to map radians (between 0 and 2PI)
     * to a sampled waveform value. The waveform may be anything from a function
     * to a table lookup.
     * 
     * @param radians the measure of where the sample is to be taken between 0
     * and 2 Pi radians.
     *
     * @return the normalized sample corresponding to the radians between -1.0
     * and 1.0.
     */
    public double getSample(double radians);

}
