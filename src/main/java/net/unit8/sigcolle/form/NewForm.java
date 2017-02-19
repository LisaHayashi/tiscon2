package net.unit8.sigcolle.form;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author kawasima
 */
@Data
public class NewForm extends FormBase {

    //**************************************↓


    @Length(min = 1, max = 50)
    private String title;


    @Length(max = 5000)
    private String statement;

    @DecimalMin("1")
    @DecimalMax("9999")
    private String goal;


    //**************************************↑
/*
    public Long getCampaignIdLong() {
        return Long.parseLong(campaignId);
    }
    public Long getCreateUserIdLong() {
        return Long.parseLong(userId);
    }
*/
}