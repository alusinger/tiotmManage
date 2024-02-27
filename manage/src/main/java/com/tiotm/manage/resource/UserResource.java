package com.tiotm.manage.resource;

import static java.time.LocalDateTime.now;

import static java.util.Map.of;
import static  org.springframework.http.HttpStatus.CREATED;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tiotm.manage.domain.HttpResponse;
import com.tiotm.manage.domain.User;
import com.tiotm.manage.dto.UserDTO;
import com.tiotm.manage.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
// Could do the /users endpoint here
// However, by adding the @RequestMapping annotation, we can update existing API endpoints with minimal 
// changes to other layers of the code in the controller or the UI
// This is a good practice to  keep the code organized and easy to maintain. 
// Such as feature toggles or blue green testing. 
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDto = userService.createUser(user);
        String id = userDto.getUserId().toString();
        return ResponseEntity.created(getUri(id)).body(
            HttpResponse.builder()
            .timeStamp(now().toString())
            .data(of("user", userDto))
            .message("User Created")
            .httpStatus(CREATED)
            .statusCode(CREATED.value())
            .developerMessage("User created successfully")
            .build()
            );
    }

    private URI getUri(String id) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
    

}
