
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.proyecto.server_backend.ServerBackendApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;




@SpringBootTest(classes = ServerBackendApplication.class) // Sustituye por el nombre de tu clase @SpringBootApplication
@AutoConfigureMockMvc
class PruebasLogin {
	
	@Autowired
    private MockMvc mockMvc;

	 // CAMINO 1: ÉXITO
    @Test
    void testCamino1_LoginExitoso() throws Exception {
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"javierms\", \"password\":\"12345678\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists()); // Verifica que genera el Token
    }

    // CAMINO 4: FORMATO INVÁLIDO (JSON Malformado)
    @Test
    void testCamino4_FormatoInvalido() throws Exception {
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"user\":}")) // JSON roto a propósito
            .andExpect(status().isBadRequest());
    }

    // CAMINO 5: USUARIO NO REGISTRADO
    @Test
    void testCamino5_UsuarioNoExiste() throws Exception {
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"usuario_fantasma\", \"password\":\"1234\"}"))
            .andExpect(status().isUnauthorized());
    }

    // CAMINO 6: PASSWORD INCORRECTA
    @Test
    void testCamino6_PasswordIncorrecta() throws Exception {
        mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"javierms44\", \"password\":\"password_erronea\"}"))
            .andExpect(status().isUnauthorized());
    }
}