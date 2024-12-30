package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcnc.talkwave.dto.RegisterDTO;
import mcnc.talkwave.entity.Departure;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.DepartureRepository;
import mcnc.talkwave.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final DepartureRepository departureRepository;

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        Departure departure = departureRepository.findById(registerDTO.getDeptId())
                .orElseThrow(() -> new NoSuchElementException("No Departure found By Id"));
        User user = User.createUser(registerDTO.getUserId(), registerDTO.getPassword(), registerDTO.getName(), departure);
        userRepository.save(user);
    }

    public boolean loginUser(String userId, String password) {
        return userRepository.findByUserId(userId)
                .map(user -> user.getPassword().equals(password)) // 비밀번호 비교
                .orElse(false); // 유저가 없으면 false
    }

}
