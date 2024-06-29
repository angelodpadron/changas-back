package com.changas.config.utils;

import com.changas.dto.area.Geometry;
import com.changas.dto.area.ServiceAreaRequest;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.WorkAreaDetailsDTO;
import com.changas.service.AuthService;
import com.changas.service.ChangaService;
import com.changas.service.HiringTransactionService;
import com.changas.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final AuthService authService;
    private final ChangaService changaService;
    private final HiringTransactionService transactionService;
    private final InquiryService inquiryService;

    @Override
    public void run(String... args) throws Exception {

        log.info("Adding filler data into DB");

        String randomPhotoUrl = "https://i.pravatar.cc/300";

        String plomeroUrl = "https://images.pexels.com/photos/9389356/pexels-photo-9389356.jpeg";
        String herreriaUrl = "https://images.pexels.com/photos/3158651/pexels-photo-3158651.jpeg";
        String electricistaUrl = "https://images.pexels.com/photos/7859953/pexels-photo-7859953.jpeg";

        String electricistaDescripcion = "Ofrecemos servicios profesionales, seguros y garantizados para instalaciones, reparaciones y mantenimiento. Atención personalizada y soluciones eficientes. Contáctanos hoy mismo para una cotización gratuita.";
        String plomeroDescripcion = "Desde reparaciones simples hasta instalaciones complejas. Contáctanos ahora y deja que nuestros expertos cuiden de tu hogar o negocio con profesionalismo y calidad garantizada.";
        String herreroDescripcion = "Somos expertos herreros matriculados. Desde puertas y ventanas hasta estructuras metálicas complejas, nuestro compromiso es la calidad y la precisión en cada detalle.";


        authService.signup(new SignupRequest("Pepe", randomPhotoUrl, "pepe@email.com", "password"));
        authService.signup(new SignupRequest("Sofia", randomPhotoUrl, "sofia@email.com", "password"));
        authService.signup(new SignupRequest("Matias", randomPhotoUrl, "matias@email.com", "password"));

        authService.login(new LoginRequest("pepe@email.com", "password"));
        ServiceAreaRequest serviceAreaRequest = new ServiceAreaRequest("Florencio Varela, Buenos Aires", new Geometry("Point", new Double[]{-58.2912458, -34.7955703}));
        ChangaOverviewDTO changa1 = changaService.createChanga(new CreateChangaRequest("Servicio de Plomeria", plomeroDescripcion, plomeroUrl, Set.of("Plomeria", "Hogar", "Mantenimiento"), serviceAreaRequest));
        ChangaOverviewDTO changa2 = changaService.createChanga(new CreateChangaRequest("Electricista Matriculado", electricistaDescripcion, electricistaUrl, Set.of("Electricista", "Hogar", "Luz"), serviceAreaRequest));

        authService.login(new LoginRequest("matias@email.com", "password"));
        changaService.createChanga(new CreateChangaRequest("Servicio de Herreria", herreroDescripcion, herreriaUrl, Set.of("Soladura", "Hogar", "Exterior"), serviceAreaRequest));

        authService.login(new LoginRequest("sofia@email.com", "password"));
        transactionService.requestChanga(new HireChangaRequest(changa1.getId(), new WorkAreaDetailsDTO(null, plomeroUrl, "Necesito tal y tal cosa. Soy de Florencio Varela.")));

        authService.login(new LoginRequest("matias@email.com", "password"));
        inquiryService.createQuestion(changa2.getId(), "Buenas, hacen instalacion de trifasica?");

    }
}
