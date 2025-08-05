package com.raymedis.rxviewui.modules.study.params;


public  class XrayParams implements Cloneable {
    private  double mAs;
    private  double mA;
    private  double KV;
    private double ms;


    //constructors
    public XrayParams() {
    }

    public XrayParams(double mAs, double mA, double KV, double ms) {
        this.mAs = mAs;
        this.mA = mA;
        this.KV = KV;
        this.ms = ms;
    }

    //setters and getters
    public double getmAs() {
        return mAs;
    }

    public void setmAs(double mAs) {
        this.mAs = mAs;
    }

    public double getmA() {
        return mA;
    }

    public void setmA(double mA) {
        this.mA = mA;
    }

    public double getKV() {
        return KV;
    }

    public void setKV(double KV) {
        this.KV = KV;
    }

    public double getMs() {
        return ms;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    @Override
    public String toString() {
        return "XrayParams{" +
                "mAs=" + mAs +
                ", mA=" + mA +
                ", KV=" + KV +
                ", ms=" + ms +
                '}';
    }


    @Override
    public XrayParams clone() {
        try {
            return (XrayParams) super.clone(); // Shallow copy is sufficient (all fields are primitives)
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("XrayParams cloning failed", e);
        }
    }

}
