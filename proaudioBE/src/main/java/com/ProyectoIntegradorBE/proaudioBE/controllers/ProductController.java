package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.services.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping
    private ProductResponseDto CreateProductComplete(
            @RequestPart("productRequestDto") ProductRequestDto productRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile[] files)
            throws BadRequestException {

        //TODO ADD MANDATORY BRAND TAG
        return productService.createProduct(productRequestDto, files);

    }

    @PutMapping("{id}")
    private ProductResponseDto UpdateProduct(@RequestBody ProductRequestDto productRequestDto,
                                             @PathVariable Long id) throws BadRequestException {

        return productService.UpdateProduct(productRequestDto, id);

    }

    @DeleteMapping("{id}")
    private ProductResponseDto DeleteProduct(@PathVariable Long id) throws BadRequestException {

        return productService.DeleteProduct(id);

    }

    @GetMapping("{id}")
    private ProductResponseDto GetProductById(@PathVariable Long id) {
        return productService.GetProduct(id);
    }

    @GetMapping("/all")
    private ProductListResponseDto ListProducts(
            @RequestParam(required = false) List<Long> tags,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) throws BadRequestException {

        return productService.getFilteredProducts(tags, sortBy, direction, startDate, endDate, page, size);

    }

    @GetMapping("/{id}/detail")
    private ProductDetailResponseDto GetProductDetails(@PathVariable Long id) {

        return productService.GetProductDetails(id);

    }

    @GetMapping("/status")
    private ProductStatusListDto GetProductStatuses() {

        return productService.GetProductStatuses();

    }


    //TAG

    @PostMapping("/tag")
    ProductTagResponseDto createProductTag(@RequestBody ProductTagRequestDto productTagRequestDto)
            throws BadRequestException {
        return productService.createProductTag(productTagRequestDto);
    }

    @DeleteMapping("/tag/{id}")
    ProductTagResponseDto DeleteProductTag(@PathVariable Long id) throws BadRequestException {
        return productService.DeleteProductTag(id);
    }

    //PHOTO

    @PostMapping("/photo/upload")
    PhotoResponseDto createPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productId") Long productId,
            @RequestParam("name") String name) throws BadRequestException {
        return productService.UploadPhoto(file, productId, name);
    }

    @PostMapping("/photo/upload-multiple")
    public List<PhotoResponseDto> uploadMultiplePhotos(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("productId") Long productId) throws BadRequestException {


        return productService.UploadMultiplePhotos(files, productId);
    }

    @PostMapping("/photo")
    PhotoResponseListDto createPhoto(@RequestBody PhotoRequestListDto photoRequestListDto) throws BadRequestException {
        return productService.CreatePhoto(photoRequestListDto);
    }

    @DeleteMapping("/photo/{id}")
    PhotoResponseDto deletePhoto(@PathVariable Long id) {
        return productService.DeletePhoto(id);
    }

    @GetMapping("/{id}/photos")
    PhotoResponseListDto GetProductPhotos(@RequestParam Long id) {
        return productService.GetProductPhotos(id);
    }

    //PRICE

    @PostMapping("/price")
    PriceReponseDto createPrice(@RequestBody PriceRequestDto priceRequestDto) throws BadRequestException {
        return productService.CreatePrice(priceRequestDto);
    }

    @DeleteMapping("/price/{id}")
    PriceReponseDto createPrice(@PathVariable Long id) throws BadRequestException {
        return productService.DeletePrice(id);
    }

}
