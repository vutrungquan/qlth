package org.example.qlth1.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.constant.PredefinedRole;
import org.example.qlth1.dto.request.IntrospectRequest;
import org.example.qlth1.dto.request.LogoutRequest;
import org.example.qlth1.dto.request.RefreshRequest;
import org.example.qlth1.dto.response.IntrospectResponse;
import org.example.qlth1.entity.InvalidatedToken;
import org.example.qlth1.entity.Role;
import org.example.qlth1.entity.User;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.InvalidatedTokenRepository;
import org.example.qlth1.repository.RoleRepository;
import org.example.qlth1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.example.qlth1.dto.request.AuthenticationRequest;
import org.example.qlth1.dto.response.AuthenticationResponse;
import org.example.qlth1.dto.request.RegisterRequest;
import org.example.qlth1.dto.response.JwtResponse;
import org.example.qlth1.dto.request.LoginRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RoleRepository roleRepository;
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @Value("${jwt.valid-duration}")         
    protected long validDuration;

    @Value("${jwt.refreshable-duration}")   
    protected long refreshableDuration;

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

       
        Role studentRole = roleRepository.findByName(PredefinedRole.STUDENT_ROLE.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .username(request.getUsername())
                .password(new BCryptPasswordEncoder(10).encode(request.getPassword()))
                .roles(Set.of(studentRole))  
                .build();

        userRepository.save(user);
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                
        if (!new BCryptPasswordEncoder(10).matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        String token = generateToken(user);
        return JwtResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
    /**
     * Kiểm tra tính hợp lệ của token.
     */
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException {
        String token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }


    public void logout(LogoutRequest request) throws JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException |  java.text.ParseException exception) {
            log.info("Token already expired");
        }
    }


    public AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException{
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            String username = signedJWT.getJWTClaimsSet().getSubject();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
            String token = generateToken(user);
            return AuthenticationResponse.builder().token(token).authenticated(true).build();
        } catch (java.text.ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }


    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("school-management.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

 
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = (isRefresh)
                    ? Date.from(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshableDuration, ChronoUnit.SECONDS))
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean verified = signedJWT.verify(verifier);
            if (!(verified && expiryTime.after(new Date()))) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return signedJWT;
        } catch (java.text.ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }


    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
        log.info("User roles: {}", user.getRoles());
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }


}