package mcnc.talkwave.controller;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.UserDeptDTO;
import mcnc.talkwave.entity.Departure;
import mcnc.talkwave.service.DepartureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @PostMapping
    public ResponseEntity<Long> createDeparture(@RequestBody Map<String, String> request) {
        Long deptId = departureService.createDeparture(request.get("name"));
        if (deptId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(deptId);
    }
}
