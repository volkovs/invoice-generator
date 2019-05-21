package dev.volkovs.office.tool.invoice.model.flat;

import lombok.Data;

@Data
public class InvIt {

    private String name;
    private String unit;
    private String price;
    private String curr;
    private String count;
    private String discPerc;
    private String taxPerc;
    private String subTtl;

}
