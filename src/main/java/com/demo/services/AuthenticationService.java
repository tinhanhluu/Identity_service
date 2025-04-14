package com.demo.services;

import com.demo.dto.requests.AuthenticationRequest;
import com.demo.dto.requests.IntrospectRequest;
import com.demo.dto.requests.LogoutRequest;
import com.demo.dto.response.AppException;
import com.demo.dto.response.AuthenticationResponse;
import com.demo.dto.response.ErrorCode;
import com.demo.dto.response.IntrospectResponse;
import com.demo.entity.InvalidatedToken;
import com.demo.entity.User;
import com.demo.repository.InvalidatedTokenRepository;
import com.demo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.impl.MACProvider;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String Sign_key;

    @Autowired
    PasswordEncoder passwordEncoder;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {

        var token = request.getToken(); // lấy token từ request
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch(AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.UNEXISTING_USER));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return AuthenticationResponse.builder()
                .loginSuccess(authenticated)
                .token(generateToken(user))
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());
        var jti = signToken.getJWTClaimsSet().getJWTID();
        var expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(Sign_key); // tạo bộ xác thực dùng HMAC + Signer Key để check chữ ký token

        SignedJWT signedJWT = SignedJWT.parse(token); // chuyển đổi string token => JWT Object

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime(); // lấy ngày hết hạn từ JWT Object vừa parse ra

        var verify = signedJWT.verify(verifier); // kiểm tra chữ ký

        if(!(verify && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("tinhanh.com")
                .issueTime(new Date())
                .subject(user.getName())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user)) // scope này là role
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(Sign_key.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot sign JWT", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" "); // Scope trong OAuth2 phân cách nhau bằng dấu cách
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }
}
