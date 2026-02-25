package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.UserDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.MessageDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.UserDTOResponse;
import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.domain.entitites.Message;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.UserException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Component
public class UserUtil {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final AttachmentUtil attachmentUtil;

    public UserUtil(UserRepository userRepository, PasswordEncoder passwordEncoder, SecurityUtil securityUtil, AttachmentUtil attachmentUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
        this.attachmentUtil = attachmentUtil;
    }

    public User convertToEntity(UserDTORequest userDTORequest) {
        return new User(
                userDTORequest.getUserName(),
                passwordEncoder.encode(userDTORequest.getPassword())
        );
    }

    public boolean isUserNameTaken(String userName) {
        Optional<User> oUser = userRepository.findByUserName(userName);
        return oUser.isPresent();
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
    }

    public boolean isAdmin(Long id) {
        return findByIdOrThrow(id).getRoles().stream().anyMatch(role -> role.getRole() == RoleEnum.ADMIN);
    }

    public void validateRegister(UserDTORequest userDTORequest) {
        if (!isValidUserName(userDTORequest.getUserName())) throw new UserException("Invalid username", HttpStatus.BAD_REQUEST);
        if (!isValidPassword(userDTORequest.getPassword())) throw new UserException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public void validateUpdate(UserDTORequest userDTORequest) {
        if (!isValidUserName(userDTORequest.getUserName())) throw new UserException("Invalid username", HttpStatus.BAD_REQUEST);
        if (userDTORequest.getPassword() != null && !isValidPassword(userDTORequest.getPassword())) throw new UserException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    private boolean isValidUserName(String userName) {
        return !userName.trim().isEmpty();
    }

    private boolean isValidPassword(String password) {
        return !password.trim().isEmpty();
    }

    public boolean authenticatedUserHasId(Long id) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return authenticatedUser.getId().equals(id);
    }

    private boolean authenticatedUserHasUsername(String userName) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return authenticatedUser.getUserName().equals(userName);
    }

    public void validateAndSet(User user, UserDTORequest userDTORequest, MultipartFile file) {
        String userName = userDTORequest.getUserName();
        String password = userDTORequest.getPassword();
        if (!isValidUserName(userName)) throw new UserException("Invalid username", HttpStatus.BAD_REQUEST);
        if (password != null && !isValidPassword(password)) throw new UserException("Invalid password", HttpStatus.BAD_REQUEST);
        if (isUserNameTaken(userName) && !authenticatedUserHasUsername(userName)) throw new UserException("Username is taken", HttpStatus.CONFLICT);
        Attachment attachment = attachmentUtil.createAttachment(file);
        user.setUserName(userName);
        if (password != null) user.setPassword(passwordEncoder.encode(password));
        user.setAttachment(attachment);
    }

    public List<UserDTOResponse> pageToDTOList(Page<User> users) {
        return users.stream()
                .map(UserDTOResponse::new)
                .toList();
    }
}
