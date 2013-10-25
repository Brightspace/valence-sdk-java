package com.d2lvalence.apiobjects;
import com.google.gson.annotations.SerializedName;


public class ProductVersions {
    
    @SerializedName("LatestVersion") private String latestVersion;
    @SerializedName("ProductCode") private String productCode;
    @SerializedName("SupportedVersions") private String[] supportedVersions;

    public ProductVersions() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String[] getSupportedVersions() {
        return supportedVersions;
    }

    public void setSupportedVersions(String[] supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

}
