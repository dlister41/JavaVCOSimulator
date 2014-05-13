/*
 AverageTwoFilteredSample.java

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
 * The AverageTwoFilteredSample implements a example implementation of
 * FilteredSample. The getSample() method returns the average of the two most
 * recent samples the current sample value set by the the set() method and the
 * last sample returned by getSample(). This effectively creates a very simple
 * low pass filter to reduce VCO output instabilities.
 * 
 * @author David G. Lister, Digital Daemons, Inc.
 */
public class AverageTwoFilteredSample implements InputSample {

    private double lastSample = 0.0;
    private double currentSample = 0.0;

    /**
     * The getSample() method returns the average of the last sample returned by
     * the previous call to getSample() and the current value set by the caller
     * of set().
     * 
     * @return
     */
    @Override
    public double getSample() {
        double sample;
        synchronized (this) {
            sample = (lastSample + currentSample) / 2.0;
            lastSample = sample;
        }
        return (sample);
    }

    /**
     * The set() method sets the current or target value for the filter.
     * 
     * @param sample
     */
    public void set(double sample) {
        synchronized (this) {
            this.currentSample = sample;
        }
    }
}
