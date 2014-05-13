/*
 VCO.java

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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The VCO class implements a rudimentary, although instructive, Voltage Controlled
 * Oscillator.
 * 
 * <p>The VCO algorithm is based upon the equation:
 * 
 * <p><strong>f(t) = KV(t)</strong>
 * 
 * <p>Where:
 * <ul>
 * <li>f(t) is the output frequency of the waveform
 * <li>K is normally a constant whose units are Hertz/Volt
 * <li>V(t) is the input voltage as a function of time
 * </ul>
 *
 * <p>The primary objects of the implementation are:
 * <ul>
 * <li>Sample Input - this is the input voltage that represents V(t)
 * <li>Wave Form Input - this is the sampled wave for that the voltage controlled
 * oscillator modulates
 * <li>Sample Output - his is the final, frequency modulated output
 * </ul>
 * 
 * <p>Please note that this is a sample implementation that may be useful for
 * very low frequency implementations. Higher frequency implementations, such as
 * audio frequency output have been created with a similar set of functions but
 * the output function was more tightly integrated with the implementation (du
 * to timing constraints) and the VCO class was implemented as a Thread instead
 * of a TimerTask.
 * 
 * @author David G. Lister
 */
public class VCO extends TimerTask {

    private double K = 1.0;
    private static final double secondsPerMillisecond = 1e-3;
    private static final double dTwoPi = Math.PI * 2.0;
    private static final Logger logger = Logger.getLogger(VCO.class.getName());
    private double dCurrentVoltage = 0.0;
    private double dPhi = 0.0;
    private long currentTime = 0;
    private WaveFunction waveFunction;
    private InputSample filteredSample;
    private OutputListener outputListener;

    /**
     * The one and only constructor is meant to provide a simplified means to
     * introduce the voltage to frequency constant, the input sample object, the
     * input wave form object, and the output listening object.
     * 
     * @param KFactor this is the constant measured in Hertz/Volt to translate
     * input volts to frequency
     * @param waveFunction this is the object that converts radians (between 0
     * and 2PI to a sample of the wave function to output
     * @param sampleFunction the sample function object is polled for input "voltage"
     * samples to the VCO.
     * @param listener this is the "listener" that receives the output from the VCO.
     */
    public VCO(double KFactor, WaveFunction waveFunction, InputSample sampleFunction, OutputListener listener) {
        VCO.logger.entering(VCO.class.getName(), "VCO",
                new Object[]{new Double(KFactor), waveFunction, sampleFunction, listener});
        innerInit(KFactor, waveFunction, sampleFunction, listener);
    }

    private void innerInit(double KFactor, WaveFunction waveFunction, InputSample sampleFunction, OutputListener listener) {
        this.K = KFactor;
        this.waveFunction = waveFunction;
        this.filteredSample = sampleFunction;
        this.outputListener = listener;
    }

    @Override
    public void run() {
        String method = "VCO";
        double functionValue;
        double dVoltage;
        long time = System.currentTimeMillis();
        long deltaTime;
        double phi = 0.0;

        VCO.logger.entering(VCO.class.getName(), method);

        try {
            dVoltage = this.filteredSample.getSample();
            if (this.currentTime > 0) {
                deltaTime = time - this.currentTime;
                phi = Math.PI * this.K
                        * (this.dCurrentVoltage + dVoltage)
                        * ((double) deltaTime * VCO.secondsPerMillisecond)
                        + this.dPhi;
                if (phi > VCO.dTwoPi) {
                    double dGamma = Math.floor(phi / VCO.dTwoPi) * VCO.dTwoPi;
                    phi -= dGamma;
                }
                functionValue = this.waveFunction.getSample(phi);
                outputListener.sendOutput(time, functionValue);
            }
            this.currentTime = time;
            this.dCurrentVoltage = dVoltage;
            this.dPhi = phi;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }

        VCO.logger.exiting(VCO.class.getName(), method);
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        
        final long currentTime = System.currentTimeMillis();
        VCO vco;
        AverageTwoFilteredSample samplerFunction;
        WaveFunction waveFunction;
        OutputListener outputListener;
        Timer timer;
        
        class ConsoleOutputListener implements OutputListener {

            @Override
            public void sendOutput(long timeStamp, double value) {
                long time = timeStamp - currentTime;
                System.out.format("%10d", time);
                int spaces = (int) Math.floor((value + 1.0) * 40.0);
                for (int i = 0; i < spaces; i++) {
                    System.out.print(" ");
                }
                System.out.print("*\n");
            }
        }

        try {
            samplerFunction = new AverageTwoFilteredSample();
            waveFunction = new SineWave();
            outputListener = new ConsoleOutputListener();
            vco = new VCO(1.0, waveFunction, samplerFunction, outputListener);
            timer = new Timer("VCO Timer", true);
            timer.scheduleAtFixedRate(vco, 0, 100);

            for (int i = 0; i < 1000; i++) {
                samplerFunction.set((double) i / 1000.0);
                Thread.sleep(100);
            }

            Thread.sleep(1000);

            for (int i = 1000; i >= 0; i--) {
                samplerFunction.set((double) i / 1000.0);
                Thread.sleep(100);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
