package br.com.chagas.pontointeligente.pontointeligente.api.controllers;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpresaControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmpresaService empresaService;

    private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
    private static final Long ID = Long.valueOf(1);
    private static final String CNPJ = "12520006000179";
    private static final String RAZAO_SOCIAL = "Empresa XYZ";

    @Test
    @WithMockUser
    public void testBuscarEmpresaCnpjInvalido() throws Exception{
        BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
                .get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest())
             .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Empresa não encontrada para o CNPJ: " + CNPJ));
    }

    
    @Test
    @WithMockUser
    public void testBuscarEmpresaCnpjValido() throws Exception {
        BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString()))
                .willReturn(Optional.of(this.obterDadosEmpresa()));

        mvc.perform(MockMvcRequestBuilders
                .get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
                .accept(MediaType.APPLICATION_JSON  
            )).andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(ID))
             .andExpect(MockMvcResultMatchers.jsonPath("$.data.razaoSocial").value(RAZAO_SOCIAL))
             .andExpect(MockMvcResultMatchers.jsonPath("$.data.cnpj").value(CNPJ))
             .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty());
    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setId(ID);
        empresa.setRazaoSocial(RAZAO_SOCIAL);
        empresa.setCnpj(CNPJ);
        return empresa;
    }


   
}