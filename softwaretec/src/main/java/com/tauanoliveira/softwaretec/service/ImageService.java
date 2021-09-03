package com.tauanoliveira.softwaretec.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.tauanoliveira.softwaretec.service.exception.FileException;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    
    public BufferedImage getJpgImageFromFile(MultipartFile uploadFile) {
        String ext = FilenameUtils.getExtension(uploadFile.getOriginalFilename());//pega extenção do arquivo (.jpg, .png)
        if(!"png".equals(ext) && !"jpg".equals(ext)) {
            throw new FileException("Somente imagens .jpg e .png");
        }

        try {
            BufferedImage img = ImageIO.read(uploadFile.getInputStream());

            if("png".equals(ext)) {
                img = pngToJpg(img);
            }
            
            return img;
        } catch (IOException e) {
            throw new FileException("Erro na leitura do arquivo");
        }
    }

    public BufferedImage pngToJpg(BufferedImage img) {
        BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
        return jpgImage;
    }

    public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

    public BufferedImage cropSquare(BufferedImage sourceImg) {
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();//descobre se é a altura ou a largura q é menor
		return Scalr.crop(//crop recorta img
			sourceImg, 
			(sourceImg.getWidth()/2) - (min/2), //pega um ponto na metade da largura menos metade do tamanho min
			(sourceImg.getHeight()/2) - (min/2), 
			min, //soma o min no ponto encontrado
			min);		
	}
	
	public BufferedImage resize(BufferedImage sourceImg, int size) {//recebe img e tamanho da img recortada
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);//retorna img na qualidade maxima e redimencionada
	}
}
