package br.com.roadtowork.resource;

import br.com.roadtowork.dao.MetaDao;
import br.com.roadtowork.dao.ProjetoPessoalDao;
import br.com.roadtowork.dao.TarefaDao;
import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Meta;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Map;

@Path("/dashboard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    @Inject
    private TarefaDao tarefaDao;

    @Inject
    private ProjetoPessoalDao projetoDao;

    @Inject
    private MetaDao metaDao;

    @GET
    @Path("/{usuarioId}")
    public Response dashboard(@PathParam("usuarioId") int usuarioId) throws SQLException, EntidadeNaoEncontradaException {
        int tarefasConcluidas = tarefaDao.contarConcluidas(usuarioId);
        int projetosConcluidos = projetoDao.contarConcluidos(usuarioId);
        Meta meta = metaDao.buscarPorUsuario(usuarioId);

        Map<String, Object> stats = Map.of(
                "tarefasConcluidas", tarefasConcluidas,
                "projetosConcluidos", projetosConcluidos,
                "metaAtual", meta.getAtual(),
                "metaTotal", meta.getMetaTotal(),
                "metaConcluida", meta.isConcluida()
        );

        return Response.ok(stats).build();
    }
}
