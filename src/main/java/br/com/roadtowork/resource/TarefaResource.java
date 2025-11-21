package br.com.roadtowork.resource;

import br.com.roadtowork.dao.MetaDao;
import br.com.roadtowork.dao.TarefaDao;
import br.com.roadtowork.dao.UsuarioDao;
import br.com.roadtowork.dto.tarefa.AtualizarStatusConcluidoTarefaDto;
import br.com.roadtowork.dto.tarefa.AtualizarTarefaDto;
import br.com.roadtowork.dto.tarefa.CadastroTarefaDto;
import br.com.roadtowork.dto.tarefa.DetalhesTarefaDto;
import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Tarefa;
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
import java.util.List;

@Path("/tarefas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TarefaResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private TarefaDao tarefaDao;

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private MetaDao metaDao;

    @GET
    public List<DetalhesTarefaDto> listarPorUsuario(@QueryParam("usuarioId") int usuarioId) throws SQLException {
        return tarefaDao.listarPorUsuario(usuarioId).stream()
                .map(t -> modelMapper.map(t, DetalhesTarefaDto.class))
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        Tarefa tarefa = tarefaDao.buscar(id);
        DetalhesTarefaDto dto = modelMapper.map(tarefa, DetalhesTarefaDto.class);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/concluidas")
    public List<DetalhesTarefaDto> listarConcluidas(@QueryParam("usuarioId") int usuarioId) throws SQLException {
        return tarefaDao.listarConcluidas(usuarioId).stream()
                .map(t -> modelMapper.map(t, DetalhesTarefaDto.class))
                .toList();
    }

    @POST
    public Response create(@Valid CadastroTarefaDto dto, @Context UriInfo uriInfo) throws SQLException, EntidadeNaoEncontradaException {
        Tarefa tarefa = modelMapper.map(dto, Tarefa.class);

        Usuario usuario = usuarioDao.buscar(dto.getUsuario());
        tarefa.setUsuario(usuario);

        tarefaDao.cadastrar(tarefa);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(tarefa.getId()))
                .build();

        return Response.created(uri).entity(modelMapper.map(tarefa, DetalhesTarefaDto.class)).build();
    }

    @PUT
    @Path("/{id}/concluir")
    public Response atualizarStatusTarefa(@PathParam("id") int id, @Valid AtualizarStatusConcluidoTarefaDto dto) throws SQLException, EntidadeNaoEncontradaException {
        Tarefa tarefa = tarefaDao.buscar(id);

        boolean mudouParaConcluida = !tarefa.isConcluido() && dto.getConcluido();
        tarefa.setConcluido(dto.getConcluido());
        tarefaDao.atualizar(tarefa);

        // Se foi marcada como concluída agora, incrementa a meta
        if (mudouParaConcluida) {
            metaDao.incrementar(tarefa.getUsuario().getId());
        }

        return Response.ok(modelMapper.map(tarefa, DetalhesTarefaDto.class)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarTarefa(@PathParam("id") int id, @Valid AtualizarTarefaDto dto) throws EntidadeNaoEncontradaException, SQLException {
        Tarefa tarefa = tarefaDao.buscar(id);

        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());

        tarefaDao.atualizar(tarefa);

        return Response.ok(modelMapper.map(tarefa, DetalhesTarefaDto.class)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response apagar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        tarefaDao.apagar(id);
        return Response.noContent().build();
    }

}
