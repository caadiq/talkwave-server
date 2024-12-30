package mcnc.talkwave.controller;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.UserDeptDTO;
import mcnc.talkwave.entity.Departure;
import mcnc.talkwave.service.DepartureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dept")
public class DepartureController {

    private final DepartureService departureService;

    @GetMapping
    public ResponseEntity<List<UserDeptDTO>> findAllDepartures() {
        List<UserDeptDTO> userDeptDTOList = departureService.findAllWithUserName();
        return ResponseEntity.ok(userDeptDTOList);
    }
}
