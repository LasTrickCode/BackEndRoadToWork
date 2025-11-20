package br.com.roadtowork.resource;

import br.com.roadtowork.dao.UsuarioDao;
import br.com.roadtowork.dto.login.LoginDto;
import br.com.roadtowork.dto.usuario.DetalhesUsuarioDto;
import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoginResource {

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private ModelMapper modelMapper;

    @POST
    @Path("/login")
    public Response login(@Valid LoginDto dto) throws SQLException {
        try {
            Usuario usuario = usuarioDao.login(dto.getEmail(), dto.getSenha());

            DetalhesUsuarioDto detalhesDto = modelMapper.map(usuario, DetalhesUsuarioDto.class);

            return Response.ok(detalhesDto).build();
        } catch (EntidadeNaoEncontradaException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("erro", "Email ou senha inválidos"))
                    .build();
        }
    }
}
