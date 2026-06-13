package com.utad.apiAndroidBackend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.utad.apiAndroidBackend.entidades.ResultadoCloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServicio {

	private final Cloudinary cloudinary;

	public CloudinaryServicio(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	@SuppressWarnings("unchecked")
	public ResultadoCloudinary subirImagen(MultipartFile archivo) {

		if (archivo.isEmpty()) {
			throw new RuntimeException("Debes seleccionar una imagen");
		}

		if (archivo.getSize() > 100 * 1024 * 1024) {
			throw new RuntimeException("La imagen supera los 100 MB");
		}

		String tipo = archivo.getContentType();

		if (tipo == null || !tipo.startsWith("image/")) {
			throw new RuntimeException("Solo se permiten imágenes");
		}

		try {
			Map<String, Object> resultado = cloudinary.uploader().upload(archivo.getBytes(),
					ObjectUtils.asMap("folder", "animales", "resource_type", "image"));

			String url = resultado.get("secure_url").toString();
			String publicId = resultado.get("public_id").toString();

			return new ResultadoCloudinary(url, publicId);

		} catch (IOException e) {
			throw new RuntimeException("Error al subir imagen a Cloudinary", e);
		}
	}

	public void eliminarImagen(String publicId) {
		if (publicId == null || publicId.isBlank()) {
			return;
		}

		try {
			cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
		} catch (IOException e) {
			throw new RuntimeException("Error al eliminar imagen de Cloudinary", e);
		}
	}
}