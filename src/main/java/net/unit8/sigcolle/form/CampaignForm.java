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
public class CampaignForm extends FormBase {
    @DecimalMin("1")
    @DecimalMax("9999")
    private String campaignId;

    //**************************************↓

    @DecimalMin("1")
    @DecimalMax("9999")
    private String userId;


    //**************************************↑

    public Long getCampaignIdLong() {
        return Long.parseLong(campaignId);
    }
    //public Long getCreateUserIdLong() {return Long.parseLong(userId);}

}
