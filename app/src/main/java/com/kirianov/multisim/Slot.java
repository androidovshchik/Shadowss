package com.kirianov.multisim;

import java.util.List;

public class Slot {

    private String imei;
    private String imsi;
    // private String msisdn; // not posible now

    private int simState;
    private String simSerialNumber;
    private String simOperator;
    private String simOperatorName;
    private String simCountryIso;

    private String networkOperator;
    private String networkOperatorName;
    private String networkCountryIso;
    private int networkType;
    // careful find networkTypeName because it will be different with networkType on same devices
    private boolean networkRoaming;


    public Slot() {
        simState = -1;
    }

    public String getImei() {
        return imei;
    }

    public int getSimState() {
        return simState;
    }

    public String getImsi() {
        return imsi;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public String getSimOperator() {
        return simOperator;
    }

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public String getSimCountryIso() {
        return simCountryIso;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public String getNetworkCountryIso() {
        return networkCountryIso;
    }

    public int getNetworkType() {
        return networkType;
    }

    public boolean isNetworkRoaming() {
        return networkRoaming;
    }


    void setImei(String imei) {
        this.imei = imei;
    }

    void setSimState(Integer simState) {
        if (simState == null) {
            this.simState = -1;
            return;
        }
        this.simState = simState;
    }

    void setImsi(String imsi) {
        this.imsi = imsi;
    }

    void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    void setSimCountryIso(String simCountryIso) {
        this.simCountryIso = simCountryIso;
    }

    void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    void setNetworkCountryIso(String networkCountryIso) {
        this.networkCountryIso = networkCountryIso;
    }

    void setNetworkType(Integer networkType) {
        if (networkType == null) {
            this.networkType = 0;
            return;
        }
        this.networkType = networkType;
    }

    void setNetworkRoaming(Boolean networkRoaming) {
        if (networkRoaming == null) {
            this.networkRoaming = false;
            return;
        }
        this.networkRoaming = networkRoaming;
    }

    public String toString() {
        return "imei=[" + imei +
                "] imsi=[" + imsi +
                "] simState=[" + simState +
                "] simSerialNumber=[" + simSerialNumber +
                "] simOperator=[" + simOperator +
                "] simOperatorName=[" + simOperatorName +
                "] simCountryIso=[" + simCountryIso +
                "] networkOperator=[" + networkOperator +
                "] networkOperatorName=[" + networkOperatorName +
                "] networkCountryIso=[" + networkCountryIso +
                "] networkType=[" + networkType +
                "] networkRoaming=[" + networkRoaming + "]";
    }

    public boolean compare(Slot slot) {
        if (slot == null) return false;
        // if (("" + getImei()).compareTo("" + slot.getImei()) == 0) return true;
        return ((("" + getImei()).compareTo("" + slot.getImei()) == 0) &&
                (("" + getImsi()).compareTo("" + slot.getImsi()) == 0) &&
                (("" + getSimSerialNumber()).compareTo("" + slot.getSimSerialNumber()) == 0) // &&
                // (("" + getSimOperator()).compareTo("" + slot.getSimOperator()) == 0) &&
                // (("" + getSimOperatorName()).compareTo("" + slot.getSimOperatorName()) == 0) &&
                // (("" + getSimCountryIso()).compareTo("" + slot.getSimCountryIso()) == 0) &&
                // (getSimState() == slot.getSimState()) &&
                // (("" + getSimSerialNumber()).compareTo("" + slot.getSimSerialNumber()) == 0) &&
                // (("" + getNetworkOperator()).compareTo("" + slot.getNetworkOperator()) == 0) &&
                // (("" + getNetworkOperatorName()).compareTo("" + slot.getNetworkOperatorName()) == 0) &&
                // (("" + getNetworkCountryIso()).compareTo("" + slot.getNetworkCountryIso()) == 0) &&
                // (getNetworkType() == slot.getNetworkType()) &&
                // (isNetworkRoaming() == slot.isNetworkRoaming())
        );
    }

    public int indexIn(List<Slot> slots) {
        if (slots == null) return -1;
        for (int i = 0; i < slots.size(); i++)
            if (this.compare(slots.get(i)))
                return i;
        return -1;
    }

    public boolean containsIn(List<Slot> slots) {
        if (slots == null) return false;
        for (Slot slot : slots)
            if (this.compare(slot))
                return true;
        return false;
    }

}
