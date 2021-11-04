package com.dev.virtualstore.controle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dev.virtualstore.modelos.FotoProduto;
import com.dev.virtualstore.modelos.Produto;
import com.dev.virtualstore.repositorios.CategoriaProdutoRepositorio;
import com.dev.virtualstore.repositorios.FotoProdutoRepositorio;
import com.dev.virtualstore.repositorios.MarcaProdutoRepositorio;
import com.dev.virtualstore.repositorios.ProdutoRepositorio;

@Controller
public class ProdutoControle {
	private static String caminhoImagens = "/home/flavio/Documentos/imagens/";
	private int quantidadeLote = 50000;

	@Autowired
	private ProdutoRepositorio produtoRepositorio;

	@Autowired
	private CategoriaProdutoRepositorio categoriaRepositorio;

	@Autowired
	private MarcaProdutoRepositorio marcaRepositorio;

	@Autowired
	private FotoProdutoRepositorio fotoProdutoRepositorio;
	
	@GetMapping("/administrativo/produtos/cadastrar")
	public ModelAndView cadastrar(Produto produto) {
		ModelAndView mv = new ModelAndView("/administrativo/produtos/cadastro");
		mv.addObject("produto", produto);
		mv.addObject("listaCategoriasProduto", this.categoriaRepositorio.findAll());
		mv.addObject("listaMarcasProduto", this.marcaRepositorio.findAll());

		return mv;
	}

	@GetMapping("/administrativo/produtos/listar")
	public ModelAndView listar() {
		return this.listar(this.produtoRepositorio.findAll());
	}

	public ModelAndView listar(List<Produto> produtos) {
		ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
		mv.addObject("listaProdutos", produtos);
		mv.addObject("listaCategoriasProduto", this.categoriaRepositorio.findAll());
		mv.addObject("listaMarcasProduto", this.marcaRepositorio.findAll());
		
		return mv;
	}
	
	@GetMapping("/administrativo/produtos/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Produto> produto = this.produtoRepositorio.findById(id);
		
		return cadastrar(produto.get());
	}
	
	@GetMapping("/administrativo/produtos/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Produto> produto = this.produtoRepositorio.findById(id);
		this.produtoRepositorio.delete(produto.get());
		
		return listar();
	}

	@GetMapping("/administrativo/produtos/mostrarImagem/{imagem}")
	@ResponseBody
	public byte[] retornarImagem(@PathVariable("imagem") String imagem) throws IOException {
		File imagemArquivo = new File(caminhoImagens+imagem);

		if (imagem != null || imagem.trim().length() > 0) {
			return Files.readAllBytes(imagemArquivo.toPath());
		}

		return null;
	}
	
	@PostMapping("/administrativo/produtos/salvar")
	public ModelAndView salvar(@Valid Produto produto, BindingResult result, @RequestParam("files") MultipartFile[] arquivos) {
		if (result.hasErrors()) 
			return cadastrar(produto);
		
		this.produtoRepositorio.saveAndFlush(produto);

		try {
			if (arquivos.length > 0) {
				for (MultipartFile arquivo : arquivos) {
					byte[] bytes = arquivo.getBytes();

					String nomeImagem = String.valueOf(produto.getId())+arquivo.getOriginalFilename();
	
					Path caminho =  Paths.get(caminhoImagens+nomeImagem);
					Files.write(caminho, bytes);

					FotoProduto fotoProduto = new FotoProduto(nomeImagem, produto);

					this.fotoProdutoRepositorio.saveAndFlush(fotoProduto);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this.listar();
	}

	@GetMapping("/administrativo/produtos/buscar")
	public ModelAndView filtrarProdutos(@RequestParam(name = "descricao", required = false) String descricao,
			@RequestParam(name = "categoria", required = false) Long categoria,
			@RequestParam(name = "marca", required = false) Long marca) {

		if (descricao != null) 
			return this.listar(this.produtoRepositorio.findAllByDescricao(descricao));
		else if (categoria != null)
			return this.listar(this.produtoRepositorio.findAllByCategoria(categoria));
		else if (marca != null)
			return this.listar(this.produtoRepositorio.findAllByMarca(marca));

		return this.listar();
	}

	@GetMapping("/administrativo/produtos/inserirLote")
	public ModelAndView inserirLote() {
		List<Produto> produtos = new ArrayList<Produto>();

		for (int i = 1; i <= this.quantidadeLote; i++) {
			Produto produto = new Produto();

			produto.setDescricao("Produto "+i);
			produto.setValorVenda(i+.5);
			produto.setQuantidadeEstoque(i*2.5);

			produtos.add(produto);
		}

		this.quantidadeLote += 50000;
		this.produtoRepositorio.saveAllAndFlush(produtos);

		return this.listar();
	}
	
}
