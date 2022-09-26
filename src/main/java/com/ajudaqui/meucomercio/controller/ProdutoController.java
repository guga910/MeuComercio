package com.ajudaqui.meucomercio.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ajudaqui.meucomercio.dto.ProdutoDto;
import com.ajudaqui.meucomercio.modelo.Produto;
import com.ajudaqui.meucomercio.repository.ProdutoRepository;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	private ProdutoRepository repository;

	@PostMapping
	public ResponseEntity<ProdutoDto> create(@RequestBody ProdutoDto produtoDto, UriComponentsBuilder uriBuilder) {
		Produto produto = repository.save(produtoDto.converte());
		URI uri = uriBuilder.path("/produto/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(new ProdutoDto(produto));
	}

	@GetMapping
	public List<ProdutoDto> mostrarProduto() {
		List<Produto> produtos = new ArrayList<>();
		List<ProdutoDto> produtosDto = new ArrayList<>();
		produtos = repository.findAll();
//		produtos.stream().map(c -> produtosDto.add(new ProdutoDto(c)));
		produtos.forEach(p->{
			ProdutoDto pDto= new ProdutoDto(p);
			produtosDto.add(pDto);
		});
		return produtosDto;
		

	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDto> getProduto(@PathVariable Long id) {
		Optional<Produto> produto = repository.findById(id);
		if (produto.isPresent()) {
			return ResponseEntity.ok(new ProdutoDto(produto.get()));
		}
		return ResponseEntity.notFound().build();

	}
	
	@GetMapping(value="/nome/{nome}")
	public Produto buscarProdutoPorNome(@PathVariable String nome) {
		Produto protuot = repository.findByNome(nome);
		return protuot;
		
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProdutoDto> atualizar(@PathVariable Long id, @RequestBody ProdutoDto produtoDto) {
		Optional<Produto> produto = repository.findById(id);
		if (produto.isPresent()) {
			produto.get().setCategoria(produtoDto.getCategoria());
			produto.get().setDescricao(produtoDto.getDescricao());
//			produto.get().setId(produtoDto.getId());
			produto.get().setNome(produtoDto.getNome());
			produto.get().setValor(produtoDto.getValor());
			
			repository.save(produto.get());

			return ResponseEntity.ok(new ProdutoDto(produto.get()));
		}
		return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ProdutoDto> remover(@PathVariable Long id) {
		Optional<Produto> produto = repository.findById(id);
		if (produto.isPresent()) {

			repository.delete(produto.get());

			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();

	}

}
