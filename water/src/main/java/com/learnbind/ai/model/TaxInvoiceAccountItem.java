package com.learnbind.ai.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "TAX_INVOICE_ACCOUNT_ITEM")
public class TaxInvoiceAccountItem {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT TAX_ACCOUNT_ITEM_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "COMBIN_ACCOUNT_ITEM")
    private String combinAccountItem;

    @Column(name = "COMBIN_ACCOUNT_ITEM_SUM")
    private BigDecimal combinAccountItemSum;

    @Column(name = "USE_PRICE")
    private Integer usePrice;

    @Column(name = "INVOICE_AMOUNT")
    private BigDecimal invoiceAmount;

    @Column(name = "WATER_NOTIFY_IDS")
    private String waterNotifyIds;

    @Column(name = "INVOICES")
    private String invoices;

    @Column(name = "DELETED")
    private Integer deleted;

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
     * @return COMBIN_ACCOUNT_ITEM
     */
    public String getCombinAccountItem() {
        return combinAccountItem;
    }

    /**
     * @param combinAccountItem
     */
    public void setCombinAccountItem(String combinAccountItem) {
        this.combinAccountItem = combinAccountItem == null ? null : combinAccountItem.trim();
    }

    /**
     * @return COMBIN_ACCOUNT_ITEM_SUM
     */
    public BigDecimal getCombinAccountItemSum() {
        return combinAccountItemSum;
    }

    /**
     * @param combinAccountItemSum
     */
    public void setCombinAccountItemSum(BigDecimal combinAccountItemSum) {
        this.combinAccountItemSum = combinAccountItemSum;
    }

    /**
     * @return USE_PRICE
     */
    public Integer getUsePrice() {
        return usePrice;
    }

    /**
     * @param usePrice
     */
    public void setUsePrice(Integer usePrice) {
        this.usePrice = usePrice;
    }

    /**
     * @return INVOICE_AMOUNT
     */
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * @param invoiceAmount
     */
    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**
     * @return WATER_NOTIFY_IDS
     */
    public String getWaterNotifyIds() {
        return waterNotifyIds;
    }

    /**
     * @param waterNotifyIds
     */
    public void setWaterNotifyIds(String waterNotifyIds) {
        this.waterNotifyIds = waterNotifyIds == null ? null : waterNotifyIds.trim();
    }

    /**
     * @return INVOICES
     */
    public String getInvoices() {
        return invoices;
    }

    /**
     * @param invoices
     */
    public void setInvoices(String invoices) {
        this.invoices = invoices == null ? null : invoices.trim();
    }

    /**
     * @return DELETED
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * @param deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", combinAccountItem=").append(combinAccountItem);
        sb.append(", combinAccountItemSum=").append(combinAccountItemSum);
        sb.append(", usePrice=").append(usePrice);
        sb.append(", invoiceAmount=").append(invoiceAmount);
        sb.append(", waterNotifyIds=").append(waterNotifyIds);
        sb.append(", invoices=").append(invoices);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}