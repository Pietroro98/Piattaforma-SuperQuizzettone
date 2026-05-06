package com.superquizzettone.dto;

import com.superquizzettone.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryDTO {

    private Long id;
    private String username;


    public static UserSummaryDTO fromModel(User user) {
        if (user == null) {
            return null;
        }

        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
