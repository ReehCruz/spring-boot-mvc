package com.rebeca.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rebeca.springboot.model.Pessoa;
import com.rebeca.springboot.repository.PessoaRepository;

@Controller
public class PessoaController {
		
		@Autowired
		private PessoaRepository pessoaRepository;
		
		@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
		public ModelAndView inicio() {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoaobj", new Pessoa());
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
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			Optional<Pessoa> pessoa = pessoaRepository.findById(idpesssoa);
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

}
