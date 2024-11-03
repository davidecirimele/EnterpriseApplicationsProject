package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.JwtService;
import com.enterpriseapplicationsproject.ecommerce.config.security.LoggedUserDetailsService;
import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimitingService;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.impl.WishlistsServiceImpl;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.Converters;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishlistController.class)
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistsService wishlistService;

    @MockBean
    private RateLimitingService rateLimitingService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private LoggedUserDetailsService loggedUserDetailsService;

    private List<WishlistDto> wishlists;

    private String URL = "https://localhost:8081/api/v1/wishlists";

    @BeforeEach
    public void setUp() {
        WishlistDto wishlist1 = new WishlistDto();
        wishlist1.setId(1L);
        wishlist1.setName("Wishlist 1");
        wishlist1.setPrivacySetting(WishlistPrivacy.PUBLIC);

        WishlistDto wishlist2 = new WishlistDto();
        wishlist2.setId(2L);
        wishlist2.setName("Wishlist 2");
        wishlist2.setPrivacySetting(WishlistPrivacy.PRIVATE);

        wishlists = Arrays.asList(wishlist1, wishlist2);

        // Mock del servizio per restituire la lista delle wishlists
        when(wishlistService.getAllSorted()).thenReturn(wishlists);
    }

    @Test
    public void testGetAllWishlists() throws Exception {
        mockMvc.perform(get(URL + "/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.size()").value(wishlists.size()))
                .andExpect(jsonPath("$[0].name").value("Wishlist 1"))
                .andExpect(jsonPath("$[1].name").value("Wishlist 2"))
                .andDo(print());

        // Verifica che il servizio sia stato chiamato
        verify(wishlistService, times(1)).getAllSorted();
    }
}

