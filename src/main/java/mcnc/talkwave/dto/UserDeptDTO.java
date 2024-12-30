package mcnc.talkwave.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mcnc.talkwave.entity.Departure;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDeptDTO {
    private Long deptId;
    private String name;
    private List<UserInfoDTO> userInfoList;

    public UserDeptDTO(Departure departure) {
        this.deptId = departure.getDeptId();
        this.name = departure.getName();
        this.userInfoList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserInfoDTO {
        String userName;
        String userId;
    }
}


