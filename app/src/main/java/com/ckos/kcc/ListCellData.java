package com.ckos.kcc;

public class ListCellData {

    private String NOSTR;
    private String COPSTR;

    public ListCellData(String NOSTR,String COPSTR){

        this.NOSTR = NOSTR;
        this.COPSTR = COPSTR;

    }

    @Override
    public String toString() {
        return getNOSTR()+"#"+getCOPSTR();
    }

    public String getNOSTR() {
        return NOSTR;
    }

    public void setNOSTR(String NOSTR) {
        this.NOSTR = NOSTR;
    }

    public String getCOPSTR() {
        return COPSTR;
    }

    public void setCOPSTR(String COPSTR) {
        this.COPSTR = COPSTR;
    }
}
