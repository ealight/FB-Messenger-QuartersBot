package com.bailbots.fb.quartersbot.handler;

import com.bailbots.fb.quartersbot.bpp.BotSelectHandle;
import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.dto.RegistrationDto;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageValueService;
import com.bailbots.fb.quartersbot.service.UserService;
import com.bailbots.fb.quartersbot.service.UserSessionService;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.userprofile.UserProfile;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@RestController
@RequestMapping("/callback")
public class Handler {
    private static final Logger LOGGER = LogManager.getLogger(Handler.class);

    private final Messenger messenger;
    private final UserService userService;
    private final UserSessionService userSessionService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final MessageValueService messageValueService;

    public Handler(Messenger messenger, @Lazy UserService userService, UserSessionService userSessionService, KeyboardLoaderService keyboardLoaderService, MessageValueService messageValueService) {
        this.messenger = messenger;
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.messageValueService = messageValueService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken, @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {
        LOGGER.info("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
        try {

            messenger.verifyWebhook(mode, verifyToken);
            botSettings();
            return ResponseEntity.ok(challenge);

        } catch (MessengerVerificationException e) {

            LOGGER.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        }

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(@RequestBody String payload, @RequestHeader(SIGNATURE_HEADER_NAME) String signature) {
        LOGGER.info("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
        try {
            messenger.onReceiveEvents(payload, of(signature), event -> {

                String facebookId = event.senderId();

                if (messageValueService.isGetValueTurn(facebookId)) {
                    messageValueService.getValueFromMessage(event, facebookId);
                    return;
                }

                boolean userExistInSession = userSessionService.getUserFromSession(facebookId) != null;

                if (!userExistInSession) {
                    if (!userService.isUserExist(facebookId)) {
                        UserProfile userProfile = null;

                        try {
                            userProfile = messenger.queryUserProfile(facebookId);
                        } catch (MessengerApiException | MessengerIOException ex) {
                            ex.printStackTrace();
                        }

                        String firstName = userProfile.firstName();
                        String lastName = userProfile.lastName();

                        RegistrationDto registrationDto = createRegistrationDto(facebookId, firstName, lastName);

                        User user = userService.registerUser(registrationDto);

                        userSessionService.addUserToSession(facebookId, user);
                    } else {
                        User user = userService.getByFacebookId(facebookId);

                        LOGGER.info("Facebook ID #{} successfully logged!", facebookId);
                        userSessionService.addUserToSession(facebookId, user);
                    }

                }

                BotSelectHandle.processByUpdate(event);

            });
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            LOGGER.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @SneakyThrows
    private void botSettings() {
        StartButton startButton = StartButton.create("StartButtonController&PressedStart");
        PersistentMenu persistentMenu = keyboardLoaderService.getPersistentMenuFromXML("persistentMenu");

        MessengerSettings messengerSettings =
                MessengerSettings.create(of(startButton), empty(), of(persistentMenu), empty(), empty(), empty(), empty());

        messenger.updateSettings(messengerSettings);

        LOGGER.info("Bot settings successfully installed");
    }

    private RegistrationDto createRegistrationDto(String facebookId, String firstName, String lastName) {
        return RegistrationDto.builder()
                .facebookId(facebookId)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
