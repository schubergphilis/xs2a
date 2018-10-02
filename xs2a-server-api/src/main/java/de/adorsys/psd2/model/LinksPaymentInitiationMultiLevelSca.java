package de.adorsys.psd2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Objects;

/**
 * Type of links admitted in this response, (further links might be added for ASPSP defined extensions):    * &#x27;startAuthorization&#x27;:      In case, where an explicit start of the transaction authorization is needed, but no more data needs to be updated (no authentication method to be selected, no PSU identification nor PSU authentication data to be uploaded).   * &#x27;startAuthorizationWithPsuIdentification&#x27;:      The link to the authorization end-point, where the authorization sub-resource has to be generated while uploading the PSU identification data.   * &#x27;startAuthorizationWithPsuAuthentication&#x27;:     The link to the authorization end-point, where an authorization sub-resource has to be generated while uploading the PSU authentication data.   * &#x27;self&#x27;:      The link to the payment initiation resource created by this request. This link can be used to retrieve the resource data.    * &#x27;status&#x27;:      The link to retrieve the transaction status of the payment initiation.
 */
@ApiModel(description = "Type of links admitted in this response, (further links might be added for ASPSP defined extensions):    * 'startAuthorization':      In case, where an explicit start of the transaction authorization is needed, but no more data needs to be updated (no authentication method to be selected, no PSU identification nor PSU authentication data to be uploaded).   * 'startAuthorizationWithPsuIdentification':      The link to the authorization end-point, where the authorization sub-resource has to be generated while uploading the PSU identification data.   * 'startAuthorizationWithPsuAuthentication':     The link to the authorization end-point, where an authorization sub-resource has to be generated while uploading the PSU authentication data.   * 'self':      The link to the payment initiation resource created by this request. This link can be used to retrieve the resource data.    * 'status':      The link to retrieve the transaction status of the payment initiation. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-08-09T18:41:17.591+02:00[Europe/Berlin]")
public class LinksPaymentInitiationMultiLevelSca extends HashMap<String, String> {

    @JsonProperty("startAuthorization")
    private String startAuthorisation = null;

    @JsonProperty("startAuthorizationWithPsuIdentification")
    private String startAuthorisationWithPsuIdentification = null;

    @JsonProperty("startAuthorizationWithPsuAuthentication")
    private String startAuthorisationWithPsuAuthentication = null;

    @JsonProperty("self")
    private String self = null;

    @JsonProperty("status")
    private String status = null;

    public LinksPaymentInitiationMultiLevelSca startAuthorisation(String startAuthorisation) {
        this.startAuthorisation = startAuthorisation;
        return this;
    }

    /**
     * Get startAuthorization
     *
     * @return startAuthorization
     **/
    @ApiModelProperty
    public String getStartAuthorisation() {
        return startAuthorisation;
    }

    public void setStartAuthorisation(String startAuthorisation) {
        this.startAuthorisation = startAuthorisation;
    }

    public LinksPaymentInitiationMultiLevelSca startAuthorisationWithPsuIdentification(String startAuthorisationWithPsuIdentification) {
        this.startAuthorisationWithPsuIdentification = startAuthorisationWithPsuIdentification;
        return this;
    }

    /**
     * Get startAuthorizationWithPsuIdentification
     *
     * @return startAuthorizationWithPsuIdentification
     **/
    @ApiModelProperty
    public String getStartAuthorisationWithPsuIdentification() {
        return startAuthorisationWithPsuIdentification;
    }

    public void setStartAuthorisationWithPsuIdentification(String startAuthorisationWithPsuIdentification) {
        this.startAuthorisationWithPsuIdentification = startAuthorisationWithPsuIdentification;
    }

    public LinksPaymentInitiationMultiLevelSca startAuthorisationWithPsuAuthentication(String startAuthorisationWithPsuAuthentication) {
        this.startAuthorisationWithPsuAuthentication = startAuthorisationWithPsuAuthentication;
        return this;
    }

    /**
     * Get startAuthorizationWithPsuAuthentication
     *
     * @return startAuthorizationWithPsuAuthentication
     **/
    @ApiModelProperty
    public String getStartAuthorisationWithPsuAuthentication() {
        return startAuthorisationWithPsuAuthentication;
    }

    public void setStartAuthorisationWithPsuAuthentication(String startAuthorisationWithPsuAuthentication) {
        this.startAuthorisationWithPsuAuthentication = startAuthorisationWithPsuAuthentication;
    }

    public LinksPaymentInitiationMultiLevelSca self(String self) {
        this.self = self;
        return this;
    }

    /**
     * Get self
     *
     * @return self
     **/
    @ApiModelProperty
    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public LinksPaymentInitiationMultiLevelSca status(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     *
     * @return status
     **/
    @ApiModelProperty
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinksPaymentInitiationMultiLevelSca _linksPaymentInitiationMultiLevelSca = (LinksPaymentInitiationMultiLevelSca) o;
        return Objects.equals(this.startAuthorisation, _linksPaymentInitiationMultiLevelSca.startAuthorisation) &&
            Objects.equals(this.startAuthorisationWithPsuIdentification, _linksPaymentInitiationMultiLevelSca.startAuthorisationWithPsuIdentification) &&
            Objects.equals(this.startAuthorisationWithPsuAuthentication, _linksPaymentInitiationMultiLevelSca.startAuthorisationWithPsuAuthentication) &&
            Objects.equals(this.self, _linksPaymentInitiationMultiLevelSca.self) &&
            Objects.equals(this.status, _linksPaymentInitiationMultiLevelSca.status) &&
            super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAuthorisation, startAuthorisationWithPsuIdentification, startAuthorisationWithPsuAuthentication, self, status, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LinksPaymentInitiationMultiLevelSca {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    startAuthorization: ").append(toIndentedString(startAuthorisation)).append("\n");
        sb.append("    startAuthorizationWithPsuIdentification: ").append(toIndentedString(startAuthorisationWithPsuIdentification)).append("\n");
        sb.append("    startAuthorizationWithPsuAuthentication: ").append(toIndentedString(startAuthorisationWithPsuAuthentication)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
