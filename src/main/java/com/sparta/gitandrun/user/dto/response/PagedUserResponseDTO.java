package com.sparta.gitandrun.user.dto.response;

import com.sparta.gitandrun.user.entity.Address;
import com.sparta.gitandrun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedUserResponseDTO {
    private UserPage userPage;

    public static PagedUserResponseDTO of(Page<User> userPage) {
        return PagedUserResponseDTO.builder()
                .userPage(new UserPage(userPage))
                .build();
    }

    @Getter
    public static class UserPage extends PagedModel<UserPage.UserDTO> {
        public UserPage(Page<User> userPage) {
            super(
                new PageImpl<>(
                    UserDTO.from(userPage.getContent()),
                    userPage.getPageable(),
                    userPage.getTotalElements()
                )
            );
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UserDTO {
            private Long userId;
            private String username;
            private String nickName;
            private String email;
            private String phone;
            private Address address;
            private String role;

            public static List<UserDTO> from(List<User> users) {
                return users.stream()
                        .map(UserDTO::from)
                        .toList();
            }

            public static UserDTO from(User user) {
                return UserDTO.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .nickName(user.getNickName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .role(user.getRole().getRole())
                        .build();
            }
        }
    }
}