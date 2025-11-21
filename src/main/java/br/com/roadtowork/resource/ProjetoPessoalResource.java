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
import java.time.LocalDate;
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

    // --------------------------------------------------------
    // LISTAR TODOS (por usuário)
    // --------------------------------------------------------
    @GET
    @Path("/usuario")
    public List<DetalhesProjetoPessoalDto> listarPorUsuario(@QueryParam("usuarioId") int usuarioId)
            throws SQLException {

        return projetoDao.listarPorUsuario(usuarioId).stream()
                .map(p -> modelMapper.map(p, DetalhesProjetoPessoalDto.class))
                .toList();
    }

    // --------------------------------------------------------
    // BUSCAR POR ID
    // --------------------------------------------------------
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id)
            throws SQLException, EntidadeNaoEncontradaException {

        ProjetoPessoal projeto = projetoDao.buscar(id);
        DetalhesProjetoPessoalDto dto = modelMapper.map(projeto, DetalhesProjetoPessoalDto.class);

        return Response.ok(dto).build();
    }

    @POST
    public Response cadastrar(@Valid CadastroProjetoPessoalDto dto, @Context UriInfo uriInfo)
            throws SQLException, EntidadeNaoEncontradaException {

        ProjetoPessoal projeto = new ProjetoPessoal();

        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setUsuario(usuarioDao.buscar(dto.getUsuario()));

        // aplica defaults existentes no construtor ou entidade (concluido = false, dataCriacao = hoje)
        projetoDao.cadastrar(projeto);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(projeto.getId()))
                .build();

        DetalhesProjetoPessoalDto detalhes = modelMapper.map(projeto, DetalhesProjetoPessoalDto.class);
        return Response.created(uri).entity(detalhes).build();
    }

    // --------------------------------------------------------
    // ATUALIZAR (TÍTULO + DESCRIÇÃO SOMENTE)
    // NÃO pode alterar datas nem conclusão
    // --------------------------------------------------------
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarProjetoPessoalDto dto)
            throws SQLException, EntidadeNaoEncontradaException {

        ProjetoPessoal projeto = projetoDao.buscar(id);

        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());

        // não altera:
        // - dataCriacao
        // - dataConclusao
        // - concluido

        projetoDao.atualizar(projeto);

        return Response.ok().build();
    }

    // --------------------------------------------------------
    // ATUALIZAR STATUS (PATCH)
    // Se concluído == true → gera dataConclusao automaticamente
    // Se concluído == false → zera dataConclusao
    // --------------------------------------------------------
    @PUT
    @Path("/{id}/concluir")
    public Response atualizarStatusProjeto(
            @PathParam("id") int id,
            @Valid AtualizarStatusProjetoPessoalDto dto
    ) throws SQLException, EntidadeNaoEncontradaException {

        ProjetoPessoal projeto = projetoDao.buscar(id);

        boolean concluindoAgora = !projeto.isConcluido() && dto.getConcluido();
        boolean desmarcandoConclusao = projeto.isConcluido() && !dto.getConcluido();

        projeto.setConcluido(dto.getConcluido());

        if (concluindoAgora) {
            projeto.setDataConclusao(LocalDate.now());
        }
        else if (desmarcandoConclusao) {
            projeto.setDataConclusao(null);
        }

        projetoDao.atualizar(projeto);

        return Response.ok(modelMapper.map(projeto, DetalhesProjetoPessoalDto.class)).build();
    }
    @DELETE
    @Path("/{id}")
    public Response apagar(@PathParam("id") int id)
            throws SQLException, EntidadeNaoEncontradaException {

        projetoDao.apagar(id);
        return Response.noContent().build();
    }

}