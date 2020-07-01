package com.rebeca.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rebeca.springboot.model.Pessoa;
import com.rebeca.springboot.model.Telefone;
import com.rebeca.springboot.repository.PessoaRepository;
import com.rebeca.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {
		
		@Autowired
		private PessoaRepository pessoaRepository;
		
		@Autowired
		private TelefoneRepository telefoneRepository;
		
		@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
		public ModelAndView inicio() {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoaobj", new Pessoa());
			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			modelAndView.addObject("pessoas", pessoaIt);
			return modelAndView;
		}
		
		@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
		public ModelAndView salvar(Pessoa pessoa) {
			pessoaRepository.save(pessoa);
		
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			andView.addObject("pessoas", pessoaIt);
			andView.addObject("pessoaobj", new Pessoa());
			return andView;
		}
		
		@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
		public ModelAndView pessoas() {
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			andView.addObject("pessoas", pessoaIt);
			andView.addObject("pessoaobj", new Pessoa());
			return andView;
		}
		
		@GetMapping("/editarpessoa/{idpessoa}")
		public ModelAndView editar(@PathVariable("idpessoa") Long idpesssoa ) {
			
			Optional<Pessoa> pessoa = pessoaRepository.findById(idpesssoa);
			
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoaobj", pessoa.get());
			return modelAndView;		
		}
		
		@GetMapping("/removerpessoa/{idpessoa}")
		public ModelAndView excluir(@PathVariable("idpessoa") Long idpesssoa ) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			pessoaRepository.deleteById(idpesssoa);
			modelAndView.addObject("pessoas", pessoaRepository.findAll());
			modelAndView.addObject("pessoaobj", new Pessoa());
			return modelAndView;
		}
		
		@PostMapping("**/pesquisarpessoa")
		public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
			modelAndView.addObject("pessoaobj", new Pessoa());
			return modelAndView;
			
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
