package br.ufrn.PDSgrupo5.service;

import br.ufrn.PDSgrupo5.model.ProfissionalSaude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {
    private ProfissionalSaudeService profissionalSaudeService;

    @Autowired
    public ValidadorService(ProfissionalSaudeService ps){
        this.profissionalSaudeService = ps;
    }
    public void salvarEdicaoProfissionalSaude(ProfissionalSaude ps){
        ProfissionalSaude psAuxiliar = profissionalSaudeService.buscarProfissioalPorId(ps.getId());

        psAuxiliar.setSituacaoProfissionalSaude(ps.getSituacaoProfissionalSaude());
        psAuxiliar.setLegalizado(ps.isLegalizado());

        profissionalSaudeService.salvar(psAuxiliar);
    }
}
