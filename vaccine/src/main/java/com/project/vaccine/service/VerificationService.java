package com.project.vaccine.service;

import com.project.vaccine.entity.User;
import com.project.vaccine.entity.Verification;
import com.project.vaccine.enums.UserStatusEnum;
import com.project.vaccine.enums.VerificationEnum;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.AuthenticationRepository;
import com.project.vaccine.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
public class VerificationService {

    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private EmailService emailService;

    public Verification createToken(User user, VerificationEnum verificationMethod) {
        Verification verification = new Verification();
        verification.setUser(user);
        verification.setTokenValue(generateVerificationCode());
        verification.setCreatedAt(LocalDateTime.now());
        verification.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        verification.setVerificationMethod(verificationMethod);
        verification.setAttemptCount(0);
        return verificationRepository.save(verification);
    }

    public String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public String verifyToken(String email, String code) {
        User user = authenticationRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Verification verification = verificationRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("No verification code found for this user"));

        if (user.getStatus() == UserStatusEnum.ACTIVE) {
            return "Account is already verified!";
        }// Account is already verified

        if (verification.getExpiredAt().isBefore(LocalDateTime.now())) {
            return "Verification code has expired!";
        }

        if (!verification.getTokenValue().equals(code)) {
            return "Incorrect verification code!";
        }// Incorrect verification code

        user.setStatus(UserStatusEnum.ACTIVE);
        authenticationRepository.save(user);
        verificationRepository.delete(verification);

        return "Account verified successfully!";
    }


    public String registerVerification(String email, String verificationMethod) {
        Optional<User> userOptional = authenticationRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = userOptional.get();
        if (user.getStatus() == UserStatusEnum.ACTIVE) {
            return "User has been verified";
        }

        VerificationEnum verificationMethodEnum = VerificationEnum.valueOf(verificationMethod);
        Optional<Verification> verificationOptional = verificationRepository.findByUser(user);

        if (verificationOptional.isPresent()) {
            return "Verification code has been sent to your email";
        }

        Verification verification = createToken(user, verificationMethodEnum);
        try {
            emailService.sendVerificationEmail(email, "Verification Code",
                    verification.getTokenValue(), verificationMethod);
        } catch (Exception e) {
            return "Failed to send email";
        }

        return "Verification code has been sent to your email";
    }



    public String resetRegisterVerification(String email, String verificationMethod) {
        User user = authenticationRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Verification verification = verificationRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("You have not requested verification!"));

        if (verification.getLockTime() != null && verification.getLockTime().isAfter(LocalDateTime.now())) {
            return "You have exceeded the maximum number of attempts. Please try again after 30 minutes!";
        }

        // Check spam
        if (verification.getAttemptCount() >= MAX_ATTEMPTS) {
            verification.setLockTime(LocalDateTime.now().plusMinutes(30));
            verificationRepository.save(verification);
            return "You have reached the maximum number of resends. Please wait 30 minutes!";
        }

        // Create new token
        String newToken = generateVerificationCode();
        verification.setTokenValue(newToken);
        verification.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        verification.setAttemptCount(verification.getAttemptCount() + 1);
        verificationRepository.save(verification);

        try {
            emailService.sendVerificationEmail(email, "New Verification Code", newToken, verificationMethod);
        } catch (Exception e) {
            verification.setAttemptCount(verification.getAttemptCount() - 1);
            verificationRepository.save(verification);
            return "Failed to send email";
        }

        return "A new verification code has been sent to your email!";
    }

}
