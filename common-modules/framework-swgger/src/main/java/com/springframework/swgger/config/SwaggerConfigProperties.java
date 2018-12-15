package com.springframework.swgger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author summer
 */
@ConfigurationProperties(prefix = "spring.boot.swgger.config")
public class SwaggerConfigProperties {


    /**
     * 版本
     */
    private String version = "1.0.0";
    /**
     * 标题
     */
    private String title = "Swagger API";
    /**
     * 描述
     */
    private String description = "This is to show api description";
    /**
     * url
     */
    private String termsOfServiceUrl = "";
    /**
     * 联系人
     */
    private Contact contact = new Contact("", "", "xieshengrong@live.com");
    /**
     * 扩展
     */
    private List<VendorExtension> vendorExtensions = newArrayList();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<VendorExtension> getVendorExtensions() {
        return vendorExtensions;
    }

    public void setVendorExtensions(List<VendorExtension> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }
}
