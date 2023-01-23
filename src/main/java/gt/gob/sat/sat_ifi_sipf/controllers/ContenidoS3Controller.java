/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.waiters.Waiter;
import com.fasterxml.jackson.annotation.JsonRawValue;
import gt.gob.sat.sat_ifi_sipf.Config;
import gt.gob.sat.sat_ifi_sipf.dtos.S3FileDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase que contiene los métodos para consumos del gestor documental
 *
 * @author Carlos Ramos (crramosl)
 * @since 29/06/2022
 * @version 1.0
 */
@Api(tags = {"Contenido S3"})
@Validated
@RestController
@RequestMapping("/content/s3")
@Slf4j
public class ContenidoS3Controller {

    @Autowired
    AmazonS3 client;

    @Autowired
    Config config;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna un listado de identificadores y URLs prefirmadas con expiración de 1 minuto")
    public List<S3FileDto> getAllFiles() {
        List<S3FileDto> keys = new ArrayList<>();

        ObjectListing objects = client.listObjects(config.getBucketName());

        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }

            objectSummaries.stream().filter((item) -> (!item.getKey().endsWith("/"))).forEachOrdered((item) -> {
                GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(config.getBucketName(), item.getKey())
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + 60 * 1000));
                keys.add(new S3FileDto(item.getKey(), client.generatePresignedUrl(generatePresignedUrlRequest).toString()));
            });

            objects = client.listNextBatchOfObjects(objects);
        }

        return keys;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Carga un archivo y retorna una URL prefirmada con expiracion indeterminada")
    public S3FileDto uploadFile(@RequestParam String filename, @RequestBody MultipartFile file) {
        try {
            if (!FileUtils.isValidImage(file.getInputStream())) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "El archivo no es válido.");
            }

            client.putObject(config.getBucketName(), filename, file.getInputStream(), null);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(config.getBucketName(), filename)
                    .withMethod(HttpMethod.GET);
            return new S3FileDto(filename, client.generatePresignedUrl(generatePresignedUrlRequest).toString());
        } catch (IOException ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "No se estableció conexión segura con el servidor.");
        }
    }

    @DeleteMapping(path = "/folder/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Elimina un listado de documentos en base al nombre de la carpeta padre.")
    public void deleteFilesInFolder(@PathVariable String name) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(config.getBucketName())
                .withPrefix(name);

        ObjectListing objectListing = client.listObjects(listObjectsRequest);

        while (true) {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                client.deleteObject(config.getBucketName(), objectSummary.getKey());
            }
            if (objectListing.isTruncated()) {
                objectListing = client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }
}
