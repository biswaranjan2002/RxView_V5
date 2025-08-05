package com.raymedis.rxviewui.service.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RaymedisConfig {
    List<ApplicationDetails> applications;


    public RaymedisConfig() {
    }

    public RaymedisConfig(List<ApplicationDetails> applications) {
        this.applications = applications;
    }

    public List<ApplicationDetails> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationDetails> applications) {
        this.applications = applications;
    }

    @Override
    public String toString() {
        return "RaymedisConfig{" +
                "applications=" + applications +
                '}';
    }
}
