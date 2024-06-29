package com.changas.controller.utils;

import com.changas.dto.area.Geometry;
import com.changas.dto.area.ServiceAreaRequest;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.dto.changa.CreateChangaRequest;

import java.util.Set;

public class ModelTestResource {

    public static SignupRequest getSignupRequest() {
        return new SignupRequest("Pepe", "PhotoUrl", "pepe@email.com", "password");
    }

    public static LoginRequest getLoginRequest() {
        return new LoginRequest("pepe@email.com", "password");
    }

    public static CreateChangaRequest getCreateChangaRequest() {
        ServiceAreaRequest serviceAreaRequest = new ServiceAreaRequest("Direccion", new Geometry("Point", new Double[]{-58.2912458, -34.7955703}));
        return new CreateChangaRequest("Title", "Description", "PhotoUrl", Set.of("Topic"), serviceAreaRequest);
    }

}
