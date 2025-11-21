package br.com.roadtowork.resource;


import br.com.roadtowork.dao.MetaDao;
import br.com.roadtowork.dao.UsuarioDao;
import br.com.roadtowork.dto.usuario.AtualizarUsuarioDto;
import br.com.roadtowork.dto.usuario.CadastroUsuarioDto;
import br.com.roadtowork.dto.usuario.DetalhesUsuarioDto;
import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Meta;
import br.com.roadtowork.model.Usuario;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private MetaDao metaDao;

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesUsuarioDto dto = modelMapper
                .map(usuarioDao.buscar(id), DetalhesUsuarioDto.class);
        return Response.ok(dto).build();
    }

    @GET
    public List<DetalhesUsuarioDto> listar() throws SQLException {
        return usuarioDao.listar().stream().map(
                p -> modelMapper.map(p, DetalhesUsuarioDto.class)
        ).toList();
    }

    @POST
    public Response create(@Valid CadastroUsuarioDto dto, @Context UriInfo uriInfo) throws SQLException {
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuarioDao.cadastrar(usuario);

        Meta meta = new Meta();
        meta.setUsuario(usuario);
        meta.setMetaTotal(40);
        meta.setAtual(0);
        meta.setConcluida(false);
        meta.setDataCriacao(LocalDate.now());

        metaDao.cadastrar(meta); // insere no banco

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(usuario.getId()))
                .build();

        return Response.created(uri)
                .entity(modelMapper.map(usuario, DetalhesUsuarioDto.class))
                .build(); // HTTP 201 Created
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarUsuarioDto dto) throws EntidadeNaoEncontradaException, SQLException {
        Usuario usuario = usuarioDao.buscar(id);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        usuarioDao.atualizar(usuario);

        return Response.ok(usuario).build();
    }
}
