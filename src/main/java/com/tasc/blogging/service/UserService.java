package com.tasc.blogging.service;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.entity.enums.ERROR;
import com.tasc.blogging.entity.user.Role;
import com.tasc.blogging.entity.user.User;
import com.tasc.blogging.entity.enums.BaseStatus;
import com.tasc.blogging.model.requset.user.LoginRequest;
import com.tasc.blogging.model.requset.user.RegisterRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.model.response.user.LoginResponse;
import com.tasc.blogging.model.response.user.UserDTO;
import com.tasc.blogging.repository.RoleRepository;
import com.tasc.blogging.repository.UserRepository;
import com.tasc.blogging.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserService implements UserDetailsService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenBlacklistService jwtTokenBlacklistService;

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public BaseResponse<UserDTO> register(RegisterRequest request) throws ApplicationException {
        log.info("1 - Register request: {}", request);
        validateRegisterRequest(request);

        log.info("2 - Find user by email: {}", request.getEmail());
        Boolean isUserExist = userRepository.existsByEmail(request.getEmail());

        if (isUserExist) {
            log.error("User already exists");
            throw new ApplicationException(ERROR.EMAIL_EXISTED);
        }

        Role defaultRole = roleRepository.findByName("DEFAULT");

        if (defaultRole == null) {
            log.error("Default role not found");
            defaultRole = Role.builder()
                    .name("DEFAULT")
                    .status(BaseStatus.ACTIVE)
                    .build();
            roleRepository.save(defaultRole);
        }

        log.info("3 - Create new user");

        Random r = new Random();
        String randomNumber = String.format("%04d", Integer.valueOf(r.nextInt(1001)));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(defaultRole)
                .verificationCode(randomNumber)
                .status(BaseStatus.PENDING)
                .build();

        log.info("4 - Save user");

        userRepository.save(user);

        sendVerificationEmail(user.getEmail(), user.getVerificationCode());

        return new BaseResponse<>("User registered successfully", convertToDTO(user));
    }

    public BaseResponse<UserDTO> verifyEmail(String code) throws ApplicationException {
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);

        if (optionalUser.isEmpty()) {
            throw new ApplicationException(ERROR.USER_NOT_FOUND);
        }

        User user = optionalUser.get();

        if (!user.getVerificationCode().equals(code)) {
            throw new ApplicationException(ERROR.INVALID_VERIFICATION_CODE);
        }

        user.setStatus(BaseStatus.ACTIVE);

        user.setVerificationCode(null);
        userRepository.save(user);

        return new BaseResponse<>("Email verified successfully", convertToDTO(user));
    }

    private void sendVerificationEmail(String recipientEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Verify your email address");
        message.setText("Verification Code " + verificationCode);
        mailSender.send(message);
    }

    public BaseResponse login(LoginRequest request, HttpServletRequest httpServletRequest) throws ApplicationException {
        log.info("1 - Login request: {}", request);
        validateLoginRequest(request);

        log.info("2 - Find user by email: {}", request.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        log.info("2.1 - Check user is empty or not");
        if (optionalUser.isEmpty()) {
            log.error("User not found");
            throw new ApplicationException(ERROR.USER_NOT_FOUND);
        }

        User user = optionalUser.get();

        log.info("3 - Check password");
        if (!passwordEncoder.matches(request.getPassword(), optionalUser.get().getPassword())) {
            log.error("Password not match");
            throw new ApplicationException(ERROR.PASSWORD_NOT_MATCH);
        }

        log.info("4 - Generate credential");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        authorities.add(authority);

        LoginResponse loginResponse =
                generateCredential(new org.springframework.security.core.userdetails
                        .User(user.getEmail(), user.getPassword(), authorities), httpServletRequest);

        return new BaseResponse("Login success", loginResponse);
    }

    public LoginResponse generateCredential(UserDetails userDetail, HttpServletRequest request) {
        String accessToken = JWTUtil.generateToken(userDetail.getUsername(),
                userDetail.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURI(),
                JWTUtil.ONE_DAY * 7);

        String refreshToken = JWTUtil.generateToken(userDetail.getUsername(),
                userDetail.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURI(),
                JWTUtil.ONE_DAY * 14);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmailAndStatus(email, BaseStatus.ACTIVE);
        User user = userOptional.orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        authorities.add(authority);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    private void validateRegisterRequest(RegisterRequest request) throws ApplicationException {
        log.info("1.1 - Validate register request username: {}", request.getEmail());
        if (StringUtils.isEmpty(request.getEmail())) {
            log.error("Invalid username request");
            throw new ApplicationException(ERROR.INVALID_EMAIL_REQUEST);
        }

        log.info("1.2 - Validate register request password: {}", request.getPassword());
        if (StringUtils.isEmpty(request.getPassword())) {
            log.error("Invalid password request");
            throw new ApplicationException(ERROR.INVALID_PASSWORD_REQUEST);
        }

        log.info("1.3 - Validate register request confirm password: {}", request.getConfirmPassword());
        if (StringUtils.isEmpty(request.getConfirmPassword())) {
            log.error("Invalid confirm password request");
            throw new ApplicationException(ERROR.INVALID_CONFIRM_PASSWORD_REQUEST);
        }

        log.info("1.4 - Validate register request check password match: {}", request.getPassword().equals(request.getConfirmPassword()));
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.error("Password not match");
            throw new ApplicationException(ERROR.PASSWORD_NOT_MATCH);
        }
    }

    private void validateLoginRequest(LoginRequest request) throws ApplicationException {
        log.info("1.1 - Validate login request username: {}", request.getEmail());
        if (StringUtils.isEmpty(request.getEmail())) {
            log.error("Invalid username request");
            throw new ApplicationException(ERROR.INVALID_EMAIL_REQUEST);
        }

        log.info("1.2 - Validate login request password: {}", request.getPassword());
        if (StringUtils.isEmpty(request.getPassword())) {
            log.error("Invalid password request");
            throw new ApplicationException(ERROR.INVALID_PASSWORD_REQUEST);
        }
    }
}
