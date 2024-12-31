package mcnc.talkwave.controller;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.RegisterDTO;
import mcnc.talkwave.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterDTO registerDTO) {
        userService.registerUser(registerDTO);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestParam("userId") String userId,
                                            @RequestParam("password") String password) {
        if (userService.loginUser(userId, password)) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.badRequest().body("아이디/비밀번호가 일치하지 않습니다.");
        }
    }
}
