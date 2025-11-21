package br.com.roadtowork.resource;

import br.com.roadtowork.dao.MetaDao;
import br.com.roadtowork.dao.UsuarioDao;
import br.com.roadtowork.dto.meta.AtualizarStatusMetaDto;
import br.com.roadtowork.dto.meta.CadastroMetaDto;
import br.com.roadtowork.dto.meta.DetalhesMetaDto;
import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Meta;
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
import java.util.List;

@Path("/metas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetaResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private MetaDao metaDao;

    @Inject
    private UsuarioDao usuarioDao;

    @GET
    public List<DetalhesMetaDto> listar() throws SQLException {
        return metaDao.listar().stream()
                .map(m -> modelMapper.map(m, DetalhesMetaDto.class))
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        Meta meta = metaDao.buscar(id);
        DetalhesMetaDto dto = modelMapper.map(meta, DetalhesMetaDto.class);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/usuario")
    public Response buscarPorUsuario(@QueryParam("usuarioId") int usuarioId) throws SQLException, EntidadeNaoEncontradaException {
        Meta meta = metaDao.buscarPorUsuario(usuarioId);
        DetalhesMetaDto dto = modelMapper.map(meta, DetalhesMetaDto.class);
        return Response.ok(dto).build();
    }

    @POST
    public Response cadastrar(@Valid CadastroMetaDto dto, @Context UriInfo uriInfo) throws SQLException, EntidadeNaoEncontradaException {
        Meta meta = new Meta(); // aplica defaults automaticamente
        meta.setUsuario(usuarioDao.buscar(dto.getUsuario()));

        metaDao.cadastrar(meta);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(meta.getId()))
                .build();

        DetalhesMetaDto detalhes = modelMapper.map(meta, DetalhesMetaDto.class);
        return Response.created(uri).entity(detalhes).build();
    }

    @PATCH
    @Path("/incrementar/{idUsuario}")
    public Response incrementarMeta(@PathParam("idUsuario") int idUsuario) throws SQLException, EntidadeNaoEncontradaException {
        metaDao.incrementar(idUsuario); // DAO faz incremento + marca concluida se atingir metaTotal
        return Response.ok().build();
    }
}
