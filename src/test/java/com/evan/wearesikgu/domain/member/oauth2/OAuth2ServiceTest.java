package com.evan.wearesikgu.domain.member.oauth2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OAuth2ServiceTest {
    @InjectMocks
    private OAuthService oAuth2Service;

    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName("인가 코드롤 Access Token을 받아온다.")
    public void getAccessToken() throws Exception {
        //given
        String code = "auth-code-123";
        KakaoTokenResponseDTO mockResponse = new KakaoTokenResponseDTO();

        ResponseEntity<KakaoTokenResponseDTO> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);

//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(KakaoTokenResponseDTO.class)))
//                .thenReturn(response);

        //when
        String token = oAuth2Service.getAccessToken(code);

        //then
        assertThat(token).isEqualTo("access-token-abc");
    }
}
