package com.mx.core.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mx.core.model.ArchivoDTO;
import com.mx.core.model.ResponseGeneric;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/documentum")
public class DocumentumController {

	@PostMapping
	public ResponseEntity<ResponseGeneric<String>> checkStatusFtp() {

	    DateTimeFormatter folderFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	    DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

	    LocalDateTime now = LocalDateTime.now();
	    String folderName = now.format(folderFormatter);       // carpeta: yyyyMMddHHmmss
	    String timestamp = now.format(fileFormatter);          // texto dentro del PDF: yyyyMMdd_HHmmss

	    String baseFolder = "src/main/resources/Documentos/";
	    File directory = new File(baseFolder + folderName);
	    if (!directory.exists()) {
	        directory.mkdirs();
	    }

	    String fileName = "archivo_" + generateRandomString(3) + ".pdf";
	    String filePath = directory.getAbsolutePath() + "/" + fileName;

	    try {
	    	
	        Document document = new Document();
	        PdfWriter.getInstance(document, new FileOutputStream(filePath));
	        document.open();
	        document.add(new Paragraph("Archivo generado el " + timestamp));
	        document.close();

	        log.debug("Archivo PDF creado: " + filePath);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError()
	            .body(ResponseGeneric.buildError("Error al generar el PDF"));
	    }

	    return ResponseEntity.ok(ResponseGeneric.buildSuccess("Archivo PDF creado", filePath));
	    
	}
	
	@GetMapping
    public ResponseEntity<List<ArchivoDTO>> getDocumentTree() {
        String basePath = "src/main/resources/Documentos/";
        File rootFolder = new File(basePath);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return ResponseEntity.badRequest().build();
        }
        List<ArchivoDTO> children = scanFolderRecursive(rootFolder);
        ArchivoDTO rootNode = new ArchivoDTO("root", "Documentos", children);
        return ResponseEntity.ok(List.of(rootNode));
    }
    
    @GetMapping("/base64")
    public ResponseEntity<ResponseGeneric<String>> getFileAsBase64(
            @RequestParam String path) {

        try {
        	
            String baseDir = "src/main/resources/";
            File file = new File(baseDir + path);

            if (!file.exists()) {
                return ResponseEntity.badRequest().body(
                        ResponseGeneric.buildError("El archivo no existe: " + path));
            }

            if (file.isDirectory()) {
                return ResponseEntity.badRequest().body(
                        ResponseGeneric.buildError("La ruta no es un archivo: " + path));
            }

            byte[] content = FileUtils.readFileToByteArray(file);
            String base64 = Base64.getEncoder().encodeToString(content);

            return ResponseEntity.ok(
                    ResponseGeneric.buildSuccess("Archivo convertido exitosamente", base64));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseGeneric.buildError("Error al convertir archivo: " + e.getMessage()));
        }
    }
    
	private String generateRandomString(int length) {
	    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    return sb.toString();
	}

    private List<ArchivoDTO> scanFolderRecursive(File folder) {
        List<ArchivoDTO> lista = new ArrayList<>();
        File[] contenidos = folder.listFiles();

        if (contenidos != null) {
            for (File archivo : contenidos) {
                String id = UUID.randomUUID().toString();
                String nombre = archivo.getName();
                if (archivo.isDirectory()) {
                    List<ArchivoDTO> hijos = scanFolderRecursive(archivo);
                    lista.add(new ArchivoDTO(id, nombre, hijos));
                } else {
                    lista.add(new ArchivoDTO(id, nombre));
                }
            }
        }
        return lista;
    }
	
}