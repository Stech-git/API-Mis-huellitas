package com.utad.apiAndroidBackend.imagenes;

import com.utad.apiAndroidBackend.animales.AnimalRepositorio;
import com.utad.apiAndroidBackend.config.CloudinaryServicio;
import com.utad.apiAndroidBackend.entidades.Animal;
import com.utad.apiAndroidBackend.entidades.ImagenAnimal;
import com.utad.apiAndroidBackend.entidades.ResultadoCloudinary;
import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImagenServicio {

	private final ImagenRepositorio imagenRepositorio;
	private final AnimalRepositorio animalRepositorio;
	private final CloudinaryServicio cloudinaryServicio;

	public ImagenServicio(ImagenRepositorio imagenRepositorio, AnimalRepositorio animalRepositorio,
			CloudinaryServicio cloudinaryServicio) {
		this.imagenRepositorio = imagenRepositorio;
		this.animalRepositorio = animalRepositorio;
		this.cloudinaryServicio = cloudinaryServicio;
	}

	private Animal validarAnimal(Long animalId, Usuario usuario) {

		Animal animal = animalRepositorio.buscarPorId(animalId);

		if (animal == null) {
			throw new RuntimeException("Animal no encontrado");
		}

		if (!animal.usuario_id.equals(usuario.id)) {
			throw new RuntimeException("No puedes modificar imágenes de este animal");
		}

		return animal;
	}

	public ImagenAnimal agregarImagen(Long animalId, Usuario usuario, String urlImagen) {

		validarAnimal(animalId, usuario);

		ImagenAnimal imagen = new ImagenAnimal();
		imagen.animalId = animalId;
		imagen.urlImagen = urlImagen;
		imagen.esPrincipal = false;

		return imagenRepositorio.crear(imagen);
	}

	public List<ImagenAnimal> listarImagenes(Long animalId) {
		return imagenRepositorio.obtenerPorAnimal(animalId);
	}

	public void marcarPrincipal(Long imagenId, Usuario usuario) {

		ImagenAnimal imagen = imagenRepositorio.buscarPorId(imagenId);

		if (imagen == null) {
			throw new RuntimeException("Imagen no encontrada");
		}

		validarAnimal(imagen.animalId, usuario);

		imagenRepositorio.desmarcarPrincipales(imagen.animalId);
		imagenRepositorio.marcarComoPrincipal(imagenId);
	}

	public void eliminarImagen(Long imagenId, Usuario usuario) {

		ImagenAnimal imagen = imagenRepositorio.buscarPorId(imagenId);
		if (imagen == null) {
			throw new RuntimeException("Imagen no encontrada");
		}
		validarAnimal(imagen.animalId, usuario);
		cloudinaryServicio.eliminarImagen(imagen.publicId);
		imagenRepositorio.eliminar(imagenId);
	}

	public ImagenAnimal subirImagenArchivo(Long animalId, Usuario usuario, MultipartFile archivo) {

		validarAnimal(animalId, usuario);
		ResultadoCloudinary resultado = cloudinaryServicio.subirImagen(archivo);
		ImagenAnimal imagen = new ImagenAnimal();
		imagen.animalId = animalId;
		imagen.urlImagen = resultado.url;
		imagen.publicId = resultado.publicId;
		imagen.esPrincipal = false;

		return imagenRepositorio.crear(imagen);
	}
}