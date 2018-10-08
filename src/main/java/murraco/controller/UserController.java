package murraco.controller;

import javax.servlet.http.HttpServletRequest;

import murraco.dto.AuthenticationResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiParam;
import murraco.dto.UserResponseDTO;
import murraco.model.User;
import murraco.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signin")
    public AuthenticationResponseDTO login(@RequestBody User user) {
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setToken(userService.signin(user.getUsername(), user.getPassword()));
        authenticationResponseDTO.setUsername(user.getUsername());
        return authenticationResponseDTO;
    }

    @PostMapping("/signup")
    public UserResponseDTO signup(@ApiParam("Signup User") @RequestBody User user) {
        return modelMapper.map(userService.signup(user), UserResponseDTO.class);
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@ApiParam("Username") @PathVariable String username) {
        userService.delete(username);
        return username;
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
        return modelMapper.map(userService.search(username), UserResponseDTO.class);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public UserResponseDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

}
