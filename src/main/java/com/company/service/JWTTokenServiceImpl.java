package com.company.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.model.dto.auth.TokenDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Token;
import com.company.model.entity.Token.Type;
import com.company.repository.ITokenRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTTokenServiceImpl extends BaseService implements JWTTokenService {

	@Value("${jwt.token.header.authorization}")
	private String JWT_TOKEN_HEADER_AUTHORIZATION;

	@Value("${jwt.token.prefix}")
	private String JWT_TOKEN_PREFIX;

	@Value("${jwt.token.secret}")
	private String JWT_TOKEN_SECRET;

	@Value("${jwt.token.time.expiration}")
	private long JWT_TOKEN_TIME_EXPIRATION;

	@Value("${jwt.refreshtoken.time.expiration}")
	private long JWT_REFRESH_TOKEN_TIME_EXPIRATION;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ITokenRepository tokenRepository;

	@Override
	public String generateJWTToken(String username) {
		return Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_TIME_EXPIRATION))
				.signWith(SignatureAlgorithm.HS512, JWT_TOKEN_SECRET).compact();
	}

	public Authentication parseTokenToUserInformation(HttpServletRequest request) {
		String token = request.getHeader(JWT_TOKEN_HEADER_AUTHORIZATION);

		if (token == null) {
			return null;
		}

		// parse the token
		try {
			String username = Jwts.parser().setSigningKey(JWT_TOKEN_SECRET)
					.parseClaimsJws(token.replace(JWT_TOKEN_PREFIX, "")).getBody().getSubject();

			Account account = accountService.getAccountByUsername(username);

			return username != null
					? new UsernamePasswordAuthenticationToken(account.getUsername(), null,
							AuthorityUtils.createAuthorityList(account.getRole().toString()))
					: null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional
	public Token generateRefreshToken(Account account) {
		Token refreshToken = new Token(account, UUID.randomUUID().toString(), Token.Type.REFRESH_TOKEN,
				new Date(new Date().getTime() + JWT_REFRESH_TOKEN_TIME_EXPIRATION));

		// delete all old refresh token of this account
		tokenRepository.deleteByTypeAndAccount(Type.REFRESH_TOKEN, account);

		// create new token
		return tokenRepository.save(refreshToken);
	}

	@Override
	public boolean isRefreshTokenValid(String refreshToken) {
		Token entity = tokenRepository.findBykeyAndType(refreshToken, Type.REFRESH_TOKEN);
		if (entity == null || entity.getExpiredDateTime().before(new Date())) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public TokenDTO getNewToken(String refreshToken) {
		// find old refresh token
		Token oldRefreshToken = tokenRepository.findBykeyAndType(refreshToken, Type.REFRESH_TOKEN);

		// delete old refresh token
		tokenRepository.deleteByTypeAndAccount(Type.REFRESH_TOKEN, oldRefreshToken.getAccount());

		// create new refresh token
		Token newRefreshToken = generateRefreshToken(oldRefreshToken.getAccount());

		// create new jwt token
		String newToken = generateJWTToken(oldRefreshToken.getAccount().getUsername());

		return new TokenDTO(newToken, newRefreshToken.getKey());
	}
}