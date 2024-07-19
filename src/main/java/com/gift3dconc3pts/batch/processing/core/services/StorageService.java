package com.gift3dconc3pts.batch.processing.core.services;

import com.gift3dconc3pts.batch.processing.batch.properties.BatchProperties;
import com.gift3dconc3pts.batch.processing.core.exceptions.StorageException;
import com.gift3dconc3pts.batch.processing.core.exceptions.StorageFileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class StorageService {

	private final BatchProperties batchProperties;

	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = batchProperties.getFileUploadRootDirectory().resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(batchProperties
					.getFileUploadRootDirectory().toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	public Stream<Path> loadAll() {
		try {
			return Files.walk(batchProperties.getFileUploadRootDirectory(), 1)
				.filter(path -> !path.equals(batchProperties.getFileUploadRootDirectory()))
				.map(batchProperties.getFileUploadRootDirectory()::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	public Path load(String filename) {
		return batchProperties.getFileUploadRootDirectory().resolve(filename);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(batchProperties.getFileUploadRootDirectory().toFile());
	}

	public void init() {
		try {
			Files.createDirectories(batchProperties.getFileUploadRootDirectory());
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
