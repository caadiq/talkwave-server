package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.UserDeptDTO;
import mcnc.talkwave.entity.Departure;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.DepartureRepository;
import mcnc.talkwave.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartureService {

    private final DepartureRepository departureRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDeptDTO> findAllWithUserName() {
        List<User> userList = userRepository.findAll();
        return departureRepository.findAll()
                .stream()
                .map(departure -> {
                    UserDeptDTO dto = new UserDeptDTO(departure);

                    List<UserDeptDTO.UserInfoDTO> userInfoList = userList.stream()
                            .filter(user -> user.getDeparture().getDeptId() == departure.getDeptId())
                            .map(user -> new UserDeptDTO.UserInfoDTO(user.getName(), user.getUserId()))
                            .collect(Collectors.toList());

                    dto.setUserInfoList(userInfoList);
                    return dto;
                })
                .toList();  // 최종 결과 리스트 반환
    }

    @Transactional
    public Long createDeparture(String name) {
        if (!departureRepository.existsByName(name)) {
            Departure departure = new Departure(name);
            departureRepository.save(departure);
            return departure.getDeptId();
        }
        return null;
    }
}
