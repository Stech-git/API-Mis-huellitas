package com.utad.apiAndroidBackend.animales;

import com.utad.apiAndroidBackend.entidades.Animal;
import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServicio {

	private final AnimalRepositorio animalRepositorio;

	public AnimalServicio(AnimalRepositorio animalRepositorio) {
		this.animalRepositorio = animalRepositorio;
	}

	// Obtener animal por ID
	public Animal obtener(Long animalId) {
		Animal animal = animalRepositorio.buscarPorId(animalId);
		if (animal == null) {
			throw new Excepciones.AnimalNoEncontradoException("Animal no encontrado");
		}
		if (animal.visibilidad.equals("OCULTA")) {
			throw new Excepciones.PublicacionOcultaException("La publicación está oculta");
		}
		if (animal.visibilidad.equals("ELIMINADA")) {
			throw new Excepciones.PublicacionEliminadaException("La publicación fue eliminada");
		}
		return animal;
	}

	// Crear animal
	public Animal crear(Usuario usuario, Animal datos) {

	    Animal nuevo = new Animal();
	    nuevo.usuario_id = usuario.id;
	    nuevo.nombre = datos.nombre;
	    nuevo.especie = datos.especie;
	    nuevo.raza = datos.raza;
	    nuevo.categoria_edad = datos.categoria_edad;
	    nuevo.genero = datos.genero;
	    nuevo.tamano = datos.tamano;
	    nuevo.descripcion = datos.descripcion;
	    nuevo.ciudad = datos.ciudad;
	    nuevo.estado_actual = datos.estado_actual;
	    nuevo.visibilidad = datos.visibilidad;
	    nuevo.contador_visitas = 0;
	    Long idGenerado = animalRepositorio.crear(nuevo);

	    return animalRepositorio.buscarPorId(idGenerado);
	}


	// Editar animal
	public Animal editar(Long animalId, Usuario usuario, Animal datos) {
		Animal existente = animalRepositorio.buscarPorId(animalId);
		if (existente == null) {
			throw new Excepciones.AnimalNoEncontradoException("Animal no encontrado");
		}
		if (!existente.usuario_id.equals(usuario.id)) {
			throw new Excepciones.AnimalNoPerteneceAlUsuarioException("No puedes editar este animal");
		}
		existente.nombre = datos.nombre;
		existente.especie = datos.especie;
		existente.raza = datos.raza;
		existente.categoria_edad = datos.categoria_edad;
		existente.genero = datos.genero;
		existente.tamano = datos.tamano;
		existente.descripcion = datos.descripcion;
		existente.ciudad = datos.ciudad;
		existente.estado_actual = datos.estado_actual;
		animalRepositorio.actualizar(existente);
		return existente;
	}
	
	public List<Animal> listarPorUsuario(Long usuarioId) {
	    return animalRepositorio.listarPorUsuario(usuarioId);
	}

	// Listar animales (sin filtros)
	public List<Animal> listar() {
		return animalRepositorio.listar();
	}

	// Listar animales con filtros (para la pantalla principal)
	public List<Animal> listarConFiltros(String estado, String especie, String edad) {
		return animalRepositorio.listarConFiltros(estado, especie, edad);
	}

	// Ocultar
	public void ocultar(Long animalId, Usuario usuario) {
		Animal animal = animalRepositorio.buscarPorId(animalId);
		if (animal == null) {
			throw new Excepciones.AnimalNoEncontradoException("Animal no encontrado");
		}
		if (!animal.usuario_id.equals(usuario.id)) {
			throw new Excepciones.AnimalNoPerteneceAlUsuarioException("No puedes ocultar este animal");
		}
		animalRepositorio.cambiarVisibilidad(animalId, "OCULTA");
	}

	// Restaurar
	public void restaurar(Long animalId, Usuario usuario) {
		Animal animal = animalRepositorio.buscarPorId(animalId);
		if (animal == null) {
			throw new Excepciones.AnimalNoEncontradoException("Animal no encontrado");
		}
		if (!animal.usuario_id.equals(usuario.id)) {
			throw new Excepciones.AnimalNoPerteneceAlUsuarioException("No puedes restaurar este animal");
		}
		animalRepositorio.cambiarVisibilidad(animalId, "PUBLICADA");
	}

	// Eliminar
	public void eliminar(Long animalId, Usuario usuario) {
		Animal animal = animalRepositorio.buscarPorId(animalId);
		if (animal == null) {
			throw new Excepciones.AnimalNoEncontradoException("Animal no encontrado");
		}
		if (!animal.usuario_id.equals(usuario.id)) {
			throw new Excepciones.AnimalNoPerteneceAlUsuarioException("No puedes eliminar este animal");
		}
		animalRepositorio.cambiarVisibilidad(animalId, "ELIMINADA");
	}
}
