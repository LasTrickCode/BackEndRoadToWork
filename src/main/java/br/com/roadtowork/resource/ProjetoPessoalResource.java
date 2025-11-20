package br.com.roadtowork.resource;

import br.com.roadtowork.dao.ProjetoPessoalDao;
import br.com.roadtowork.dao.UsuarioDao;
import br.com.roadtowork.dto.projetopessoal.AtualizarProjetoPessoalDto;
import br.com.roadtowork.dto.projetopessoal.AtualizarStatusProjetoPessoalDto;
import br.com.roadtowork.dto.projetopessoal.CadastroProjetoPessoalDto;
import br.com.roadtowork.dto.projetopessoal.DetalhesProjetoPessoalDto;
import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.ProjetoPessoal;
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

@Path("/projetos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProjetoPessoalResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private ProjetoPessoalDao projetoDao;

    @Inject
    private UsuarioDao usuarioDao;

    @GET
    public List<DetalhesProjetoPessoalDto> listar() throws SQLException {
        return projetoDao.listar().stream()
                .map(p -> modelMapper.map(p, DetalhesProjetoPessoalDto.class))
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        ProjetoPessoal projeto = projetoDao.buscar(id);
        DetalhesProjetoPessoalDto dto = modelMapper.map(projeto, DetalhesProjetoPessoalDto.class);
        return Response.ok(dto).build();
    }

    @POST
    public Response cadastrar(@Valid CadastroProjetoPessoalDto dto, @Context UriInfo uriInfo) throws SQLException, EntidadeNaoEncontradaException {
        ProjetoPessoal projeto = modelMapper.map(dto, ProjetoPessoal.class);

        projeto.setUsuario(usuarioDao.buscar(dto.getUsuario()));

        projetoDao.cadastrar(projeto);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(projeto.getId()))
                .build();

        DetalhesProjetoPessoalDto detalhes = modelMapper.map(projeto, DetalhesProjetoPessoalDto.class);
        return Response.created(uri).entity(detalhes).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarProjetoPessoalDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ProjetoPessoal projeto = projetoDao.buscar(id);

        projeto.setPeriodo(dto.getPeriodo());
        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());

        projetoDao.atualizar(projeto);

        DetalhesProjetoPessoalDto detalhes = modelMapper.map(projeto, DetalhesProjetoPessoalDto.class);
        return Response.ok(detalhes).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response atualizarStatus(@PathParam("id") int id, @Valid AtualizarStatusProjetoPessoalDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ProjetoPessoal projeto = projetoDao.buscar(id);

        projeto.setConcluido(dto.getConcluido());
        projeto.setConclusao(dto.getConclusao());

        projetoDao.atualizarStatus(projeto);

        DetalhesProjetoPessoalDto detalhes = modelMapper.map(projeto, DetalhesProjetoPessoalDto.class);
        return Response.ok(detalhes).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        projetoDao.apagar(id);
        return Response.noContent().build(); // HTTP 204
    }

}

