package it.sander.aml.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.sander.aml.application.dtos.AuthenticationDto;
import it.sander.aml.service.AuthenticationService;

@RestController
@RequestMapping("aml")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path={"authentication"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody AuthenticationDto authDto) {
        return this.authenticationService.login(authDto.getEmail(), authDto.getPassword());
    }
}
