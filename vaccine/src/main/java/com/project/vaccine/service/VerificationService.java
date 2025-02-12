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
import java.util.List;
import java.util.UUID;

@Service
public class VerificationService {

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private EmailService emailService;

/*
*  Method getAllTokens is used to verify the token.
*  If the token is valid, the user status = 1.
*  Need to delete this function in production.
* */
    public List<Verification> getAllTokens(){
        return verificationRepository.findAll();
    }

    public void createToken(User user, VerificationEnum verificationMethod){
        Verification verification = new Verification();
        verification.setUser(user);
        verification.setTokenValue(UUID.randomUUID().toString()); // random token
        verification.setCreatedAt(LocalDateTime.now());
        verification.setExpiredAt(LocalDateTime.now().plusMinutes(15)); // 15 minutes
        verification.setVerificationMethod(verificationMethod);
        verification.setAttemptCount(0); // default 0, max 5
        verificationRepository.save(verification);
    }


    public String verifyToken(String token) {
        Verification verification = verificationRepository.findByTokenValue(token)
                .orElseThrow(() -> new NotFoundException("Link xác minh không tồn tại!"));

        if (verification.getExpiredAt().isBefore(LocalDateTime.now())) {
            return "Link xác minh đã hết hạn!";
        }

        User user = verification.getUser();

        // Nếu tài khoản đã kích hoạt và không có email chờ xác nhận, không cần xác minh lại
        if (user.getStatus().equals(UserStatusEnum.ACTIVE) && user.getPendingEmail() == null) {
            return "Tài khoản đã được kích hoạt!";
        }

        // Nếu user chưa kích hoạt -> kích hoạt tài khoản
        if (user.getStatus().equals(UserStatusEnum.INACTIVE)){
            user.setStatus(UserStatusEnum.ACTIVE);
        }

        // Nếu user có pendingEmail -> đây là xác minh đổi email, cập nhật email mới
        if (user.getPendingEmail() != null) {
            user.setEmail(user.getPendingEmail());
            user.setPendingEmail(null);
        }

        authenticationRepository.save(user);
        verificationRepository.delete(verification);

        return "Xác minh thành công!";
    }



    /*
      IMPORTANT NOTE: giới hạn là 5 lần gửi trong 30 phút
     -Lần gửi email khi đăng ký. (lần đầu tiên sau khi nhấn verify)
     -Lần gửi khi nhấn lại nút Verify.
     -Lần gửi khi thay đổi email sau khi xác minh
    */

    public String registerVerification(String email, String verificationMethod) {
        User user = authenticationRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getStatus().equals(UserStatusEnum.ACTIVE)) {
            return "Tài khoản đã được kích hoạt!";
        }

        Verification verification = verificationRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Bạn chưa yêu cầu xác minh!"));

        if(verification.getAttemptCount() > 0){
            return "Bạn đã gửi yêu cầu xác minh!";
        }

        if (verification.getExpiredAt().isBefore(LocalDateTime.now())) {
            return "Link xác minh đã hết hạn!";
        } // Nếu hết hạn, thì phải gọi api resend verify

        // Create link
        String link = "http://localhost:8080/api/verification/register/confirm?token="
                + verification.getTokenValue();

        // Send email
        try {
            emailService.sendVerificationEmail(email, "Verify email", link, verificationMethod);
        } catch (Exception e) {
            return "Gửi email thất bại!";
        }

        verificationRepository.save(verification);
        return "Email verification sent";
    }


    public String resetVerification(String email) {
        User user = authenticationRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Verification verification = verificationRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Bạn chưa yêu cầu xác minh!"));


        if (verification.getExpiredAt().isBefore(LocalDateTime.now())) {
            verification.setExpiredAt(LocalDateTime.now().plusMinutes(15));
            verification.setAttemptCount(0);
        } // Nếu hết hạn, cập nhật lại thời gian hết hạn và reset số lần gửi

        if (verification.getLockTime() != null && verification.getLockTime().isAfter(LocalDateTime.now())) {
            return "Tài khoản đã bị khóa trong 30 phút!";
        } // Nếu tài khoản bị khóa

        if (verification.getAttemptCount() >= 5) {
            // Lock user for 30 minutes
            verification.setLockTime(LocalDateTime.now().plusMinutes(30));
            verificationRepository.save(verification);
            return "Bạn đã vượt quá số lần gửi trong 30 phút!";
        } else {
            verification.setAttemptCount(verification.getAttemptCount() + 1);
        }


        // Create link
        String link = "http://localhost:8080/api/verification/register/confirm?token="
                + verification.getTokenValue();

        // Send email
        try {
            emailService.sendVerificationEmail(email, "Verify email", link, "REGISTER");
        } catch (Exception e) {
            return "Gửi email thất bại!";
        }

        verificationRepository.save(verification);
        return "Xác minh đã được gửi lại!";
    }

    public void getAllToken(){
        verificationRepository.findAll();
    }
}
