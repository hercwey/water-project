package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "PRINTER_CONFIG")
public class PrinterConfig {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "ALIAS_NAME")
    private String aliasName;

    @Column(name = "PRINTER_NAME")
    private String printerName;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return ALIAS_NAME
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * @param aliasName
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName == null ? null : aliasName.trim();
    }

    /**
     * @return PRINTER_NAME
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     * @param printerName
     */
    public void setPrinterName(String printerName) {
        this.printerName = printerName == null ? null : printerName.trim();
    }

    /**
     * @return IP_ADDRESS
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", aliasName=").append(aliasName);
        sb.append(", printerName=").append(printerName);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append("]");
        return sb.toString();
    }
}