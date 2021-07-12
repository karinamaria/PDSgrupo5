package br.ufrn.PDSgrupo5.service;

import br.ufrn.PDSgrupo5.enumeration.EnumSituacaoProfissionalSaude;
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
    public void salvarEdicaoProfissionalSaude(Long idProfissional, boolean autorizacao, EnumSituacaoProfissionalSaude justificativa){
        ProfissionalSaude psAuxiliar = profissionalSaudeService.buscarProfissioalPorId(idProfissional);

        psAuxiliar.setSituacaoProfissionalSaude(justificativa);
        psAuxiliar.setLegalizado(autorizacao);

        profissionalSaudeService.salvar(psAuxiliar);
    }
}
