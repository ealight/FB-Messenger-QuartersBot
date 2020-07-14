package com.bailbots.fb.quartersbot.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationDto {
    private String facebookId;
    private String firstName;
    private String lastName;
}
