package com.rebeca.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rebeca.springboot.model.Pessoa;
import com.rebeca.springboot.model.Telefone;
import com.rebeca.springboot.repository.PessoaRepository;
import com.rebeca.springboot.repository.ProfissaoRepository;
import com.rebeca.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {
		
		@Autowired
		private PessoaRepository pessoaRepository;
		
		@Autowired
		private TelefoneRepository telefoneRepository;
		
		@Autowired
		private ReportUtil<Pessoa> reportUtil;
		
		@Autowired
		private ProfissaoRepository profissaoRepository;
		
		@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
		public ModelAndView inicio() {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoaobj", new Pessoa());
			modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			modelAndView.addObject("profissoes", profissaoRepository.findAll());
			return modelAndView;
		}
		
		@GetMapping("/pessoaspag")
		public ModelAndView carregaPessoaPorPaginacao(@PageableDefault(size = 5) Pageable pageable,
				ModelAndView model, @RequestParam("nomepesquisa") String nomepesquisa) {
			
			Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
			model.addObject("pessoas", pagePessoa);
			model.addObject("pessoaobj", new Pessoa());
			model.addObject("nomepesquisa", nomepesquisa);
			model.setViewName("/cadastro/cadastropessoa");
			
			return model;
			
			
		}
		
		@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa", 
				consumes = {"multipart/form-data"})
		public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, 
				final MultipartFile file) throws IOException {
			
			pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
			
			if (bindingResult.hasErrors()) {
				ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
				modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
				modelAndView.addObject("pessoaobj", pessoa);
				
				List<String> msg = new ArrayList<String>();
				for (ObjectError objectErro : bindingResult.getAllErrors()) {
					msg.add(objectErro.getDefaultMessage()); //vem das anotações @NotEmpty e outras
				}
				
				modelAndView.addObject("msg", msg);
				return modelAndView;
			}
			
			if (file.getSize() > 0) { /*Cadastrando um curriculo*/
				pessoa.setCurriculo(file.getBytes());
				pessoa.setTipoFileCurriculo(file.getContentType());
				pessoa.setNomeFileCurriculo(file.getOriginalFilename());
			} else { // se esta cadastrada no banco 
				if (pessoa.getId() != null && pessoa.getId() > 0) {// editando
					
					Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
				
					pessoa.setCurriculo(pessoaTemp.getCurriculo());
					pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
					pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
				}
			}
			
			pessoaRepository.save(pessoa);
		
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			andView.addObject("pessoaobj", new Pessoa());
			return andView;
		}
		
		@GetMapping("**/baixarcurriculo/{idpessoa}")
		public void baixarcurriculo(@PathVariable("idpessoa") Long idpessoa, 
				HttpServletResponse response) throws IOException {
		
			/* Consultar objeto pessoa no banco de dados */
			Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
			if(pessoa.getCurriculo() != null) {
			
				/* Setar o tamanho da resposta */
				response.setContentLength(pessoa.getCurriculo().length);
				
				/*Tipo do arquivo para download ou pode ser generica applicaton/octet-stream */ 
				response.setContentType(pessoa.getTipoFileCurriculo());
				
				/* Defini o cabecalho da resposta */
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
				response.setHeader(headerKey, headerValue);
				
				/* Finaliza a resposta passando o arquivo */
				response.getOutputStream().write(pessoa.getCurriculo());
			}
		}
		
		
		
		@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
		public ModelAndView pessoas() {
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			andView.addObject("pessoaobj", new Pessoa());
			return andView;
		}
		
		@GetMapping("/editarpessoa/{idpessoa}")
		public ModelAndView editar(@PathVariable("idpessoa") Long idpesssoa ) {
			
			Optional<Pessoa> pessoa = pessoaRepository.findById(idpesssoa);
			
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoaobj", pessoa.get());
			modelAndView.addObject("profissoes", profissaoRepository.findAll());
			return modelAndView;		
		}
		
		@GetMapping("/removerpessoa/{idpessoa}")
		public ModelAndView excluir(@PathVariable("idpessoa") Long idpesssoa ) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			pessoaRepository.deleteById(idpesssoa);
			modelAndView.addObject("pessoas",pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			modelAndView.addObject("pessoaobj", new Pessoa());
			return modelAndView;
		}
		
		@PostMapping("**/pesquisarpessoa")
		public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, 
				@RequestParam("pesqsexo") String pesqsexo, 
				@PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
			
			Page<Pessoa> pessoas = null;
			
			if (pesqsexo != null && !pesqsexo.isEmpty()) {
				pessoas = pessoaRepository.findPessoaByNameSexoPage(nomepesquisa, pesqsexo, pageable);
			} else {	
				pessoas = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
			}					
					
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoas", pessoas);
			modelAndView.addObject("pessoaobj", new Pessoa());
			modelAndView.addObject("nomepesquisa", nomepesquisa);
			return modelAndView;
			
		}
		
		@GetMapping("**/pesquisarpessoa")
		public void  imprimePdf(@RequestParam("nomepesquisa") String nomepesquisa, 
				@RequestParam("pesqsexo") String pesqsexo, HttpServletRequest request, 
				HttpServletResponse response) throws Exception {
			
			List<Pessoa> pessoas = new ArrayList<Pessoa>();
			
			if (pesqsexo != null && !pesqsexo.isEmpty()
					&& nomepesquisa != null && !nomepesquisa.isEmpty()) { /* Busca por nome e sexo */
				
				pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa, pesqsexo);
			
			} else if(nomepesquisa != null && !nomepesquisa.isEmpty()) { // Busca por nome
				
				pessoas = pessoaRepository.findPessoaByName(nomepesquisa);
			
			} else if(pesqsexo != null && !pesqsexo.isEmpty()) { // Busca por sexo
				
				pessoas = pessoaRepository.findPessoaBySexo(pesqsexo);
			
			} else { // Busca Todos 
				Iterable<Pessoa> interator = pessoaRepository.findAll();
				for (Pessoa pessoa : interator) {
					pessoas.add(pessoa);
				}
			}
			
			/* Chama os serviço que faz a geração de ralatorio */
			byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
			
			/*Tamanho da resposta do navegador */
			response.setContentLength(pdf.length);
			
			/* Definir na resposta o tipo de arquivo */
			response.setContentType("application/octet-stream");
			
			/* Definir o cabeçaho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue  = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
			response.setHeader(headerKey, headerValue);
			
			/*Finaliza a resposta pro navegador*/
			response.getOutputStream().write(pdf);
		}
		
		
		@GetMapping("/telefones/{idpessoa}")
		public ModelAndView telefone(@PathVariable("idpessoa") Long idpessoa ) {
			Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa.get());
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
			return modelAndView;		
		}
		
		@PostMapping("**/addfonePessoa/{pessoaid}")
		public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {
		
			Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
			
			if (telefone != null && telefone.getNumero().isEmpty()
					|| telefone.getTipo().isEmpty()) {
						
				ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
				modelAndView.addObject("pessoaobj", pessoa);
				modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
						
				List<String> msg = new ArrayList<String>();
				if (telefone.getNumero().isEmpty()){
					msg.add("Numero deve ser informado");
				}
				if (telefone.getTipo().isEmpty()) {
					msg.add("Tipo deve ser informado");
				}
				modelAndView.addObject("msg", msg);
				
				return modelAndView;
			}
			
				
			telefone.setPessoa(pessoa);
			telefoneRepository.save(telefone);
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
			return modelAndView;	
		}
		
		@GetMapping("/removertelefone/{idtelefone}")
		public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone ) {
		
			Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
			telefoneRepository.deleteById(idtelefone);
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
			return modelAndView;
		}
			
}
