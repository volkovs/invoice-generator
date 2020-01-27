package dev.volkovs.office.tool.invoice.model.flat;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Flat Invoice structure with short property names
 * to minimize affect of template layout.
 * <p>
 * String type for template simplicity purposes.
 */
@Data
@ToString
public class Inv {

    // Invoice
    private String date;
    private String due;
    private String number;

    // Service Provider
    private String fromName;
    private String fromRegNum;
    private String fromTaxNum;
    private String fromAddr;
    private String fromPhone;
    private String fromBnkName;
    private String fromBnkBic;
    private String fromBnkAccNum;

    // Service Consumer
    private String toName;
    private String toRegNum;
    private String toTaxNum;
    private String toAddr;
    private String toPhone;
    private String toBnkName;
    private String toBnkBic;
    private String toBnkAccNum;

    // agreement number
    private String agrNum;

    // total before discount and tax
    private String ttlBdat;
    // total discount amount
    private String ttlDam;
    // total amount before tax
    private String ttlAbt;
    // total tax amount
    private String ttlTa;
    // total to be paid
    private String ttlTbp;

    // this fields name affords to be long
    private String totalAmountWords;

    // invoice items
    private List<InvIt> items;

    // first invoice item
    private String name;
    private String unit;
    private String price;
    private String curr;
    private String count;
    private String dp;
    private String txp;
    private String subTtl;
}
