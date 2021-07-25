package br.ufrn.PDSgrupo5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.ufrn.PDSgrupo5.model.Atendimento;

@Service
@EnableScheduling
public class EmailService {
	private static final String TIME_ZONE = "America/Sao_Paulo";
	private JavaMailSender emailSender;
	private AtendimentoService atendimentoService;
	
	@Autowired
	EmailService(JavaMailSender emailSender, AtendimentoService atendimentoService){
		this.emailSender = emailSender;
		this.atendimentoService = atendimentoService;
	}
	
	public void enviarEmailSimples(String destinatario, String assunto, String mensagem) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom("sistemadesaudev1@gmail.com");
		email.setTo(destinatario);
		email.setSubject(assunto);
		email.setText(mensagem);
		
		emailSender.send(email);
	}
	
	public void enviarLembreteRetorno(Atendimento atendimento) {	
		String saudacao = "Olá, " + atendimento.getPaciente().getPessoa().getNome() + ", tudo bem?";
		
		String texto = "Notamos que já faz um tempo desde seu atendimento com o "
						+ atendimento.getProfissionalSaude().getPessoa().getNome()
						+ " (" + atendimento.getProfissionalSaude().getEnumTipoRegistro().getDescricao() + ")."
						+ " De acordo com nossa programação, esse período é ideal para realizar um retorno."
						+ "\nQue tal entrar no Sistema de Saúde v1.0 e realizar um novo agendamento?";
		
		String despedida = "Sistema de Saúde v1.0"
						+ "\n\nNota: Este e-mail foi gerado automaticamente. Por favor não responda esta mensagem.";
		
		String assunto = "Realização de retorno médico - Sistema de Saúde v1.0";
		String mensagem = saudacao + "\n\n" + texto + "\n\n" + despedida;
		enviarEmailSimples(atendimento.getPaciente().getPessoa().getEmail(), assunto, mensagem);
	}
	
	@Scheduled(cron = "0 0 12 * * *", zone = TIME_ZONE)
	//@Scheduled(cron = "* * * * * *", zone = TIME_ZONE) //a cada um minuto para teste
	public void enviarLembretesRetorno() {
		List<Atendimento> atendimentos = atendimentoService.buscarAtendimentosRequeremLembreteRetorno();
		for(Atendimento a : atendimentos) {
			enviarLembreteRetorno(a);
			a.setRequerLembreteRetorno(false);
			atendimentoService.salvar(a);
		}
	}
}
