package com.tdtu.models;

import java.sql.Date;

public class Certificate {
    private int certificateId;
    private String certificateName;
    private Date issuedDate;

    // Constructor
    public Certificate(int certificateId, String certificateName, Date issuedDate) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.issuedDate = issuedDate;
    }

    // Getters and Setters
    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
}
